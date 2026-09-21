package com.fikri.frontend.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.enemies.Enemy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;

import static com.badlogic.gdx.Input.Keys.*;

public class Player extends GameObject {
    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 32, 200f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 32, 200f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();
        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");
        boolean defeated = target.takeDamage(damage);
        if (defeated) {
            addScore(target.getScoreValue());
        }
    }

    // Soal 10: Method shootBullet()
    public Bullet shootBullet() {
        int damage = 10 + power;
        System.out.println(name + " shoots bullet dealing " + damage + " DMG!");
        float bulletX = this.x + (this.width / 2f) - 4f;
        float bulletY = this.y + this.height;
        return new Bullet(bulletX, bulletY, BulletType.AMULET, damage);
    }

    public void takeDamage(int damage) {
        setHp(this.hp - damage);
        System.out.println(getName() + " took " + damage + " damage! Remaining HP: " + getHp());
    }

    public void addScore(long points) {
        this.score += points;
        System.out.println(name + " gained " + points + " pts! Total Score: " + score);
    }

    public void collectItem(Item item) {
        if (item.isDestroyed()) return;
        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    this.power += type.getPowerBonus();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POWER item! Power increased to " + power);
                }
                case POINT -> {
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POINT item!");
                }
                case BOMB -> {
                    this.spellCards += 1;
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected BOMB item! SpellCards: " + spellCards);
                }
                case LIFE -> {
                    this.hp += 20;
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected LIFE item! HP: " + hp);
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }
        item.destroy();
    }

    @Override
    public void update(float delta) {
        if (Gdx.input != null) {
            if (Gdx.input.isKeyPressed(W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += speed * delta;
            }
            if (Gdx.input.isKeyPressed(S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                y -= speed * delta;
            }
            if (Gdx.input.isKeyPressed(A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                x -= speed * delta;
            }
            if (Gdx.input.isKeyPressed(D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                x += speed * delta;
            }
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item item) {
            if (!item.isCollected()) {
                item.setCollected(true);
                collectItem(item);
            }
        } else if (other instanceof Enemy enemy) {
            if (enemy.canCollide()) {
                takeDamage(10);
                enemy.resetCollisionCooldown();
            }
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    public int getSpellCards() { return spellCards; }
    public void setSpellCards(int spellCards) { this.spellCards = spellCards; }

    public long getScore() { return score; }
}
