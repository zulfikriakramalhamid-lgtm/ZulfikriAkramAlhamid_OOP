package com.fikri.frontend;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.enemies.Boss;

public class Main extends ApplicationAdapter {

    private ShapeRenderer shapeRenderer;
    private List<GameObject> entities;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        entities = new ArrayList<>();

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
        Item powerItem = new Item(200, 450, 16, 16, 80f, ItemType.POWER, 500L);
        Item pointItem = new Item(320, 480, 12, 12, 120f, ItemType.POINT, 1000L);

        // Add semua entity ke List<GameObject>
        entities.add(player);
        entities.add(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Update semua GameObject
        for (GameObject obj : entities) {
            obj.update(delta);
        }

        // AABB Collision detection antara setiap pasangan unik entity
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (a.getCoreHitbox().overlaps(b.getCoreHitbox())) {
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }

        // Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // Render semua GameObject
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (GameObject obj : entities) {
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
