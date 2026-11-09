package com.fikri.frontend.objects.patterns.bulletStrategy;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.enemies.Enemy;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class FantasySealMovement implements BulletMovementPattern {
    private GameObject target;
    private List<GameObject> entities;
    private float homingSpeed;
    private float turnSpeed;
    private float driftDuration;

    // Satu strategi dipakai bersama oleh delapan orb. Timer harus dipisah per peluru.
    private final Map<Bullet, Float> timers = new WeakHashMap<>();

    public FantasySealMovement(GameObject target, List<GameObject> entities,
                               float homingSpeed, float turnSpeed, float driftDuration) {
        this.target = target;
        this.entities = entities;
        this.homingSpeed = homingSpeed;
        this.turnSpeed = turnSpeed;
        this.driftDuration = driftDuration;
    }

    public FantasySealMovement(GameObject target, List<GameObject> entities) {
        this(target, entities, 400f, 300f, 0.4f);
    }

    public FantasySealMovement(GameObject target) {
        this(target, null, 400f, 300f, 0.4f);
    }

    @Override
    public void move(Bullet bullet, float delta) {
        if (bullet == null) return;

        float timer = timers.getOrDefault(bullet, 0f) + delta;
        timers.put(bullet, timer);

        if (timer < driftDuration) {
            bullet.setX(bullet.getX() + bullet.getVx() * delta);
            bullet.setY(bullet.getY() + bullet.getVy() * delta);
            return;
        }

        if (target == null || target.isDestroyed()) {
            target = findNearestEnemy(bullet);
        }

        if (target != null && !target.isDestroyed()) {
            float targetAngle = (float) Math.toDegrees(Math.atan2(
                    target.getY() + target.getHeight() / 2f - bullet.getY(),
                    target.getX() + target.getWidth() / 2f - bullet.getX()));

            float difference = (targetAngle - bullet.getAngle()) % 360f;
            if (difference > 180f) difference -= 360f;
            if (difference < -180f) difference += 360f;

            float turn = Math.signum(difference)
                    * Math.min(Math.abs(difference), turnSpeed * delta);
            bullet.setAngle(bullet.getAngle() + turn);
        }

        float radians = (float) Math.toRadians(bullet.getAngle());
        bullet.setX(bullet.getX() + (float) Math.cos(radians) * homingSpeed * delta);
        bullet.setY(bullet.getY() + (float) Math.sin(radians) * homingSpeed * delta);
    }

    private GameObject findNearestEnemy(Bullet bullet) {
        if (entities == null) return null;

        GameObject nearest = null;
        float minDistanceSquared = Float.MAX_VALUE;
        for (GameObject entity : entities) {
            if (entity instanceof Enemy && !entity.isDestroyed()) {
                float dx = entity.getX() + entity.getWidth() / 2f - bullet.getX();
                float dy = entity.getY() + entity.getHeight() / 2f - bullet.getY();
                float distanceSquared = dx * dx + dy * dy;
                if (distanceSquared < minDistanceSquared) {
                    minDistanceSquared = distanceSquared;
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
    public float getHomingSpeed() { return homingSpeed; }
    public void setHomingSpeed(float speed) { this.homingSpeed = speed; }
    public float getTurnSpeed() { return turnSpeed; }
    public void setTurnSpeed(float speed) { this.turnSpeed = speed; }
    public float getDriftDuration() { return driftDuration; }
    public void setDriftDuration(float duration) { this.driftDuration = duration; }
}
