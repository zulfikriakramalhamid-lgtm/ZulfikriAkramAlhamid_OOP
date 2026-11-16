
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
    private float horizontalSpacing = 0f;
    private int layers = 1;
    private float minSpeed;
    private float angleOffset = 0f;
    private BulletMovementPattern bulletMovementPattern;
    private GameObject target;
    private String spriteKey;

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees,
                      int damage, float horizontalSpacing,
                      BulletMovementPattern bulletMovementPattern,
                      GameObject target, String spriteKey) {
        this.speed = speed;
        this.numBullets = numBullets;
        this.spreadAngleDegrees = spreadAngleDegrees;
        this.damage = damage;
        this.horizontalSpacing = horizontalSpacing;
        this.bulletMovementPattern = bulletMovementPattern;
        this.target = target;
        this.spriteKey = spriteKey;
        this.minSpeed = speed;
    }

    public SpreadShot withLayers(int layers, float minSpeed) {
        this.layers = Math.max(1, layers);
        this.minSpeed = minSpeed;
        return this;
    }

    public SpreadShot withAngleOffset(float angleOffsetDegrees) {
        this.angleOffset = angleOffsetDegrees;
        return this;
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees) {
        this(speed, numBullets, spreadAngleDegrees, 15, 0f, null, null, null);
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees,
                      int damage) {
        this(speed, numBullets, spreadAngleDegrees, damage, 0f, null, null, null);
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees,
                      int damage, BulletMovementPattern bulletMovementPattern) {
        this(speed, numBullets, spreadAngleDegrees, damage, 0f,
            bulletMovementPattern, null, null);
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees,
                      int damage, GameObject target, String spriteKey) {
        this(speed, numBullets, spreadAngleDegrees, damage, 0f,
            null, target, spriteKey);
    }

    @Override
    public void execute(float originX, float originY,
                        BulletManager bulletManager, boolean isPlayer) {
        if (bulletManager == null) return;

        float centerAngle;

        if (target != null) {
            float targetCenterX = target.getX() + target.getWidth() / 2f;
            float targetCenterY = target.getY() + target.getHeight() / 2f;

            centerAngle = (float) Math.toDegrees(
                Math.atan2(targetCenterY - originY, targetCenterX - originX)
            );
        } else {
            centerAngle = isPlayer ? 90f : 270f;
        }

        centerAngle += angleOffset;

        float startAngle = centerAngle - (spreadAngleDegrees / 2f);
        float angleStep = (numBullets > 1)
            ? spreadAngleDegrees / (numBullets - 1)
            : 0f;

        float startX = (horizontalSpacing > 0f && numBullets > 1)
            ? originX - ((numBullets - 1) * horizontalSpacing / 2f)
            : originX;

        for (int layer = 0; layer < layers; layer++) {
            float layerSpeed = (layers > 1)
                ? speed - (speed - minSpeed) * layer / (layers - 1)
                : speed;

            for (int i = 0; i < numBullets; i++) {
                float angleDeg = startAngle + (i * angleStep);
                float angleRad = (float) Math.toRadians(angleDeg);

                float vx = (float) (layerSpeed * Math.cos(angleRad));
                float vy = (float) (layerSpeed * Math.sin(angleRad));

                float spawnX = (horizontalSpacing > 0f && numBullets > 1)
                    ? startX + i * horizontalSpacing
                    : originX;

                Bullet bullet;

                if (isPlayer) {
                    bullet = bulletManager.spawnPlayerBullet(
                        spawnX, originY, vx, vy, damage, spriteKey
                    );
                } else {
                    bullet = (spriteKey != null)
                        ? bulletManager.spawnEnemyBullet(
                        spawnX, originY, vx, vy, damage, spriteKey
                    )
                        : bulletManager.spawnEnemyBullet(
                        spawnX, originY, vx, vy, damage
                    );
                }

                if (bullet != null) {
                    bullet.setAngle(angleDeg);
                    bullet.setSpeed(layerSpeed);

                    if (bulletMovementPattern != null) {
                        bullet.setMovementPattern(bulletMovementPattern);
                    }
                }
            }
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public int getNumBullets() { return numBullets; }
    public void setNumBullets(int numBullets) { this.numBullets = numBullets; }

    public float getSpreadAngleDegrees() { return spreadAngleDegrees; }
    public void setSpreadAngleDegrees(float angle) {
        this.spreadAngleDegrees = angle;
    }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }

    public float getHorizontalSpacing() { return horizontalSpacing; }
    public void setHorizontalSpacing(float spacing) {
        this.horizontalSpacing = spacing;
    }

    public int getLayers() { return layers; }
    public float getMinSpeed() { return minSpeed; }
    public float getAngleOffset() { return angleOffset; }

    public BulletMovementPattern getBulletMovementPattern() {
        return bulletMovementPattern;
    }

    public void setBulletMovementPattern(BulletMovementPattern pattern) {
        this.bulletMovementPattern = pattern;
    }

    public GameObject getTarget() { return target; }
    public void setTarget(GameObject target) { this.target = target; }

    public String getSpriteKey() { return spriteKey; }
    public void setSpriteKey(String spriteKey) { this.spriteKey = spriteKey; }
}
