package com.fikri.frontend.objects.patterns.shootingStrategy;

import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.patterns.ShootingPattern;
import com.fikri.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;
import com.fikri.frontend.objects.Systems.BulletManager;

public class SideShot implements ShootingPattern {
    private float speed;
    private int damage;
    private float horizontalOffset;
    private float verticalOffset;
    private float leftAngle;
    private float rightAngle;
    private boolean dualSides;
    private BulletMovementPattern bulletMovementPattern;
    private String spriteKey;

    public SideShot(float speed, int damage, float horizontalOffset, float verticalOffset,
                    float leftAngle, float rightAngle, boolean dualSides,
                    BulletMovementPattern bulletMovementPattern, String spriteKey) {
        this.speed = speed;
        this.damage = damage;
        this.horizontalOffset = horizontalOffset;
        this.verticalOffset = verticalOffset;
        this.leftAngle = leftAngle;
        this.rightAngle = rightAngle;
        this.dualSides = dualSides;
        this.bulletMovementPattern = bulletMovementPattern;
        this.spriteKey = spriteKey;
    }

    // Overload yang dipakai pada rank Power awal; sprite homing menggunakan amulet khusus.
    public SideShot(float speed, int damage, float horizontalOffset, float verticalOffset,
                    float leftAngle, float rightAngle, boolean dualSides,
                    BulletMovementPattern bulletMovementPattern) {
        this(speed, damage, horizontalOffset, verticalOffset, leftAngle, rightAngle,
             dualSides, bulletMovementPattern, "bullet_amulet_homing");
    }

    // Constructor kompatibilitas dengan implementasi Modul 8.
    public SideShot(float speed, int damage, float offset,
                    BulletMovementPattern bulletMovementPattern, String spriteKey) {
        this(speed, damage, offset, 0f, 90f, 90f, true,
             bulletMovementPattern, spriteKey);
    }

    public SideShot(float speed, int damage, float offset) {
        this(speed, damage, offset, 0f, 90f, 90f, true, null, null);
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        if (bulletManager == null) return;

        float[] offsets = dualSides
                ? new float[]{-horizontalOffset, horizontalOffset}
                : new float[]{0f};

        for (int i = 0; i < offsets.length; i++) {
            float angleDeg;
            if (isPlayer) {
                angleDeg = (i == 0) ? leftAngle : rightAngle;
                if (!dualSides) angleDeg = (leftAngle + rightAngle) / 2f;
            } else {
                angleDeg = 270f;
            }

            float rad = (float) Math.toRadians(angleDeg);
            float vx = (float) Math.cos(rad) * speed;
            float vy = (float) Math.sin(rad) * speed;
            float spawnX = originX + offsets[i];
            float spawnY = originY + verticalOffset;

            Bullet bullet = isPlayer
                    ? bulletManager.spawnPlayerBullet(spawnX, spawnY, vx, vy, damage, spriteKey)
                    : bulletManager.spawnEnemyBullet(spawnX, spawnY, vx, vy, damage, spriteKey);

            if (bullet != null && bulletMovementPattern != null) {
                bullet.setMovementPattern(bulletMovementPattern);
            }
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    public float getOffset() { return horizontalOffset; }
    public void setOffset(float offset) { this.horizontalOffset = offset; }
    public float getHorizontalOffset() { return horizontalOffset; }
    public void setHorizontalOffset(float value) { this.horizontalOffset = value; }
    public float getVerticalOffset() { return verticalOffset; }
    public void setVerticalOffset(float value) { this.verticalOffset = value; }
    public float getLeftAngle() { return leftAngle; }
    public void setLeftAngle(float value) { this.leftAngle = value; }
    public float getRightAngle() { return rightAngle; }
    public void setRightAngle(float value) { this.rightAngle = value; }
    public boolean isDualSides() { return dualSides; }
    public void setDualSides(boolean dualSides) { this.dualSides = dualSides; }
    public BulletMovementPattern getBulletMovementPattern() { return bulletMovementPattern; }
    public void setBulletMovementPattern(BulletMovementPattern pattern) { this.bulletMovementPattern = pattern; }
    public String getSpriteKey() { return spriteKey; }
    public void setSpriteKey(String spriteKey) { this.spriteKey = spriteKey; }
}
