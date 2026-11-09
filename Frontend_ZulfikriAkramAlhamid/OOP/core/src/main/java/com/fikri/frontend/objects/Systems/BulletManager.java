package com.fikri.frontend.objects.Systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.BulletType;
import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;

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

    // [TAMBAHAN CS] Field untuk menampung daftar entitas arena (dibutuhkan peluru Homing)
    private List<GameObject> entities;

    public void update(float delta) {
        update(delta, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    }

    public void update(float delta, float screenWidth, float screenHeight) {

        Iterator<Bullet> pIter = activePlayerBullets.iterator();

        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();

            // [TAMBAHAN CS] Sisipkan pengecekan homing entities sebelum update
            if (entities != null && bullet.getMovementPattern() instanceof HomingBulletMovement homing && homing.getEntities() == null) {
                homing.setEntities(entities);
            }

            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight)) {
                bullet.destroy();
            }

            if (bullet.isDestroyed()) {
                pIter.remove();
                playerBulletPool.offer(bullet);
            }
        }

        Iterator<Bullet> eIter = activeEnemyBullets.iterator();

        while (eIter.hasNext()) {
            Bullet bullet = eIter.next();
            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight)) {
                bullet.destroy();
            }

            if (bullet.isDestroyed()) {
                eIter.remove();
                enemyBulletPool.offer(bullet);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet bullet : activePlayerBullets) {
            bullet.render(batch);
        }

        for (Bullet bullet : activeEnemyBullets) {
            bullet.render(batch);
        }
    }

    // [UBAH CS] Overload 6 parameter untuk player bullet dengan spriteKey
    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage, String spriteKey) {
        String key = (spriteKey != null) ? spriteKey : "bullet_amulet";
        Bullet bullet;
        if (!playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key));
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage, key);
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        }
        activePlayerBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        return spawnPlayerBullet(x, y, vx, vy, damage, "bullet_amulet");
    }

    // [UBAH CS] Overload 6 parameter untuk enemy bullet dengan spriteKey
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage, String spriteKey) {
        String key = (spriteKey != null) ? spriteKey : "bullet_danmaku";
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key));
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage, key);
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        }
        activeEnemyBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        return spawnEnemyBullet(x, y, vx, vy, damage, "bullet_danmaku");
    }

    public void clearEnemyBullets(List<GameObject> entitiesList) {
        Iterator<Bullet> iterator = activeEnemyBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (entitiesList != null) {
                Item pointItem = EntityFactory.createItem(
                    bullet.getX(),
                    bullet.getY(),
                    ItemType.POINT
                );

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

    public List<Bullet> getActivePlayerBullets() {
        return activePlayerBullets;
    }

    public List<Bullet> getActiveEnemyBullets() {
        return activeEnemyBullets;
    }

    // [TAMBAHAN CS] Getter & Setter untuk entities arena
    public List<GameObject> getEntities() {
        return entities;
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }
}
