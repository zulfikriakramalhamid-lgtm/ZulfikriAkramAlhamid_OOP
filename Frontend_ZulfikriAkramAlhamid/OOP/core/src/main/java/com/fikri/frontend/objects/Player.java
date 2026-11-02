
package com.fikri.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.patterns.ShootingPattern;
import com.fikri.frontend.objects.Systems.BulletManager;

import java.util.List;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int lives = 3;
    private int power;
    private int spellCards;
    private long score;

    private ShootingPattern shootingPattern;

    private int grazeCount = 0;
    private boolean focused = false;

    private float shootTimer = 0.1f;
    private float shootCooldown = 0.1f;
    private float invulnerableTimer = 0f;

    public Player(float x, float y, String name,
                  int hp, int power, int spellCards) {

        super(x, y, 32f, 48f, 200f, Color.WHITE);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
    }

    @Override
    public void update(float delta) {

        super.update(delta);

        shootTimer += delta;

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;

            if (invulnerableTimer < 0f) {
                invulnerableTimer = 0f;
            }
        }
    }

    public void move(float dx, float dy) {

        this.x += dx * speed;
        this.y += dy * speed;

        clampToPlayfield();
    }

    private void clampToPlayfield() {

        float minX = 32;
        float maxX = 416 - width;
        float minY = 16;
        float maxY = 560 - height;

        if (x < minX) x = minX;
        if (x > maxX) x = maxX;
        if (y < minY) y = minY;
        if (y > maxY) y = maxY;
    }

    public void shootBullet(BulletManager bulletManager) {

        if (bulletManager == null) return;

        if (shootTimer >= shootCooldown) {

            if (shootingPattern != null) {

                shootingPattern.execute(
                    x + width / 2f - 8f,
                    y + height,
                    bulletManager,
                    true
                );

            } else {

                bulletManager.spawnPlayerBullet(
                    x + width / 2f - 8f,
                    y + height,
                    0,
                    400f,
                    10 + power
                );
            }

            shootTimer = 0f;
        }
    }

    public void useBomb(
        BulletManager bulletManager,
        List<GameObject> entities
    ) {

        if (spellCards > 0) {

            spellCards--;
            invulnerableTimer = 5.0f;

            if (bulletManager != null) {
                bulletManager.clearEnemyBullets(entities);
            }

            System.out.println(
                name + " unleashes SPELL CARD (Fantasy Seal)! All enemy bullets cleared!"
            );

        } else {

            System.out.println("No Spell Cards (Bombs) remaining!");
        }
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        this.grazeCount++;
        addScore(50);
    }

    public void addScore(long amount) {
        this.score += amount;
    }

    @Override
    public void render(SpriteBatch batch) {

        if (batch != null && active && !isDestroyed()) {

            if (invulnerableTimer > 0f) {

                boolean visible =
                    ((int) (invulnerableTimer * 16)) % 2 == 0;

                if (!visible) return;
            }

            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f);
            }

            super.render(batch);

            if (focused) {
                batch.setColor(1f, 1f, 1f, 1.0f);
            }
        }
    }

    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {

        if (focused && shapeRenderer != null && active) {

            Rectangle core = getCoreHitbox();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(Color.RED);

            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                4f
            );

            shapeRenderer.setColor(Color.WHITE);

            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                2f
            );

            shapeRenderer.end();
        }
    }

    @Override
    public Rectangle getCoreHitbox() {

        return new Rectangle(
            x + width / 2f - 4f,
            y + height / 2f - 4f,
            8f,
            8f
        );
    }

    public void takeDamage(int damage) {
        takeDamage(damage, null, null);
    }

    public void takeDamage(
        int damage,
        BulletManager bulletManager,
        List<GameObject> entities
    ) {

        if (isInvulnerable() || isDestroyed()) return;

        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        if (this.hp == 0) {
            loseLife(bulletManager, entities);
        }
    }

    public void loseLife(
        BulletManager bulletManager,
        List<GameObject> entities
    ) {

        this.lives--;

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
        }

        if (this.lives > 0) {
            respawn();
        } else {
            destroy();
        }
    }

    public void respawn() {

        this.active = true;
        this.x = 200;
        this.y = 50;
        this.hp = 100;
        this.invulnerableTimer = 3.0f;
        this.power = Math.max(10, this.power - 16);
        this.spellCards = 3;
    }

    public void resetGame() {

        this.lives = 3;
        this.hp = 100;
        this.power = 15;
        this.spellCards = 3;
        this.score = 0;
        this.grazeCount = 0;

        this.active = true;
        this.x = 200;
        this.y = 50;
        this.invulnerableTimer = 3.0f;
    }

    public void collectItem(Item item) {

        if (item == null || item.isDestroyed()) return;

        ItemType type = item.getItemTypeEnum();

        if (type != null) {

            switch (type) {

                case POWER -> {
                    this.power += type.getPowerBonus();
                    addScore(item.getScoreValue());
                }

                case POINT -> addScore(item.getScoreValue());

                case BOMB -> {
                    this.spellCards++;
                    addScore(item.getScoreValue());
                }

                case LIFE -> {
                    this.lives++;
                    this.hp = 100;
                    addScore(item.getScoreValue());
                }
            }
        }

        item.destroy();
    }

    // Getter

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getPower() {
        return power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public long getScore() {
        return score;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public int getGrazeCount() {
        return grazeCount;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }
}
