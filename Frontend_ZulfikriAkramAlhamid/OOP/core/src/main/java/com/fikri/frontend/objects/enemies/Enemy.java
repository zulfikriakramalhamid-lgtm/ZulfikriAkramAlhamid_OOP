
package com.fikri.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.EntityShooter;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.Systems.AudioManager;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.patterns.ShootingPattern;
import com.fikri.frontend.objects.patterns.entityStrategy.EntityMovementPattern;

public class Enemy extends EntityShooter {
    public String name;
    public int hp;
    public int maxHp;
    private long scoreValue;
    protected float collisionCooldown = 0f;

    // Pre-CS Modul 10
    protected ItemType itemDrop = null;
    protected boolean damageable = true;
    protected boolean collidable = true;
    protected float shotHitboxWidth = 0f;
    protected float shotHitboxHeight = 0f;

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

    public Enemy(float x, float y, float width, float height,
                 float speed, Color color, String name,
                 int hp, long scoreValue) {
        super(x, y, width, height, speed, color);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    // Konstruktor Constructor Injection untuk Modul 9
    public Enemy(float x, float y, float width, float height,
                 Color color, String name, int hp, long scoreValue,
                 ShootingPattern shootingPattern,
                 EntityMovementPattern movementPattern) {
        super(x, y, width, height, 0, color,
            shootingPattern, movementPattern);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void attack(Player player, int damage) {
        System.out.println(name + " attacks " + player.getName()
            + " for " + damage + " DMG!");
        player.takeDamage(damage);
    }

    public boolean takeDamage(int damage) {
        if (!damageable) {
            return false;
        }

        boolean wasAlive = isAlive();
        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        System.out.println(name + " took " + damage
            + " damage! HP: " + this.hp + "/" + this.maxHp);

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
        super.update(delta);

        if (collisionCooldown > 0) {
            collisionCooldown -= delta;
        }
    }

    @Override
    public void onCollision(Collidable other) {}

    // Pre-CS Modul 10: Getter dan Setter

    public ItemType getItemDrop() {
        return itemDrop;
    }

    public void setItemDrop(ItemType itemDrop) {
        this.itemDrop = itemDrop;
    }

    public boolean isDamageable() {
        return damageable;
    }

    public void setDamageable(boolean damageable) {
        this.damageable = damageable;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public Rectangle getShotHitbox() {
        if (shotHitboxWidth <= 0f || shotHitboxHeight <= 0f) {
            return getCoreHitbox();
        }

        return new Rectangle(
            x + (width - shotHitboxWidth) / 2f,
            y + (height - shotHitboxHeight) / 2f,
            shotHitboxWidth,
            shotHitboxHeight
        );
    }

    public void setShotHitboxSize(float width, float height) {
        this.shotHitboxWidth = width;
        this.shotHitboxHeight = height;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public long getScoreValue() { return scoreValue; }
}
