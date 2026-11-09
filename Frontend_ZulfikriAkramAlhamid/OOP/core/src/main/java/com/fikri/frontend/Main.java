package com.fikri.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.fikri.frontend.commands.Command;
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
import com.fikri.frontend.objects.patterns.shootingStrategy.AlternatingRingShot;
import com.fikri.frontend.ui.GameHUD;

import java.util.ArrayList;
import java.util.Iterator;
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

    private float enemyShootTimer = 0f;

    @Override
    public void create() {
        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        bulletManager = new BulletManager();
        collisionReferee = new CollisionReferee();

        inputHandler = new InputHandler();
        gameHUD = new GameHUD();

        AssetManager.getInstance().init();
        AudioManager.getInstance().init();
        AudioManager.getInstance().playBgm();

        player = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 15, 3);
        player.registerObserver(gameHUD);

        fairy.add(EntityFactory.createFairy(150, 380, "Red Fairy", 20));
        fairy.add(EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue"));

        boss = EntityFactory.createBoss(250, 400, "Rumia", 150);

        powerItem = EntityFactory.createItem(180, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(220, 480, ItemType.POINT);

        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        inputHandler.setEntities(entities);
        bulletManager.setEntities(entities);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input != null && Gdx.input.isKeyJustPressed(Input.Keys.R) && player != null && player.isGameOver()) {
            restartGame();
        }

        List<Command> commands = inputHandler.handleInput(delta);
        for (Command command : commands) {
            command.execute(player, bulletManager);
        }

        updateEnemyScheduler(delta);

        // TODO (Part IV): Boss Phase Swapping berdasarkan sisa HP
        if (boss != null && !boss.isDestroyed()) {
            if (boss.getHp() < 75 && !(boss.getShootingPattern() instanceof AlternatingRingShot)) {
                boss.setShootingPattern(new AlternatingRingShot(180f, 8, 15, 3, 30f));
            }
        }

        updateAndClean(entities, delta, 416, 560);
        bulletManager.update(delta, 416, 560);

        collisionReferee.resolveCollisions(player, entities, bulletManager);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameHUD.renderBackground();

        batch.begin();
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }
        bulletManager.render(batch);
        gameHUD.render(batch);
        batch.end();

        player.renderFocusIndicator(gameHUD.getShapeRenderer());
    }

    private void updateEnemyScheduler(float delta) {
        enemyShootTimer += delta;
        if (enemyShootTimer >= 1f) {
            if (boss != null && !boss.isDestroyed()) {
                boss.shootBullet(bulletManager);
            }
            enemyShootTimer = 0f;
        }
    }

    private void updateAndClean(List<GameObject> objects, float delta, float width, float height) {
        Iterator<GameObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            GameObject entity = iterator.next();
            entity.update(delta);
            if (entity != player && (entity.isDestroyed() || entity.isOffScreen(width, height))) {
                iterator.remove();
            }
        }
    }

    private void restartGame() {
        entities.clear();
        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();
        enemyShootTimer = 0f;

        player.resetGame();
        entities.add(player);

        fairy.clear();
        fairy.add(EntityFactory.createFairy(150, 380, "Red Fairy", 20));
        fairy.add(EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue"));

        boss = EntityFactory.createBoss(250, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(180, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(220, 480, ItemType.POINT);

        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        inputHandler.setEntities(entities);
        bulletManager.setEntities(entities);
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (gameHUD != null) gameHUD.dispose();
        AssetManager.getInstance().dispose();
        AudioManager.getInstance().dispose();
    }
}
