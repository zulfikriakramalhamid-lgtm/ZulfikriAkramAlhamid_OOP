package com.fikri.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.Player;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.Player;

public class Enemy extends GameObject {
    private String name;
    private int hp;
    private long scoreValue;
    protected float collisionCooldown = 0f;

    public Enemy(String name, int hp) {
        super(0, 0, 32, 32, 100f, Color.WHITE);
        this.name = name;
        this.hp = hp;
        this.scoreValue = 500;
    }

    public Enemy(float x, float y, String name, int hp) {
        super(x, y, 32, 32, 100f, Color.WHITE);
        this.name = name;
        this.hp = hp;
        this.scoreValue = 500;
    }

    public Enemy(float x, float y, float width, float height, float speed, Color color, String name, int hp, long scoreValue) {
        super(x, y, width, height, speed, color);
        this.name = name;
        this.hp = hp;
        this.scoreValue = scoreValue;
    }

    public void attack(Player player, int damage) {
        System.out.println(name + " attacks " + player.getName() + " for " + damage + " DMG!");
        player.takeDamage(damage);
    }

    public boolean takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            this.hp = 0;
            return true;
        }
        return false;
    }

    public boolean canCollide() {
        return collisionCooldown <= 0;
    }

    public void resetCollisionCooldown() {
        this.collisionCooldown = 1.0f;
    }

    @Override
    public void update(float delta) {
        if (collisionCooldown > 0) {
            collisionCooldown -= delta;
        }
    }

    @Override
    public void onCollision(Collidable other) {}

    public String getName() { return name; }
    public int getHp() { return hp; }
    public long getScoreValue() { return scoreValue; }
}
