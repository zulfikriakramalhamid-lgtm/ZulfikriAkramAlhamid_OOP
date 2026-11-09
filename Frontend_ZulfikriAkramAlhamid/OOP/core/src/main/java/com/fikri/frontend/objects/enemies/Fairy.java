package com.fikri.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.patterns.entityStrategy.LinearEntityMovement;
import com.fikri.frontend.objects.patterns.shootingStrategy.FrontShot;

public class Fairy extends Enemy {
    private float shootTimer = 0f;
    private float shootDelay = 0.67f;
    private float shootInterval = 1.4f;
    private int maxShots = 2;
    private int shotsFired = 0;

    public Fairy(String name, int hp) {
        super(150f, 380f, 24f, 24f, 100f, Color.PINK, name, hp, 500L);
        injectDefaultStrategies();
    }

    public Fairy(float x, float y) {
        super(x, y, 24f, 24f, 100f, Color.PINK, "Fairy", 20, 500L);
        injectDefaultStrategies();
    }

    public Fairy(float x, float y, String name, int hp) {
        super(x, y, 24f, 24f, 100f, Color.PINK, name, hp, 500L);
        injectDefaultStrategies();
    }

    public Fairy(float x, float y, String name, int hp,
                 float shootDelay, float shootInterval, int maxShots) {
        super(x, y, 24f, 24f, 100f, Color.PINK, name, hp, 500L);
        this.shootDelay = shootDelay;
        this.shootInterval = shootInterval;
        this.maxShots = Math.max(0, maxShots);
        injectDefaultStrategies();
    }

    private void injectDefaultStrategies() {
        setShootingPattern(new FrontShot(150f, 10));
        setMovementPattern(new LinearEntityMovement(0f, -40f));
    }

    public boolean canShoot(float delta) {
        if (shotsFired >= maxShots || isDestroyed()) return false;

        shootTimer += delta;
        float requiredDelay = shotsFired == 0 ? shootDelay : shootInterval;

        if (shootTimer >= requiredDelay) {
            shotsFired++;
            shootTimer = 0f;
            return true;
        }
        return false;
    }

    public void setShootTiming(float shootDelay, float shootInterval, int maxShots) {
        this.shootDelay = Math.max(0f, shootDelay);
        this.shootInterval = Math.max(0f, shootInterval);
        this.maxShots = Math.max(0, maxShots);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player player && canCollide()) {
            player.takeDamage(10);
            resetCollisionCooldown();
        }
    }

    public int getShotsFired() { return shotsFired; }
    public float getShootTimer() { return shootTimer; }
    public float getShootDelay() { return shootDelay; }
    public float getShootInterval() { return shootInterval; }
    public int getMaxShots() { return maxShots; }
}
