package com.fikri.frontend.objects.Systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.BulletType;
import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;

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

    public void update(float delta) {
        update(delta, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    }

    public void update(float delta, float screenWidth, float screenHeight) {

        Iterator<Bullet> pIter = activePlayerBullets.iterator();

        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();
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

    public void spawnPlayerBullet(
        float x, float y, float vx, float vy, int damage
    ) {
        Bullet bullet = playerBulletPool.poll();

        if (bullet == null) {
            bullet = new Bullet(x, y, BulletType.PLAYER_BULLET, damage);
        }

        bullet.init(
            x, y, vx, vy,
            BulletType.PLAYER_BULLET,
            damage,
            true
        );

        activePlayerBullets.add(bullet);
    }

    public void spawnEnemyBullet(
        float x, float y, float vx, float vy, int damage
    ) {
        Bullet bullet = enemyBulletPool.poll();

        if (bullet == null) {
            bullet = new Bullet(x, y, BulletType.ENEMY_BULLET, damage);
        }

        bullet.init(
            x, y, vx, vy,
            BulletType.ENEMY_BULLET,
            damage,
            false
        );

        activeEnemyBullets.add(bullet);
    }

    public void clearEnemyBullets(List<GameObject> entities) {
        Iterator<Bullet> iterator = activeEnemyBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (entities != null) {
                Item pointItem = EntityFactory.createItem(
                    bullet.getX(),
                    bullet.getY(),
                    ItemType.POINT
                );

                entities.add(pointItem);
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
}
