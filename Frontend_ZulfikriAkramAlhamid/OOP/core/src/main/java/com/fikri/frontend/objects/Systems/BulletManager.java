package com.fikri.frontend.objects.Systems;

import com.fikri.frontend.objects.bullets.Bullet;
import java.util.ArrayList;
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

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        return null;
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        return null;
    }

    public List<Bullet> getActivePlayerBullets() { return activePlayerBullets; }
    public List<Bullet> getActiveEnemyBullets() { return activeEnemyBullets; }
    public int getPlayerPoolSize() { return playerBulletPool.size(); }
    public int getEnemyPoolSize() { return enemyBulletPool.size(); }
}
