
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
import com.fikri.frontend.objects.enemies.Enemy;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Systems.AssetManager;
import com.fikri.frontend.objects.Systems.AudioManager;
import com.fikri.frontend.objects.Systems.BulletManager;
import com.fikri.frontend.objects.Systems.CollisionReferee;
import com.fikri.frontend.objects.Systems.EntityFactory;

import com.fikri.frontend.objects.patterns.entityStrategy.LinearEntityMovement;
import com.fikri.frontend.objects.patterns.entityStrategy.SCurveMovement;
import com.fikri.frontend.objects.patterns.entityStrategy.TargetPointMovement;

import com.fikri.frontend.objects.patterns.shootingStrategy.AlternatingRingShot;
import com.fikri.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.fikri.frontend.objects.patterns.shootingStrategy.RingShot;
import com.fikri.frontend.objects.patterns.shootingStrategy.SpreadShot;

import com.fikri.frontend.ui.GameHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;

    private Player player;
    private Boss boss;

    private List<Fairy> fairy;
    private List<Enemy> normalEnemies;
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

    private float normalWaveTimer = 0f;
    private int normalSpawnIndex = 0;

    private float bossRespawnTimer = 0f;
    private boolean bossRespawnPending = false;
    private int bossCount = 0;

    private final Map<Enemy, Float> normalEnemyShootTimers =
        new HashMap<>();

    private final float[] fairySpawnX = {
        60f, 130f, 200f, 270f, 340f
    };

    private final float[] normalEnemySpawnX = {
        80f, 192f, 304f
    };

    private final float[] normalEnemyHorizontalSpeed = {
        -15f, 0f, 15f
    };

    private final float[] normalEnemyShootInterval = {
        0.85f, 1.15f, 1.35f
    };

    @Override
    public void create() {
        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        normalEnemies = new ArrayList<>();
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
        spawnBoss();

        powerItem = EntityFactory.createItem(
            180f, 450f, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220f, 480f, ItemType.POINT
        );

        entities.add(player);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    private void spawnBoss() {
        boss = EntityFactory.createBoss(
            200f, 420f, "Rumia", 150
        );

        boss.setMovementPattern(
            new TargetPointMovement(200f, 420f, 150f)
        );

        boss.setShootingPattern(
            new SpreadShot(
                220f, 5, 25f, 10,
                player, "bullet_danmaku_red"
            )
        );

        entities.add(boss);

        bossCount++;
        bossRespawnTimer = 0f;
        bossRespawnPending = false;
        enemyShootTimer = 0f;
        phaseTimer = 0f;
        currentPhase = 0;

        System.out.println("Boss ke-" + bossCount + " muncul!");
    }

    private void updateBossRespawn(float delta) {
        if (boss != null && boss.isDestroyed() &&
            !bossRespawnPending) {

            bossRespawnPending = true;
            bossRespawnTimer = 0f;
        }

        if (bossRespawnPending) {
            bossRespawnTimer += delta;

            if (bossRespawnTimer >= 5f) {
                spawnBoss();
            }
        }
    }

    private void spawnFairy(int index) {
        Fairy f = EntityFactory.createFairy(
            fairySpawnX[index],
            500f,
            "S-Curve Fairy",
            8,
            "fairy_idle_blue"
        );

        boolean mirrored = index % 2 != 0;

        f.setMovementPattern(
            new SCurveMovement(
                85f,
                (float) Math.toRadians(270),
                mirrored
            )
        );

        switch (index % 3) {
            case 0:
                f.setShootingPattern(
                    new FrontShot(165f, 7)
                );
                break;

            case 1:
                f.setShootingPattern(
                    new SpreadShot(145f, 3, 35f, 7)
                );
                break;

            default:
                f.setShootingPattern(
                    new RingShot(105f, 8, 7)
                );
                break;
        }

        f.setShootTiming(0.25f, 0.9f, 8);

        fairy.add(f);
        entities.add(f);
    }

    private void updateFairyWave(float delta) {
        waveTimer += delta;

        while (wave1SpawnIndex < fairySpawnX.length) {
            float spawnTime = 0.35f + wave1SpawnIndex * 0.4f;

            if (waveTimer < spawnTime) {
                break;
            }

            spawnFairy(wave1SpawnIndex);
            wave1SpawnIndex++;
        }

        if (wave1SpawnIndex >= fairySpawnX.length &&
            waveTimer >= 6f) {

            waveTimer = 0f;
            wave1SpawnIndex = 0;
        }

        fairy.removeIf(f ->
            f == null ||
                f.isDestroyed() ||
                f.isOffScreen(416f, 560f)
        );
    }

    private void spawnNormalEnemy(int index) {
        Enemy enemy = EntityFactory.createEnemy(
            normalEnemySpawnX[index],
            520f,
            "Normal Enemy",
            25
        );

        enemy.setMovementPattern(
            new LinearEntityMovement(
                normalEnemyHorizontalSpeed[index],
                -45f
            )
        );

        switch (index % 3) {
            case 0:
                enemy.setShootingPattern(
                    new FrontShot(180f, 8)
                );
                break;

            case 1:
                enemy.setShootingPattern(
                    new SpreadShot(155f, 3, 35f, 8)
                );
                break;

            default:
                enemy.setShootingPattern(
                    new RingShot(115f, 8, 8)
                );
                break;
        }

        normalEnemies.add(enemy);
        normalEnemyShootTimers.put(enemy, 0f);
        entities.add(enemy);
    }

    private void updateNormalEnemyWave(float delta) {
        normalWaveTimer += delta;

        while (normalSpawnIndex < normalEnemySpawnX.length) {
            float spawnTime = 0.35f + normalSpawnIndex * 0.5f;

            if (normalWaveTimer < spawnTime) {
                break;
            }

            spawnNormalEnemy(normalSpawnIndex);
            normalSpawnIndex++;
        }

        if (normalSpawnIndex >= normalEnemySpawnX.length &&
            normalWaveTimer >= 4.8f) {

            normalWaveTimer = 0f;
            normalSpawnIndex = 0;
        }

        normalEnemies.removeIf(enemy ->
            enemy == null ||
                enemy.isDestroyed() ||
                enemy.isOffScreen(416f, 560f)
        );

        normalEnemyShootTimers.keySet().removeIf(enemy ->
            enemy == null ||
                enemy.isDestroyed() ||
                enemy.isOffScreen(416f, 560f)
        );
    }

    private void updateNormalEnemyShooting(float delta) {
        for (Enemy enemy : normalEnemies) {
            if (enemy == null || enemy.isDestroyed()) {
                continue;
            }

            float timer =
                normalEnemyShootTimers.getOrDefault(enemy, 0f);

            timer += delta;

            int index = normalEnemies.indexOf(enemy);
            float interval =
                normalEnemyShootInterval[Math.max(0, index) % 3];

            if (timer >= interval) {
                enemy.shootBullet(bulletManager);
                timer = 0f;
            }

            normalEnemyShootTimers.put(enemy, timer);
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input != null &&
            Gdx.input.isKeyJustPressed(Input.Keys.R)) {

            restartGame();
            return;
        }

        if (player == null) {
            return;
        }

        List<Command> commands =
            inputHandler.handleInput(delta);

        for (Command command : commands) {
            command.execute(player, bulletManager);
        }

        updateEnemyScheduler(delta);

        updateAndClean(entities, delta, 416f, 560f);

        bulletManager.update(delta, 416f, 560f);

        collisionReferee.resolveCollisions(
            player, entities, bulletManager
        );

        Gdx.gl.glClearColor(
            0.1f, 0.1f, 0.15f, 1f
        );

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

        player.renderFocusIndicator(
            gameHUD.getShapeRenderer()
        );
    }

    private void updateEnemyScheduler(float delta) {
        updateBossRespawn(delta);

        updateFairyWave(delta);

        updateNormalEnemyWave(delta);
        updateNormalEnemyShooting(delta);

        enemyShootTimer += delta;

        if (enemyShootTimer >= 1.2f &&
            boss != null &&
            !boss.isDestroyed() &&
            !bossRespawnPending) {

            boss.shootBullet(bulletManager);
            enemyShootTimer = 0f;
        }

        for (Fairy f : fairy) {
            if (f != null &&
                !f.isDestroyed() &&
                f.canShoot(delta)) {

                f.shootBullet(bulletManager);
            }
        }

        if (boss != null &&
            !boss.isDestroyed() &&
            !bossRespawnPending) {

            phaseTimer += delta;

            if (phaseTimer >= 3.5f) {
                phaseTimer = 0f;
                currentPhase = (currentPhase + 1) % 2;

                if (currentPhase == 0) {
                    boss.setMovementPattern(
                        new TargetPointMovement(
                            120f, 420f, 150f
                        )
                    );

                    boss.setShootingPattern(
                        new SpreadShot(
                            220f, 5, 35f, 10,
                            player, "bullet_danmaku_red"
                        )
                    );

                } else {
                    boss.setMovementPattern(
                        new TargetPointMovement(
                            280f, 420f, 150f
                        )
                    );

                    boss.setShootingPattern(
                        new AlternatingRingShot(
                            180f, 16, 10
                        )
                    );
                }
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
                (entity.isDestroyed() ||
                    entity.isOffScreen(width, height))) {

                iterator.remove();
            }
        }
    }

    private void restartGame() {
        entities.clear();

        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();

        enemyShootTimer = 0f;
        phaseTimer = 0f;
        currentPhase = 0;

        waveTimer = 0f;
        wave1SpawnIndex = 0;

        normalWaveTimer = 0f;
        normalSpawnIndex = 0;

        bossRespawnTimer = 0f;
        bossRespawnPending = false;
        bossCount = 0;

        fairy.clear();
        normalEnemies.clear();
        normalEnemyShootTimers.clear();

        player.resetGame();
        player.setFocused(false);

        createStageEntities();

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
