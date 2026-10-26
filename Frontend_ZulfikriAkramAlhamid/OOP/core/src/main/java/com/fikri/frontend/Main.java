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
import com.fikri.frontend.objects.Systems.AudioManager;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Systems.AssetManager;
import com.fikri.frontend.objects.Systems.EntityFactory;
import com.fikri.frontend.objects.Systems.BulletManager;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private List<Fairy> fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;
    private BulletManager bulletManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        fairy = new ArrayList<>();
        entities = new ArrayList<>();
        bulletManager = new BulletManager();

        AssetManager.getInstance().init();
        AudioManager.getInstance().init();
        AudioManager.getInstance().playBgm();

        player = EntityFactory.createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);

        Fairy redFairy = EntityFactory.createFairy(120, 250, "Red Fairy", 20);
        Fairy blueFairy = EntityFactory.createFairy(240, 250, "Blue Fairy", 20, "fairy_idle_blue");

        fairy.add(redFairy);
        fairy.add(blueFairy);

        boss = EntityFactory.createBoss(380, 260, "Rumia", 150);

        powerItem = EntityFactory.createItem(180, 320, ItemType.POWER);
        pointItem = EntityFactory.createItem(300, 320, ItemType.POINT);

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
                iterator.remove();
            }
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input != null && Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player.shootBullet(bulletManager);
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
        AudioManager.getInstance().dispose();
    }
}
