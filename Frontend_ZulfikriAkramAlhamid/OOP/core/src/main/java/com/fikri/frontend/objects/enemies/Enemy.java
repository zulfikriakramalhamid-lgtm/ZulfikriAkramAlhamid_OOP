package com.fikri.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.EntityShooter;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.Systems.AudioManager;
import com.fikri.frontend.objects.patterns.ShootingPattern;
import com.fikri.frontend.objects.patterns.entityStrategy.EntityMovementPattern;

public class Enemy extends EntityShooter {
    private String name;
    private int hp;
    private int maxHp;
    private long scoreValue;
    protected float collisionCooldown = 0f;

    public Enemy(String name, int hp) {
        super(0, 0, 32, 32, 100f, Color.WHITE);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 500;
    }

    public Enemy(float x, float y, String name, int hp) {
        super(x, y, 32, 32, 100f, Color.WHITE);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 500;
    }

    public Enemy(float x, float y, float width, float height, float speed, Color color, String name, int hp, long scoreValue) {
        super(x, y, width, height, speed, color);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    // Konstruktor Constructor Injection untuk Modul 9 (Strategy & DI)
    public Enemy(float x, float y, float width, float height, Color color, String name, int hp, long scoreValue, ShootingPattern shootingPattern, EntityMovementPattern movementPattern) {
        super(x, y, width, height, 0, color, shootingPattern, movementPattern);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void attack(Player player, int damage) {
        System.out.println(name + " attacks " + player.getName() + " for " + damage + " DMG!");
        player.takeDamage(damage);
    }

    public boolean takeDamage(int damage) {
        boolean wasAlive = isAlive();
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " took " + damage + " damage! HP: " + this.hp + "/" + this.maxHp);
        if (wasAlive && this.hp == 0) {
            AudioManager.getInstance().playHitSfx();
            System.out.println(name + " was defeated!");
            destroy();
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
        super.update(delta); // Memanggil updateMovement dari EntityShooter
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
