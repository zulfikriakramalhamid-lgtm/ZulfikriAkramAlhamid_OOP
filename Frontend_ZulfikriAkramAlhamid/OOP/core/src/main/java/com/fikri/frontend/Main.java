
package com.fikri.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.fikri.frontend.commands.InputHandler;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Player;

import com.fikri.frontend.objects.Systems.AssetManager;
import com.fikri.frontend.objects.Systems.AudioManager;
import com.fikri.frontend.objects.Systems.BulletManager;
import com.fikri.frontend.objects.Systems.CollisionReferee;
import com.fikri.frontend.objects.Systems.EntityFactory;

import com.fikri.frontend.ui.GameHUD;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Player player;
    private Boss boss;
    private List<Fairy> fairy;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;

    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;
    private InputHandler inputHandler;
    private GameHUD gameHUD;

    @Override
    public void create() {

        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        bulletManager = new BulletManager();
        collisionReferee = new CollisionReferee();

        AssetManager.getInstance().init();
        AudioManager.getInstance().init();

        player = EntityFactory.createPlayer(
            200, 50, "Reimu Hakurei", 100, 15, 3
        );

        fairy.add(EntityFactory.createFairy(
            150, 380, "Red Fairy", 20
        ));

        fairy.add(EntityFactory.createFairy(
            250, 380, "Blue Fairy", 20, "fairy_idle_blue"
        ));

        boss = EntityFactory.createBoss(
            250, 400, "Rumia", 150
        );

        powerItem = EntityFactory.createItem(
            180, 450, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220, 480, ItemType.POINT
        );

        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        inputHandler = new InputHandler(entities);
        gameHUD = new GameHUD();
    }

    @Override
    public void render() {

        float deltaTime = Gdx.graphics.getDeltaTime();

        player.update(deltaTime);

        for (Fairy f : fairy) {
            f.update(deltaTime);
        }

        boss.update(deltaTime);

        bulletManager.update(deltaTime);

        collisionReferee.resolveCollisions(
            player, entities, bulletManager
        );

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameHUD.renderBackground();

        batch.begin();

        for (GameObject entity : entities) {
            entity.render(batch);
        }

        bulletManager.render(batch);
        gameHUD.render(batch);

        batch.end();

        if (gameHUD.getShapeRenderer() != null) {
            player.renderFocusIndicator(
                gameHUD.getShapeRenderer()
            );
        }
    }

    private void restartGame() {

        System.out.println("[Main] Restarting game...");

        entities.clear();

        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();

        entities.add(player);
        player.resetGame();

        fairy.clear();

        fairy.add(EntityFactory.createFairy(
            150, 380, "Red Fairy", 20
        ));

        fairy.add(EntityFactory.createFairy(
            250, 380, "Blue Fairy", 20, "fairy_idle_blue"
        ));

        boss = EntityFactory.createBoss(
            250, 400, "Rumia", 150
        );

        powerItem = EntityFactory.createItem(
            180, 450, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220, 480, ItemType.POINT
        );

        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
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
