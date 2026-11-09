package com.fikri.frontend.objects.patterns.shootingStrategy;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.patterns.ShootingPattern;
import com.fikri.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;
import com.fikri.frontend.objects.Systems.BulletManager;

public class SpreadShot implements ShootingPattern {
    private float speed;
    private int numBullets;
    private float spreadAngleDegrees;
    private int damage;
    private float horizontalSpacing;
    private BulletMovementPattern bulletMovementPattern;
    private GameObject target;
    private String spriteKey;

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, int damage,
                      float horizontalSpacing, BulletMovementPattern bulletMovementPattern,
                      GameObject target, String spriteKey) {
        this.speed = speed;
        this.numBullets = Math.max(1, numBullets);
        this.spreadAngleDegrees = spreadAngleDegrees;
        this.damage = damage;
        this.horizontalSpacing = Math.max(0f, horizontalSpacing);
        this.bulletMovementPattern = bulletMovementPattern;
        this.target = target;
        this.spriteKey = spriteKey;
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, int damage,
                      BulletMovementPattern bulletMovementPattern) {
        this(speed, numBullets, spreadAngleDegrees, damage, 0f,
             bulletMovementPattern, null, null);
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, int damage,
                      GameObject target, String spriteKey) {
        this(speed, numBullets, spreadAngleDegrees, damage, 0f, null, target, spriteKey);
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, int damage) {
        this(speed, numBullets, spreadAngleDegrees, damage, 0f, null, null, null);
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        if (bulletManager == null) return;

        float baseAngle;
        if (isPlayer || target == null || target.isDestroyed()) {
            baseAngle = isPlayer ? 90f : 270f;
        } else {
            float targetX = target.getX() + target.getWidth() / 2f;
            float targetY = target.getY() + target.getHeight() / 2f;
            baseAngle = (float) Math.toDegrees(
                    Math.atan2(targetY - originY, targetX - originX));
        }

        float startAngle = baseAngle - spreadAngleDegrees / 2f;
        float angleStep = numBullets > 1
                ? spreadAngleDegrees / (numBullets - 1)
                : 0f;

        float startX = horizontalSpacing > 0f && numBullets > 1
                ? originX - ((numBullets - 1) * horizontalSpacing / 2f)
                : originX;

        for (int i = 0; i < numBullets; i++) {
            float angle = startAngle + i * angleStep;
            float radians = (float) Math.toRadians(angle);
            float vx = (float) Math.cos(radians) * speed;
            float vy = (float) Math.sin(radians) * speed;
            float spawnX = horizontalSpacing > 0f && numBullets > 1
                    ? startX + i * horizontalSpacing
                    : originX;

            Bullet bullet = isPlayer
                    ? bulletManager.spawnPlayerBullet(spawnX, originY, vx, vy, damage, spriteKey)
                    : bulletManager.spawnEnemyBullet(spawnX, originY, vx, vy, damage, spriteKey);

            if (bullet != null && bulletMovementPattern != null) {
                bullet.setMovementPattern(bulletMovementPattern);
            }
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public int getNumBullets() { return numBullets; }
    public void setNumBullets(int numBullets) { this.numBullets = Math.max(1, numBullets); }
    public float getSpreadAngleDegrees() { return spreadAngleDegrees; }
    public void setSpreadAngleDegrees(float angle) { this.spreadAngleDegrees = angle; }
    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    public float getHorizontalSpacing() { return horizontalSpacing; }
    public void setHorizontalSpacing(float spacing) { this.horizontalSpacing = Math.max(0f, spacing); }
    public BulletMovementPattern getBulletMovementPattern() { return bulletMovementPattern; }
    public void setBulletMovementPattern(BulletMovementPattern pattern) { this.bulletMovementPattern = pattern; }
    public GameObject getTarget() { return target; }
    public void setTarget(GameObject target) { this.target = target; }
    public String getSpriteKey() { return spriteKey; }
    public void setSpriteKey(String spriteKey) { this.spriteKey = spriteKey; }
}
