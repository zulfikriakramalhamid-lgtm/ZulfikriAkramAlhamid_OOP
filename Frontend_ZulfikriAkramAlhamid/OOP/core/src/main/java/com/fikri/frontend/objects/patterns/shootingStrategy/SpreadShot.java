package com.fikri.frontend.objects.patterns.shootingStrategy;

import com.fikri.frontend.objects.GameObject;
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

    // Master Constructor
    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, int damage, float horizontalSpacing,
                      BulletMovementPattern bulletMovementPattern, GameObject target, String spriteKey) {
        this.speed = speed;
        this.numBullets = numBullets;
        this.spreadAngleDegrees = spreadAngleDegrees;
        this.damage = damage;
        this.horizontalSpacing = horizontalSpacing;
        this.bulletMovementPattern = bulletMovementPattern;
        this.target = target;
        this.spriteKey = spriteKey;
    }

    // Konstruktor Overload 1
    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, int damage, BulletMovementPattern bulletMovementPattern) {
        this(speed, numBullets, spreadAngleDegrees, damage, 0f, bulletMovementPattern, null, null);
    }

    // Konstruktor Overload 2
    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, int damage, GameObject target, String spriteKey) {
        this(speed, numBullets, spreadAngleDegrees, damage, 0f, null, target, spriteKey);
    }

    // Konstruktor Dasar
    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, int damage) {
        this(speed, numBullets, spreadAngleDegrees, damage, 0f, null, null, null);
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        if (bulletManager == null) return;

        float baseAngle = isPlayer ? 90.0f : 270.0f;
        float startAngle = baseAngle - (spreadAngleDegrees / 2f);
        float angleStep = (numBullets > 1) ? (spreadAngleDegrees / (numBullets - 1)) : 0f;

        float startX = (horizontalSpacing > 0f && numBullets > 1) ? (originX - ((numBullets - 1) * horizontalSpacing / 2f)) : originX;

        for (int i = 0; i < numBullets; i++) {
            float angleDeg = startAngle + (i * angleStep);
            float angleRad = (float) Math.toRadians(angleDeg);

            float vx = (float) (speed * Math.cos(angleRad));
            float vy = (float) (speed * Math.sin(angleRad));
            float spawnX = (horizontalSpacing > 0f && numBullets > 1) ? (startX + i * horizontalSpacing) : originX;

            if (isPlayer) {
                bulletManager.spawnPlayerBullet(spawnX, originY, vx, vy, damage);
            } else {
                bulletManager.spawnEnemyBullet(spawnX, originY, vx, vy, damage);
            }
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public int getNumBullets() { return numBullets; }
    public void setNumBullets(int numBullets) { this.numBullets = numBullets; }
    public float getSpreadAngleDegrees() { return spreadAngleDegrees; }
    public void setSpreadAngleDegrees(float spreadAngleDegrees) { this.spreadAngleDegrees = spreadAngleDegrees; }
    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    public float getHorizontalSpacing() { return horizontalSpacing; }
    public void setHorizontalSpacing(float horizontalSpacing) { this.horizontalSpacing = horizontalSpacing; }
    public BulletMovementPattern getBulletMovementPattern() { return bulletMovementPattern; }
    public void setBulletMovementPattern(BulletMovementPattern bulletMovementPattern) { this.bulletMovementPattern = bulletMovementPattern; }
    public String getSpriteKey() { return spriteKey; }
    public void setSpriteKey(String spriteKey) { this.spriteKey = spriteKey; }
}
