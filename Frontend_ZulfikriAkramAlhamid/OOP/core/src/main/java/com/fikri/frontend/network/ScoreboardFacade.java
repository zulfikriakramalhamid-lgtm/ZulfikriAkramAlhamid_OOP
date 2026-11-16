package com.fikri.frontend.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScoreboardFacade {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final int TIMEOUT_MS = 3000;

    private final Json json;

    public ScoreboardFacade() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
    }

    private static class ScoreSubmission {
        private String playerName;
        private int point;

        private ScoreSubmission() {}

        private ScoreSubmission(String playerName, int point) {
            this.playerName = playerName;
            this.point = point;
        }
    }

    public void checkHealth(Consumer<Boolean> onResult) {
        // TODO (2 poin):
        // Kirim request get("/health") memakai send(...).
        // Callback respons mengembalikan onResult.accept(status == HttpStatus.SC_OK);
        // callback error mengembalikan onResult.accept(false).
        send(get("/health"),
            (status, body) -> onResult.accept(status == HttpStatus.SC_OK),
            error -> onResult.accept(false));
    }

    public void submitScore(String playerName, long points, Consumer<ScoreEntry> onSaved, Consumer<String> onError) {
        // TODO (4 poin):
        // 1. Clamp `points` ke rentang [0, Integer.MAX_VALUE] menjadi int `point`.
        // 2. Bangun HttpRequest POST ke BASE_URL + "/scores" dengan HttpRequestBuilder
        //    (header "Content-Type": "application/json", content json.toJson(new ScoreSubmission(playerName, point)),
        //    dan timeout TIMEOUT_MS).
        // 3. Kirim memakai send(...):
        //    - Jika status == HttpStatus.SC_CREATED: onSaved.accept(json.fromJson(ScoreEntry.class, body)).
        //    - Jika status lain: onError.accept("Submit failed (HTTP " + status + ")").
        //    - Callback error langsung diteruskan ke onError.
        int point = (int) Math.max(0L, Math.min(points, (long) Integer.MAX_VALUE));

        HttpRequest request = new HttpRequestBuilder().newRequest()
            .method(HttpMethods.POST)
            .url(BASE_URL + "/scores")
            .header("Content-Type", "application/json")
            .content(json.toJson(new ScoreSubmission(playerName, point)))
            .timeout(TIMEOUT_MS)
            .build();

        send(request, (status, body) -> {
            if (status == HttpStatus.SC_CREATED) {
                onSaved.accept(json.fromJson(ScoreEntry.class, body));
            } else {
                onError.accept("Submit failed (HTTP " + status + ")");
            }
        }, onError);
    }

    @SuppressWarnings("unchecked")
    public void fetchLeaderboard(int limit, Consumer<List<ScoreEntry>> onLoaded, Consumer<String> onError) {
        // TODO (4 poin):
        // 1. Kirim GET ke "/scores/leaderboard?limit=" + limit memakai send(get(...), ...).
        // 2. Jika status == HttpStatus.SC_OK: parse JSON array via
        //       Array<ScoreEntry> entries = json.fromJson(Array.class, ScoreEntry.class, body);
        //    salin ke List<ScoreEntry> (waspadai entries == null), lalu panggil onLoaded.accept(result).
        // 3. Jika status lain: panggil onError.accept("Leaderboard failed (HTTP " + status + ")").
        send(get("/scores/leaderboard?limit=" + limit), (status, body) -> {
            if (status == HttpStatus.SC_OK) {
                Array<ScoreEntry> entries = json.fromJson(Array.class, ScoreEntry.class, body);
                List<ScoreEntry> result = new ArrayList<>();
                if (entries != null) {
                    for (ScoreEntry entry : entries) {
                        result.add(entry);
                    }
                }
                onLoaded.accept(result);
            } else {
                onError.accept("Leaderboard failed (HTTP " + status + ")");
            }
        }, onError);
    }

    // ====================================================
    // Boilerplate HTTP & Thread Dispatch
    // ====================================================
    private interface ResponseHandler {
        void handle(int status, String body);
    }

    private HttpRequest get(String path) {
        return new HttpRequestBuilder().newRequest()
            .method(HttpMethods.GET)
            .url(BASE_URL + path)
            .timeout(TIMEOUT_MS)
            .build();
    }

    private void send(HttpRequest request, ResponseHandler onResponse, Consumer<String> onError) {
        if (Gdx.net == null) {
            onError.accept("Scoreboard offline");
            return;
        }
        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse response) {
                int status = response.getStatus().getStatusCode();
                String body = response.getResultAsString();
                onRenderThread(() -> {
                    try {
                        onResponse.handle(status, body);
                    } catch (RuntimeException e) {
                        onError.accept("Unreadable reply from backend");
                    }
                });
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("[ScoreboardFacade] Backend unreachable: " + t.getMessage());
                onRenderThread(() -> onError.accept("Scoreboard offline"));
            }

            @Override
            public void cancelled() {
                onRenderThread(() -> onError.accept("Request cancelled"));
            }
        });
    }

    private void onRenderThread(Runnable task) {
        if (Gdx.app != null) {
            Gdx.app.postRunnable(task);
        } else {
            task.run();
        }
    }
}
