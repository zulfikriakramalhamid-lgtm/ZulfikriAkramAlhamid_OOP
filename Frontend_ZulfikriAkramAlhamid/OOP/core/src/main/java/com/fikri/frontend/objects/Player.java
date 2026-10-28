package com.fikri.frontend.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.fikri.frontend.objects.Systems.AudioManager;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.enemies.Enemy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.Systems.AssetManager;
import com.fikri.frontend.objects.Systems.EntityFactory;
import com.fikri.frontend.objects.Systems.BulletManager;
import com.fikri.frontend.objects.patterns.ShootingPattern;

public class Player extends GameObject {
    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private int score = 0;

    private int currentDir = 0;
    private ShootingPattern shootingPattern;

    public Player(String name, int hp) {
        super(0, 0, 32, 48, 200f, Color.GREEN);
        this.name = name;
        this.hp = hp;
        this.power = 0;
        this.spellCards = 2;
    }

    public Player(String name, int hp, int power, int spellCards) {
        super(0, 0, 32, 48, 200f, Color.GREEN);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 48, 200f, Color.GREEN);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        float dx = 0;

        if (Gdx.input != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                y -= speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                x -= speed * delta;
                dx -= 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                x += speed * delta;
                dx += 1;
            }
        }

        updateAnimationState(dx);
    }

    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();
        if (dx < 0) {
            if (currentDir != -1) {
                currentDir = -1;
                Animation<TextureRegion> anim = assets.getAnimation("player_left");
                if (anim != null) {
                    setAnimation(anim);
                }
            }
        } else if (dx > 0) {
            if (currentDir != 1) {
                currentDir = 1;
                Animation<TextureRegion> anim = assets.getAnimation("player_right");
                if (anim != null) {
                    setAnimation(anim);
                }
            }
        } else {
            if (currentDir != 0) {
                currentDir = 0;
                Animation<TextureRegion> anim = assets.getAnimation("player_idle");
                if (anim != null) {
                    setAnimation(anim);
                }
            }
        }
    }

    public Bullet shootBullet() {
        int damage = 10 + power;
        AudioManager.getInstance().playShootSfx();

        float bulletX = this.x + (this.width / 2f) - 8f;
        float bulletY = this.y + this.height;
        return EntityFactory.createPlayerBullet(bulletX, bulletY, damage);
    }

    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null) return;

        if (shootingPattern != null) {
            shootingPattern.execute(x + width / 2f - 8f, y + height, bulletManager, true);
        } else {
            bulletManager.spawnPlayerBullet(x + width / 2f - 8f, y + height, 0f, 400f, 10 + power);
        }
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) this.hp = 0;
        if (this.hp == 0) {
            destroy();
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();
        target.takeDamage(damage);
    }

    public void collectItem(Item item) {
        AudioManager.getInstance().playItemSfx();
        switch (item.getItemType()) {
            case "POWER" -> this.power += 1;
            case "POINT" -> this.score += 100;
            case "BOMB"  -> this.spellCards += 1;
            case "LIFE"  -> this.hp += 20;
        }
        item.destroy();
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item item) {
            collectItem(item);
        }
    }

    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(x + (width / 2f) - 4f, y + (height / 2f) - 4f, 8f, 8f);
    }

    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(x, y, width, height);
    }

    public void addScore(long score) {
        this.score += score;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getPower() { return power; }
    public int getSpellCards() { return spellCards; }
    public int getScore() { return score; }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }
}
