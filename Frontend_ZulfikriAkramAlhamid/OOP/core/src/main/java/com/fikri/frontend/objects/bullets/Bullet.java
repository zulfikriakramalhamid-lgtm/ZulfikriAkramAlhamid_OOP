package com.fikri.frontend.objects.bullets;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.BulletType;
import com.fikri.frontend.objects.GameObject;

public class Bullet extends GameObject {
    private BulletType bulletType;
    private int damage;

    // Soal 3a: Kecepatan default (speed = 400f)
    public Bullet(float x, float y, BulletType bulletType, int damage) {
        super(x, y, 8, 16, 400f, Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
    }

    // Soal 3b: Kecepatan custom
    public Bullet(float x, float y, float speed, BulletType bulletType, int damage) {
        super(x, y, 8, 16, speed, Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
    }

    // Soal 4: Bergerak ke atas
    @Override
    public void update(float delta) {
        this.y += this.speed * delta;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public int getDamage() {
        return damage;
    }
}
