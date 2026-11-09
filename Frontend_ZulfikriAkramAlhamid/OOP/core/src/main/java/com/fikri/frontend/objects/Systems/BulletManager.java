package com.fikri.frontend.objects.Systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fikri.frontend.objects.BulletType;
import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.patterns.bulletStrategy.FantasySealMovement;
import com.fikri.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.fikri.frontend.objects.patterns.shootingStrategy.RingShot;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class BulletManager {
    private final List<Bullet> activePlayerBullets = new ArrayList<>();
    private final List<Bullet> activeEnemyBullets = new ArrayList<>();
    private final Queue<Bullet> playerBulletPool = new ArrayDeque<>();
    private final Queue<Bullet> enemyBulletPool = new ArrayDeque<>();
    private List<GameObject> entities;

    public void update(float delta) {
        update(delta, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    }

    public void update(float delta, float screenWidth, float screenHeight) {
        Iterator<Bullet> playerIterator = activePlayerBullets.iterator();
        while (playerIterator.hasNext()) {
            Bullet bullet = playerIterator.next();
            injectEntities(bullet);
            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight)) bullet.destroy();
            if (bullet.isDestroyed()) {
                playerIterator.remove();
                playerBulletPool.offer(bullet);
            }
        }

        Iterator<Bullet> enemyIterator = activeEnemyBullets.iterator();
        while (enemyIterator.hasNext()) {
            Bullet bullet = enemyIterator.next();
            injectEntities(bullet);
            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight)) bullet.destroy();
            if (bullet.isDestroyed()) {
                enemyIterator.remove();
                enemyBulletPool.offer(bullet);
            }
        }
    }

    private void injectEntities(Bullet bullet) {
        if (bullet == null || entities == null || bullet.getMovementPattern() == null) return;

        if (bullet.getMovementPattern() instanceof HomingBulletMovement homing
                && homing.getEntities() == null) {
            homing.setEntities(entities);
        } else if (bullet.getMovementPattern() instanceof FantasySealMovement fantasy
                && fantasy.getEntities() == null) {
            fantasy.setEntities(entities);
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet bullet : activePlayerBullets) bullet.render(batch);
        for (Bullet bullet : activeEnemyBullets) bullet.render(batch);
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy,
                                    int damage, String spriteKey) {
        String key = spriteKey != null ? spriteKey : "bullet_amulet";
        Bullet bullet;
        if (!playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll();
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage, key);
        }

        bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        bullet.setSprite(AssetManager.getInstance().getTextureRegion(key));
        activePlayerBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        return spawnPlayerBullet(x, y, vx, vy, damage, "bullet_amulet");
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy,
                                   int damage, String spriteKey) {
        String key = spriteKey != null ? spriteKey : "bullet_danmaku";
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage, key);
        }

        bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        bullet.setSprite(AssetManager.getInstance().getTextureRegion(key));
        activeEnemyBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        return spawnEnemyBullet(x, y, vx, vy, damage, "bullet_danmaku");
    }

    public void spawnBombOrbs(float originX, float originY,
                              GameObject targetEnemy, List<GameObject> entities) {
        int orbCount = 8;
        int orbDamage = 35;
        List<GameObject> targetEntities = entities != null ? entities : this.entities;

        RingShot ringShot = new RingShot(
                150f,
                orbCount,
                orbDamage,
                new FantasySealMovement(targetEnemy, targetEntities)
        );
        ringShot.execute(originX, originY, this, true);

        System.out.println("[BulletManager] Spawned 8 Bomb Spirit Orbs via RingShot + FantasySealMovement!");
    }

    public void spawnBombOrbs(float originX, float originY, GameObject targetEnemy) {
        spawnBombOrbs(originX, originY, targetEnemy, null);
    }

    public void clearEnemyBullets(List<GameObject> entitiesList) {
        Iterator<Bullet> iterator = activeEnemyBullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (entitiesList != null) {
                Item pointItem = EntityFactory.createItem(
                        bullet.getX(), bullet.getY(), ItemType.POINT);
                entitiesList.add(pointItem);
            }

            bullet.destroy();
            iterator.remove();
            enemyBulletPool.offer(bullet);
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    public void clearPlayerBullets() {
        Iterator<Bullet> iterator = activePlayerBullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.destroy();
            iterator.remove();
            playerBulletPool.offer(bullet);
        }
    }

    public List<Bullet> getActivePlayerBullets() { return activePlayerBullets; }
    public List<Bullet> getActiveEnemyBullets() { return activeEnemyBullets; }
    public List<GameObject> getEntities() { return entities; }
    public void setEntities(List<GameObject> entities) { this.entities = entities; }
}
