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
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Systems.AssetManager;
import com.fikri.frontend.objects.Systems.EntityFactory;

public class Main extends ApplicationAdapter {

    // Field class Main sesuai Soal 1 Langkah 3
    private SpriteBatch batch;
    private Player player;
    private List<Fairy> fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;

    @Override

    public void create() {
        // TODO 1: Buat SpriteBatch
        batch = new SpriteBatch();

        // TODO 2: Inisialisasi list fairy dan entities
        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        // TODO 3: Inisialisasi AssetManager
        AssetManager.getInstance().init();

        // TODO 4: Buat Player, Fairy, Boss, dan Item sesuai tabel
        player = EntityFactory.createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);

        Fairy redFairy = EntityFactory.createFairy(150, 380, "Red Fairy", 20);
        Fairy blueFairy = EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue");

        fairy.add(redFairy);
        fairy.add(blueFairy);

        boss = EntityFactory.createBoss(380, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(200, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(320, 480, ItemType.POINT);

        // TODO 5: Masukkan semua objek ke entities
        entities.add(player);
        entities.addAll(fairy);
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
                // Soal 1 Langkah 4: Gambar entitas mengoper SpriteBatch
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

        // Soal 1 Langkah 4: Pelepasan AssetManager
        AssetManager.getInstance().dispose();
    }
}
