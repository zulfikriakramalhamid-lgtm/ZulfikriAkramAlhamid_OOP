package com.fikri.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.Player;

public class Fairy extends Enemy {

    public Fairy(String name, int hp) {
        super(name, hp);
    }

    public Fairy(float x, float y) {
        super(x, y, "Fairy", 20);
    }

    public Fairy(float x, float y, String name, int hp) {
        super(x, y, name, hp);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        y -= speed * delta;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player player) {
            if (canCollide()) {
                System.out.println("Player collided with Fairy!");
                player.takeDamage(10);
                resetCollisionCooldown();
            }
        }
    }
}
