
package com.fikri.frontend.objects.patterns.entityStrategy;

import com.fikri.frontend.objects.GameObject;

public class SCurveMovement implements EntityMovementPattern {

    private float speed;
    private float angle;
    private boolean mirrored;

    private float timer = 0f;

    private float startX;
    private float startY;
    private boolean initialized = false;

    private float amplitude = 55f;
    private float frequency = 1.5f;

    public SCurveMovement(float speed, float initialAngle, boolean mirrored) {
        this.speed = speed;
        this.angle = initialAngle;
        this.mirrored = mirrored;
    }

    @Override
    public void move(GameObject entity, float delta) {
        if (entity == null) {
            return;
        }

        if (!initialized) {
            startX = entity.getX();
            startY = entity.getY();
            initialized = true;
        }

        timer += delta;

        float direction = mirrored ? -1f : 1f;

        // Gerakan horizontal membentuk kurva S.
        float offsetX = direction * amplitude *
            (float) Math.sin(timer * frequency);

        // Fairy bergerak turun secara konsisten.
        float newX = startX + offsetX;
        float newY = startY - speed * timer;

        entity.setX(newX);
        entity.setY(newY);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean isMirrored() {
        return mirrored;
    }

    public void setMirrored(boolean mirrored) {
        this.mirrored = mirrored;
    }
}
