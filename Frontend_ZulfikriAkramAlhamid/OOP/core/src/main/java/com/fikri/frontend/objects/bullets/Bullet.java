package com.fikri.frontend.objects.bullets;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.BulletType;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.enemies.Enemy;

public class Bullet extends GameObject {
    private BulletType bulletType;
    private int damage;


    public Bullet(float x, float y, BulletType bulletType, int damage) {
        super(x, y, 16,16 ,400f , Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
    }

    public Bullet(float x, float y, float speed, BulletType bulletType, int damage) {
        super(x, y,16, 16, speed, Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
    }

    @Override
    public void update(float delta) {
        this.y += this.speed * delta;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Enemy enemy) {
            System.out.println("Bullet hit " + enemy.getName() + " for " + damage + " DMG!");
            enemy.takeDamage(damage);
            destroy();
        }
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public int getDamage() {
        return damage;
    }
}






