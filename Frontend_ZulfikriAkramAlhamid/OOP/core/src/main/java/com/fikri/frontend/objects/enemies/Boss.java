package com.fikri.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.Player;

public class Boss extends Enemy {

    public Boss(String name, int hp) {
        super(name, hp);
    }

    public Boss(float x, float y) {
        super(x, y, "Boss", 200);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, name, hp);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player player) {
            if (canCollide()) {
                System.out.println("Player collided with Boss!");
                player.takeDamage(20);
                resetCollisionCooldown();
            }
        }
    }
}
