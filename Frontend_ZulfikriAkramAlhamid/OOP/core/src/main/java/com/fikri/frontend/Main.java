package com.netlab.frontend;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import com.fikri.frontend.GameObject;
import com.fikri.frontend.Item;
import com.fikri.frontend.Player;
import com.fikri.frontend.Fairy;
import com.fikri.frontend.Boss;

public class Main extends ApplicationAdapter {

    private ShapeRenderer shapeRenderer;
    private List<GameObject> gameObjects;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        gameObjects = new ArrayList<>();

        // Player
        Player player = new Player(
            280, 40,
            "Reimu Hakurei",
            100, 15, 3
        );

        // Fairy
        Fairy fairy = new Fairy(
            150, 380,
            "Stage 1 Fairy",
            20
        );

        // Boss
        Boss boss = new Boss(
            380, 400,
            "Cirno (Stage 2 Boss)",
            150
        );

        // Items
        Item pointItem1 = new Item(
            200, 450,
            12, 12,
            120f,
            "Point Item",
            1000L
        );

        Item pointItem2 = new Item(
            300, 550,
            12, 12,
            100f,
            "Point Item",
            1000L
        );

        Item pointItem3 = new Item(
            400, 650,
            12, 12,
            80f,
            "Point Item",
            1000L
        );

        // Add semua object ke List<GameObject>
        gameObjects.add(player);
        gameObjects.add(fairy);
        gameObjects.add(boss);
        gameObjects.add(pointItem1);
        gameObjects.add(pointItem2);
        gameObjects.add(pointItem3);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Update semua GameObject
        for (GameObject obj : gameObjects) {
            obj.update(delta);
        }

        // Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // Render semua GameObject
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (GameObject obj : gameObjects) {
            obj.render(shapeRenderer);
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
