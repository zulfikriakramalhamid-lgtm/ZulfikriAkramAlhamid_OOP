package com.fikri.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.fikri.frontend.objects.patterns.shootingStrategy.SpreadShot;

public class Boss extends Enemy {

    public Boss(String name, int hp) {
        super(380f, 400f, 64f, 64f, 100f, Color.BLUE, name, hp, 5000L);
        injectDefaultStrategies();
    }

    public Boss(float x, float y) {
        super(x, y, 64f, 64f, 100f, Color.BLUE, "Rumia", 200, 5000L);
        injectDefaultStrategies();
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64f, 64f, 100f, Color.BLUE, name, hp, 5000L);
        injectDefaultStrategies();
    }

    private void injectDefaultStrategies() {
        setShootingPattern(new SpreadShot(200f, 3, 30f, 10));
        setMovementPattern(new FixedMovement());
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player player && canCollide()) {
            player.takeDamage(20);
            resetCollisionCooldown();
        }
    }
}
