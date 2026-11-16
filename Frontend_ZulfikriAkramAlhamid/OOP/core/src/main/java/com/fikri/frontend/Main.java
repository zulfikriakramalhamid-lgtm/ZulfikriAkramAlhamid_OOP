package com.fikri.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.fikri.frontend.network.ScoreboardFacade;
import com.fikri.frontend.objects.Systems.AssetManager;
import com.fikri.frontend.objects.Systems.AudioManager;
import com.fikri.frontend.objects.Systems.StageFacade;
import com.fikri.frontend.ui.GameHUD;

public class Main extends ApplicationAdapter {
    // TODO (1 poin):
    // Lengkapi konstanta dan field tambahan milik `Main` sesuai UML di atas
    // (konstanta nama pemain untuk disimpan ke leaderboard dengan username Anda sendiri,
    // batas ukuran leaderboard = 5, instance ScoreboardFacade, dan flag penanda agar skor
    // hanya dikirim satu kali per run).
    public static final String PLAYER_NAME = "Fikri";
    private static final int LEADERBOARD_SIZE = 5;

    private SpriteBatch batch;
    private GameHUD gameHUD;
    private StageFacade stage;
    private ScoreboardFacade scoreboard;
    private boolean scoreSubmitted = false;

    @Override
    public void create() {
        batch = new SpriteBatch();

        gameHUD = new GameHUD();

        AssetManager.getInstance().init();
        AudioManager.getInstance().init();
        AudioManager.getInstance().playBgm();

        stage = new StageFacade(gameHUD);
        // TODO (0.5 poin): Inisialisasi komponen network facade sesuai UML.
        scoreboard = new ScoreboardFacade();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input != null && Gdx.input.isKeyJustPressed(Input.Keys.R)
            && (stage.isGameOver() || stage.isStageClear())) {
            stage.restart();
            // TODO (0.5 poin): Reset penanda pengiriman skor untuk run baru sebelum membersihkan scoreboard di HUD.
            scoreSubmitted = false;
            gameHUD.clearScoreboard();
        }

        stage.update(delta);
        gameHUD.setStageClear(stage.isStageClear());
        // TODO (0.5 poin): Panggil method helper untuk memeriksa akhir run dan mengirim skor (lihat UML `Main`).
        submitScoreOnRunEnd();

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        batch.begin();
        stage.render(batch);
        gameHUD.render(batch);
        batch.end();

        gameHUD.renderBackground();
        stage.renderOverlay(gameHUD.getShapeRenderer());
    }

    private void submitScoreOnRunEnd() {
        // TODO (3.5 poin): Alur pengiriman skor & pengambilan leaderboard ke backend:
        // 1. Jika run belum berakhir (isGameOver() maupun isStageClear() masih false) ATAU skor run ini sudah dikirim, langsung return.
        // 2. Tandai skor sudah dikirim dan perbarui status HUD menjadi "Saving score...".
        // 3. Panggil scoreboard.submitScore(PLAYER_NAME, stage.getScore(), onSaved, onError):
        //    - Pada onSaved (saved): set status HUD menjadi "Saved as " + saved.getPlayerName(),
        //      lalu panggil scoreboard.fetchLeaderboard(LEADERBOARD_SIZE, gameHUD::showLeaderboard, gameHUD::setScoreboardStatus).
        //    - Pada onError: teruskan ke gameHUD::setScoreboardStatus.










































        if ((!stage.isGameOver() && !stage.isStageClear()) || scoreSubmitted) {
            return;
        }

        scoreSubmitted = true;
        gameHUD.setScoreboardStatus("Saving score...");

        scoreboard.submitScore(PLAYER_NAME, stage.getScore(),
            saved -> {
                gameHUD.setScoreboardStatus("Saved as " + saved.getPlayerName());
                scoreboard.fetchLeaderboard(LEADERBOARD_SIZE,
                    gameHUD::showLeaderboard,
                    gameHUD::setScoreboardStatus);
            },
            gameHUD::setScoreboardStatus);
    }

    @Override
    public void dispose() {

        if (batch != null) {
            batch.dispose();
        }

        if (gameHUD != null) {
            gameHUD.dispose();
        }

        AssetManager.getInstance().dispose();
        AudioManager.getInstance().dispose();
    }
}
