package com.fikri.frontend.objects.patterns.bulletStrategy;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.enemies.Enemy;

import java.util.List;

public class HomingBulletMovement implements BulletMovementPattern {
    private GameObject target;
    private List<GameObject> entities;
    private float turnSpeed;
    private float homingDuration = 2.0f;

    public HomingBulletMovement(GameObject target, float turnSpeed) {
        this(target, null, turnSpeed);
    }

    public HomingBulletMovement(GameObject target, List<GameObject> entities, float turnSpeed) {
        this.target = target;
        this.entities = entities;
        this.turnSpeed = Math.max(0f, turnSpeed);
    }

    @Override
    public void move(Bullet bullet, float delta) {
        if (bullet == null || delta <= 0f) return;

        // Homing dibatasi durasinya; setelah window selesai peluru lanjut lurus.
        boolean homingActive = bullet.getStateTime() <= homingDuration;
        if (homingActive && (target == null || target.isDestroyed())) {
            target = findNearestEnemy(bullet);
        }

        // Jika belum ada musuh aktif, peluru tetap bergerak mengikuti arah terakhirnya.
        if (homingActive && target != null && !target.isDestroyed()) {
            float targetX = target.getX() + target.getWidth() / 2f;
            float targetY = target.getY() + target.getHeight() / 2f;

            float targetAngle = (float) Math.toDegrees(
                    Math.atan2(targetY - bullet.getY(), targetX - bullet.getX()));

            float currentAngle = bullet.getAngle();
            float difference = (targetAngle - currentAngle) % 360f;
            if (difference > 180f) difference -= 360f;
            if (difference < -180f) difference += 360f;

            float maxTurn = turnSpeed * delta;
            float turn = Math.signum(difference) * Math.min(Math.abs(difference), maxTurn);
            bullet.setAngle(currentAngle + turn);
        }

        float rad = (float) Math.toRadians(bullet.getAngle());
        float bulletSpeed = bullet.getSpeed();
        bullet.setX(bullet.getX() + (float) Math.cos(rad) * bulletSpeed * delta);
        bullet.setY(bullet.getY() + (float) Math.sin(rad) * bulletSpeed * delta);
    }

    private GameObject findNearestEnemy(Bullet bullet) {
        if (entities == null) return null;

        GameObject nearest = null;
        float nearestDistance = Float.MAX_VALUE;

        for (GameObject entity : entities) {
            if (entity instanceof Enemy && !entity.isDestroyed()) {
                float dx = entity.getX() + entity.getWidth() / 2f - bullet.getX();
                float dy = entity.getY() + entity.getHeight() / 2f - bullet.getY();
                float distanceSquared = dx * dx + dy * dy;

                if (distanceSquared < nearestDistance) {
                    nearestDistance = distanceSquared;
                    nearest = entity;
                }
            }
        }
        return nearest;
    }

    public GameObject getTarget() { return target; }
    public void setTarget(GameObject target) { this.target = target; }
    public List<GameObject> getEntities() { return entities; }
    public void setEntities(List<GameObject> entities) { this.entities = entities; }
    public float getTurnSpeed() { return turnSpeed; }
    public void setTurnSpeed(float turnSpeed) { this.turnSpeed = Math.max(0f, turnSpeed); }
    public float getHomingDuration() { return homingDuration; }
    public void setHomingDuration(float homingDuration) { this.homingDuration = Math.max(0f, homingDuration); }
}
