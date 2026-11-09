package com.fikri.frontend.objects.patterns.shootingStrategy;

import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.patterns.ShootingPattern;
import com.fikri.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;
import com.fikri.frontend.objects.Systems.BulletManager;

public class SideShot implements ShootingPattern {
    private float speed;
    private int damage;
    private float offset;
    private BulletMovementPattern bulletMovementPattern;
    private String spriteKey;

    public SideShot(float speed, int damage, float offset, BulletMovementPattern bulletMovementPattern, String spriteKey) {
        this.speed = speed;
        this.damage = damage;
        this.offset = offset;
        this.bulletMovementPattern = bulletMovementPattern;
        this.spriteKey = spriteKey;
    }

    public SideShot(float speed, int damage, float offset) {
        this(speed, damage, offset, null, null);
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        if (bulletManager == null) return;

        float[] xOffsets = {-offset, offset};
        float angleDeg = isPlayer ? 90.0f : 270.0f;
        float rad = (float) Math.toRadians(angleDeg);
        float vx = (float) (speed * Math.cos(rad));
        float vy = (float) (speed * Math.sin(rad));

        for (float ox : xOffsets) {
            float spawnX = originX + ox;
            if (isPlayer) {
                bulletManager.spawnPlayerBullet(spawnX, originY, vx, vy, damage);
            } else {
                bulletManager.spawnEnemyBullet(spawnX, originY, vx, vy, damage);
            }
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    public float getOffset() { return offset; }
    public void setOffset(float offset) { this.offset = offset; }
    public BulletMovementPattern getBulletMovementPattern() { return bulletMovementPattern; }
    public void setBulletMovementPattern(BulletMovementPattern bulletMovementPattern) { this.bulletMovementPattern = bulletMovementPattern; }
    public String getSpriteKey() { return spriteKey; }
    public void setSpriteKey(String spriteKey) { this.spriteKey = spriteKey; }
}
