
package com.fikri.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.fikri.frontend.commands.Command;
import com.fikri.frontend.commands.InputHandler;
import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Systems.AssetManager;
import com.fikri.frontend.objects.Systems.AudioManager;
import com.fikri.frontend.objects.Systems.BulletManager;
import com.fikri.frontend.objects.Systems.CollisionReferee;
import com.fikri.frontend.objects.Systems.EntityFactory;
import com.fikri.frontend.objects.patterns.entityStrategy.SCurveMovement;
import com.fikri.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.fikri.frontend.objects.patterns.shootingStrategy.AlternatingRingShot;
import com.fikri.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.fikri.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.fikri.frontend.ui.GameHUD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Player player;
    private Boss boss;

    private List<Fairy> fairy;
    private List<GameObject> entities;

    private Item powerItem;
    private Item pointItem;

    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;
    private InputHandler inputHandler;
    private GameHUD gameHUD;

    private float enemyShootTimer = 0f;
    private float phaseTimer = 0f;
    private int currentPhase = 0;

    private float waveTimer = 0f;
    private int wave1SpawnIndex = 0;

    private final float[] fairySpawnX = {
        60f, 130f, 200f, 270f, 340f
    };

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

        player = EntityFactory.createPlayer(
            200f, 50f, "Reimu Hakurei", 100, 15, 3
        );
        player.registerObserver(gameHUD);

        createStageEntities();

        inputHandler.setEntities(entities);
        bulletManager.setEntities(entities);
    }

    private void createStageEntities() {
        boss = EntityFactory.createBoss(200f, 420f, "Rumia", 150);

        powerItem = EntityFactory.createItem(
            180f, 450f, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220f, 480f, ItemType.POINT
        );

        entities.add(player);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        boss.setMovementPattern(
            new TargetPointMovement(200f, 420f, 150f)
        );

        boss.setShootingPattern(
            new SpreadShot(
                220f, 5, 25f, 10,
                player, "bullet_danmaku_red"
            )
        );
    }

    private void spawnFairy(int index) {
        float x = fairySpawnX[index];

        Fairy newFairy = EntityFactory.createFairy(
            x, 500f, "S-Curve Fairy", 8, "fairy_idle_blue"
        );

        boolean mirrored = index % 2 != 0;

        newFairy.setMovementPattern(
            new SCurveMovement(
                85f,
                (float) Math.toRadians(270),
                mirrored
            )
        );

        newFairy.setShootingPattern(
            new FrontShot(150f, 10)
        );

        fairy.add(newFairy);
        entities.add(newFairy);
    }

    private void updateFairyWave(float delta) {
        waveTimer += delta;

        while (wave1SpawnIndex < fairySpawnX.length) {
            float spawnTime = 0.5f + wave1SpawnIndex * 0.8f;

            if (waveTimer < spawnTime) {
                break;
            }

            spawnFairy(wave1SpawnIndex);
            wave1SpawnIndex++;
        }

        fairy.removeIf(f ->
            f == null || f.isDestroyed() || f.isOffScreen(416f, 560f)
        );
    }

    private void resetFairyWave() {
        waveTimer = 0f;
        wave1SpawnIndex = 0;
        fairy.clear();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Tekan R kapan saja untuk mengulang permainan.
        if (Gdx.input != null &&
            Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            restartGame();
            return;
        }

        if (player == null) {
            return;
        }

        List<Command> commands = inputHandler.handleInput(delta);

        for (Command command : commands) {
            command.execute(player, bulletManager);
        }

        updateEnemyScheduler(delta);

        updateAndClean(entities, delta, 416f, 560f);

        bulletManager.update(delta, 416f, 560f);

        collisionReferee.resolveCollisions(
            player, entities, bulletManager
        );

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameHUD.renderBackground();

        batch.begin();

        for (GameObject entity : entities) {
            if (entity != null && !entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);
        gameHUD.render(batch);

        batch.end();

        player.renderFocusIndicator(gameHUD.getShapeRenderer());
    }

    private void updateEnemyScheduler(float delta) {
        updateFairyWave(delta);

        // Boss menembak setiap 1,5 detik.
        enemyShootTimer += delta;

        if (enemyShootTimer >= 1.5f &&
            boss != null && !boss.isDestroyed()) {

            boss.shootBullet(bulletManager);
            enemyShootTimer = 0f;
        }

        // Fairy menembak sesuai jadwal masing-masing.
        for (Fairy f : fairy) {
            if (f != null && !f.isDestroyed() && f.canShoot(delta)) {
                f.shootBullet(bulletManager);
            }
        }

        // Pergantian strategi Boss setiap 4 detik.
        phaseTimer += delta;

        if (phaseTimer >= 4f &&
            boss != null && !boss.isDestroyed()) {

            phaseTimer = 0f;
            currentPhase = (currentPhase + 1) % 2;

            if (currentPhase == 0) {
                boss.setMovementPattern(
                    new TargetPointMovement(120f, 420f, 150f)
                );

                boss.setShootingPattern(
                    new SpreadShot(
                        220f, 5, 25f, 10,
                        player, "bullet_danmaku_red"
                    )
                );

            } else {
                boss.setMovementPattern(
                    new TargetPointMovement(280f, 420f, 150f)
                );

                boss.setShootingPattern(
                    new AlternatingRingShot(180f, 16, 10)
                );
            }
        }
    }

    private void updateAndClean(
        List<GameObject> objects,
        float delta,
        float width,
        float height
    ) {
        Iterator<GameObject> iterator = objects.iterator();

        while (iterator.hasNext()) {
            GameObject entity = iterator.next();

            if (entity == null) {
                iterator.remove();
                continue;
            }

            entity.update(delta);

            if (entity != player &&
                (entity.isDestroyed() || entity.isOffScreen(width, height))) {
                iterator.remove();
            }
        }
    }

    private void restartGame() {
        // Bersihkan semua objek lama.
        entities.clear();

        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();

        // Reset status permainan.
        enemyShootTimer = 0f;
        phaseTimer = 0f;
        currentPhase = 0;

        resetFairyWave();

        // Reset Player.
        player.resetGame();
        player.setFocused(false);

        // Buat ulang Boss dan item.
        createStageEntities();

        // Perbarui referensi list pada sistem lain.
        inputHandler.setEntities(entities);
        bulletManager.setEntities(entities);

        System.out.println("Game berhasil di-restart!");
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
