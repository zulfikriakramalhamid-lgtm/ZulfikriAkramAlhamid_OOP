package com.fikri.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;

public class Boss extends Enemy {
    public Boss(String name, int hp) {
        super(380, 400, 48, 48, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 48, 48, Color.BLUE, name, hp, 5000L);
    }

    @Override
    public void onCollision(Collidable other) {
        // TODO: Cek apakah other yang diterima method ini adalah Player
        // TODO: Cetak "Player touches boss"
        System.out.println("Player touches boss");
    }
}

