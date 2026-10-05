package com.fikri.frontend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.Systems.AssetManager;
import com.fikri.frontend.objects.Systems.EntityFactory;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private List<GameObject> entities;
    private Player player;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // 1. Inisialisasi AssetManager (Singleton)
        AssetManager.getInstance().init();

        entities = new ArrayList<>();

        // 2. Instansiasi objek menggunakan EntityFactory
        player = EntityFactory.createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);

        GameObject fairy = EntityFactory.createFairy(150, 380, "Stage 1 Fairy", 20);
        GameObject boss = EntityFactory.createBoss(380, 400, "Cirno (Stage 2 Boss)", 150);
        GameObject powerItem = EntityFactory.createItem(200, 450, ItemType.POWER);
        GameObject pointItem = EntityFactory.createItem(320, 480, ItemType.POINT);

        // Add semua entity ke List<GameObject>
        entities.add(player);
        entities.add(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);
            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                System.out.println("Remove via Generic iterator : " + entity.getClass().getSimpleName());
                iterator.remove();
            }
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input != null && Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            Bullet newBullet = player.shootBullet();
            entities.add(newBullet);
        }

        updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (!a.isDestroyed() && !b.isDestroyed()) {
                    if (a.getCoreHitbox().overlaps(b.getCoreHitbox())) {
                        a.onCollision(b);
                        b.onCollision(a);
                    }
                }
            }
        }

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin();
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        AssetManager.getInstance().dispose();
    }
}
