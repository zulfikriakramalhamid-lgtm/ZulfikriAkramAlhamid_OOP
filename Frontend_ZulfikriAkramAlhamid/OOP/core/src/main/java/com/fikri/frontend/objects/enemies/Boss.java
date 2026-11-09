package com.fikri.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.fikri.frontend.objects.patterns.entityStrategy.FixedMovement;

public class Boss extends Enemy {

    public Boss(String name, int hp) {
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
        setShootingPattern(new SpreadShot(200f, 3, 30f, 10));
        setMovementPattern(new FixedMovement());
    }

    public Boss(float x, float y) {
        super(x, y, 64, 64, 100f, null, "Rumia", 200, 500);
        setShootingPattern(new SpreadShot(200f, 3, 30f, 10));
        setMovementPattern(new FixedMovement());
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
        setShootingPattern(new SpreadShot(200f, 3, 30f, 10));
        setMovementPattern(new FixedMovement());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player player) {
            if (canCollide()) {
                player.takeDamage(20);
                resetCollisionCooldown();
            }
        }
    }
}
