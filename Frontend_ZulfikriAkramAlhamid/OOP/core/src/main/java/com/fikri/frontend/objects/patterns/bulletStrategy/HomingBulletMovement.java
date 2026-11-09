package com.fikri.frontend.objects.patterns.bulletStrategy;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.bullets.Bullet;

public class HomingBulletMovement implements BulletMovementPattern {
    private GameObject target;
    private float homingSpeed;

    public HomingBulletMovement(GameObject target, float homingSpeed) {
        this.target = target;
        this.homingSpeed = homingSpeed;
    }

    @Override
    public void move(Bullet bullet, float delta) {
        if (bullet == null) return;

        if (target != null && !target.isDestroyed()) {
            float targetX = target.getX() + target.getWidth() / 2f;
            float targetY = target.getY() + target.getHeight() / 2f;

            float angle = (float) Math.toDegrees(Math.atan2(targetY - bullet.getY(), targetX - bullet.getX()));
            bullet.setAngle(angle);

            float rad = (float) Math.toRadians(angle);
            float dx = (float) Math.cos(rad) * homingSpeed * delta;
            float dy = (float) Math.sin(rad) * homingSpeed * delta;

            bullet.setX(bullet.getX() + dx);
            bullet.setY(bullet.getY() + dy);
        } else {
            float rad = (float) Math.toRadians(bullet.getAngle());
            float dx = (float) Math.cos(rad) * bullet.getSpeed() * delta;
            float dy = (float) Math.sin(rad) * bullet.getSpeed() * delta;
            bullet.setX(bullet.getX() + dx);
            bullet.setY(bullet.getY() + dy);
        }
    }

    public GameObject getTarget() { return target; }
    public void setTarget(GameObject target) { this.target = target; }
    public float getHomingSpeed() { return homingSpeed; }
    public void setHomingSpeed(float homingSpeed) { this.homingSpeed = homingSpeed; }
}
