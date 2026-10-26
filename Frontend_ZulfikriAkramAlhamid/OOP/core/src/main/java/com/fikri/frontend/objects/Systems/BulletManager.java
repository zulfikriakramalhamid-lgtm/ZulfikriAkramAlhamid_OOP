package com.fikri.frontend.objects.Systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fikri.frontend.objects.BulletType;
import com.fikri.frontend.objects.bullets.Bullet;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BulletManager {
    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;
    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;

    public BulletManager() {
        this.playerBulletPool = new LinkedList<>();
        this.enemyBulletPool = new LinkedList<>();
        this.activePlayerBullets = new ArrayList<>();
        this.activeEnemyBullets = new ArrayList<>();
    }

    // Peminjaman bullet Pemain dari Pool (5 poin)
    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        // TODO:
        // 1. Cek apakah playerBulletPool tidak kosong.
        if(!playerBulletPool.isEmpty()){
        bullet = playerBulletPool.poll();
        bullet.init(x,y,vx,vy,BulletType.AMULET,damage, true);
        }
        else{
            bullet = EntityFactory.createPlayerBullet(x,y, damage, "Bulllet_Amulet");
            bullet.init(x,y,vx,vy,BulletType.AMULET,damage, true);
        }
        activePlayerBullets.add(bullet);
        return bullet;
        //    Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.
        // 2. Re-initialize bullet.
        // 3. Tambahkan bullet ke dalam daftar activePlayerBullets.
        // 4. Kembalikan instance bullet tersebut.
    }

    // Peminjaman bullet Musuh dari Pool (5 poin)
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        // TODO:
        // 1. Cek apakah playerBulletPool tidak kosong.
        if(!enemyBulletPool.isEmpty()){
            bullet = enemyBulletPool.poll();
            bullet.init(x,y,vx,vy,BulletType.DANMAKU,damage, false);
        }
        else{
            bullet = EntityFactory.createPlayerBullet(x,y, damage, "Bulllet_Amulet");
            bullet.init(x,y,vx,vy,BulletType.DANMAKU,damage, false);
        }
        activeEnemyBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage);
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        }
        activeEnemyBullets.add(bullet);
        return bullet;
    }

    public void update(float delta, float screenWidth, float screenHeight) {
        Iterator<Bullet> playerIterator = activePlayerBullets.iterator();
        while (playerIterator.hasNext()) {
            Bullet bullet = playerIterator.next();
            bullet.update(delta);
            if (bullet.isOffScreen(screenWidth, screenHeight) || bullet.isDestroyed()) {
                bullet.destroy();
                playerIterator.remove();
                playerBulletPool.offer(bullet);
            }
        }

        Iterator<Bullet> enemyIterator = activeEnemyBullets.iterator();
        while (enemyIterator.hasNext()) {
            Bullet bullet = enemyIterator.next();
            bullet.update(delta);
            if (bullet.isOffScreen(screenWidth, screenHeight) || bullet.isDestroyed()) {
                bullet.destroy();
                enemyIterator.remove();
                enemyBulletPool.offer(bullet);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet b : activePlayerBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
        for (Bullet b : activeEnemyBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
    }

    public List<Bullet> getActivePlayerBullets() { return activePlayerBullets; }
    public List<Bullet> getActiveEnemyBullets() { return activeEnemyBullets; }
    public int getPlayerPoolSize() { return playerBulletPool.size(); }
    public int getEnemyPoolSize() { return enemyBulletPool.size(); }
}
