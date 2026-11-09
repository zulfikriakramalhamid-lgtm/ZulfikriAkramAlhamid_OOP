package com.fikri.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.BulletType;
import com.fikri.frontend.objects.enemies.Enemy;
import com.fikri.frontend.objects.patterns.ShootingPattern;
import com.fikri.frontend.objects.Systems.BulletManager;
import com.fikri.frontend.observers.GameObserver;
import com.fikri.frontend.objects.Systems.AudioManager;
import com.fikri.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.fikri.frontend.objects.patterns.shootingStrategy.SideShot;
import com.fikri.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.fikri.frontend.objects.patterns.shootingStrategy.CompositeShootingPattern;
import com.fikri.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.fikri.frontend.objects.patterns.entityStrategy.FixedMovement;

import java.util.ArrayList;
import java.util.List;

public class Player extends EntityShooter {
    private String name;
    private int hp;
    private int lives = 3;
    private int power;
    private int spellCards;
    private long score;

    private int grazeCount = 0;
    private boolean focused = false;
    private float shootCooldown = 5f / 60f;
    private float shootTimer = shootCooldown;
    private float invulnerableTimer = 0f;

    private float homingCooldown = 30f / 60f;
    private float homingShootTimer = homingCooldown;

    private ShootingPattern mainShootingPattern;
    private ShootingPattern sideShootingPattern;

    private final List<GameObserver> observers = new ArrayList<>();

    public Player(String name, int hp, int power, int spellCards) {
        this(200f, 50f, name, hp, power, spellCards);
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32f, 48f, 240f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public void updatePowerStrategy() {
        float spreadAngle = focused ? 4f : 18f;
        float dualSpreadAngle = focused ? 3f : 12f;
        float sideHOffset = focused ? 8f : 24f;
        float sideVOffset = focused ? 16f : -16f;
        float leftAngle = focused ? 100f : 120f;
        float rightAngle = focused ? 80f : 60f;

        float normalSpeed = 720f;
        float homingSpeed = 500f;

        if (power < 8) {
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = null;
            homingCooldown = 30f / 60f;
        } else if (power < 16) {
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(null, 360f));
            homingCooldown = 30f / 60f;
        } else if (power < 32) {
            mainShootingPattern = new SpreadShot(normalSpeed, 2, dualSpreadAngle, 30, 8f, null, null, null);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(null, 360f));
            homingCooldown = 30f / 60f;
        } else if (power < 48) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(null, 360f), "bullet_amulet_homing");
            homingCooldown = 30f / 60f;
        } else if (power < 64) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new SideShot(homingSpeed, 12, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(null, 360f), "bullet_amulet_homing");
            homingCooldown = 15f / 60f;
        } else if (power < 80) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(homingSpeed, 9, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(null, 360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 12, sideHOffset + 4f, sideVOffset, leftAngle + 30f, rightAngle - 30f, true, new HomingBulletMovement(null, 360f), "bullet_amulet_homing")
            );
            homingCooldown = 15f / 60f;
        } else if (power < 128) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(homingSpeed, 8, sideHOffset, sideVOffset, leftAngle - 10f, rightAngle + 10f, true, new HomingBulletMovement(null, 360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 8, sideHOffset + 4f, sideVOffset, leftAngle + 10f, rightAngle - 10f, true, new HomingBulletMovement(null, 360f), "bullet_amulet_homing")
            );
            homingCooldown = 15f / 60f;
        } else {
            mainShootingPattern = new SpreadShot(normalSpeed, 4, spreadAngle * 1.2f, 24, 16f, null, null, null);
            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(homingSpeed, 8, sideHOffset, sideVOffset, leftAngle - 10f, rightAngle + 10f, true, new HomingBulletMovement(null, 360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 8, sideHOffset + 4f, sideVOffset, leftAngle + 10f, rightAngle - 10f, true, new HomingBulletMovement(null, 360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 8, sideHOffset + 8f, sideVOffset, leftAngle + 30f, rightAngle - 30f, true, new HomingBulletMovement(null, 360f), "bullet_amulet_homing")
            );
            homingCooldown = 15f / 60f;
        }

        if (sideShootingPattern != null) {
            setShootingPattern(new CompositeShootingPattern(mainShootingPattern, sideShootingPattern));
        } else {
            setShootingPattern(mainShootingPattern);
        }
    }

    public void registerObserver(GameObserver observer) {
        if(observer != null && !observers.contains(observer)){
            observers.add(observer);
            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onLivesChanged(lives);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
            observer.onGrazeChanged(grazeCount);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyScoreChanged(){ for(GameObserver obs : observers) obs.onScoreChanged(score); }
    private void notifyHpChanged(){ for(GameObserver obs : observers) obs.onHpChanged(hp); }
    private void notifyLivesChanged(){ for(GameObserver obs : observers) obs.onLivesChanged(lives); }
    private void notifySpellCardsChanged(){ for(GameObserver obs : observers) obs.onSpellCardsChanged(spellCards); }
    private void notifyPowerChanged(){ for(GameObserver obs : observers) obs.onPowerChanged(power); }
    private void notifyGrazeChanged(){ for(GameObserver obs : observers) obs.onGrazeChanged(grazeCount); }

    @Override
    public void update(float delta) {
        super.update(delta);
        shootTimer += delta;
        homingShootTimer += delta;

        if (shootTimer > 0.15f) {
            shootTimer = shootCooldown;
            homingShootTimer = homingCooldown;
        }

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;
            if (invulnerableTimer < 0f) invulnerableTimer = 0f;
        }
    }

    public void move(float dx, float dy) {
        // Normalisasi agar gerakan diagonal tidak lebih cepat dari gerakan lurus.
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length > 1f) {
            dx /= length;
            dy /= length;
        }
        this.x += dx * speed;
        this.y += dy * speed;
        clampToPlayfield();
    }

    private void clampToPlayfield() {
        float minX = 32f;
        float maxX = 416f - width;
        float minY = 16f;
        float maxY = 560f - height;

        if (x < minX) x = minX;
        if (x > maxX) x = maxX;
        if (y < minY) y = minY;
        if (y > maxY) y = maxY;
    }

    @Override
    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null || !active || isDestroyed()) return;

        boolean fired = false;
        float originX = x + width / 2f - 8f;
        float originY = y + height;

        if (shootTimer >= shootCooldown) {
            if (mainShootingPattern != null) {
                mainShootingPattern.execute(originX, originY, bulletManager, true);
            } else if (shootingPattern != null) {
                shootingPattern.execute(originX, originY, bulletManager, true);
            } else {
                bulletManager.spawnPlayerBullet(originX, originY, 0f, 400f, 10 + power);
            }
            shootTimer = 0f;
            fired = true;
        }

        if (sideShootingPattern != null && homingShootTimer >= homingCooldown) {
            sideShootingPattern.execute(originX, originY, bulletManager, true);
            homingShootTimer = 0f;
            fired = true;
        }

        if (fired) AudioManager.getInstance().playShootSfx();
    }

    public void useBomb(BulletManager bulletManager, List<GameObject> entities) {
        if (spellCards > 0) {
            spellCards--;
            invulnerableTimer = 5.0f;
            notifySpellCardsChanged();
            if (bulletManager != null) {
                bulletManager.clearEnemyBullets(entities);

                GameObject target = null;
                if (entities != null) {
                    for (GameObject obj : entities) {
                        if (obj instanceof Enemy enemy && !enemy.isDestroyed()) {
                            target = enemy;
                            break;
                        }
                    }
                }
                bulletManager.spawnBombOrbs(x + width / 2f, y + height / 2f, target, entities);
            }
            System.out.println(name + " unleashes SPELL CARD: Spirit Sign Fantasy Seal! All enemy bullets cleared & 8 homing orbs launched!");
        } else {
            System.out.println("No Spell Cards (Bombs) remaining!");
        }
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        grazeCount++;
        notifyGrazeChanged();
        addScore(50);
    }

    public void addScore(long points) {
        if (points > 0) {
            score += points;
            notifyScoreChanged();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active && !isDestroyed()) {
            if (invulnerableTimer > 0f && ((int)(invulnerableTimer / 0.016f)) % 2 != 0) {
                return;
            }
            if (focused) batch.setColor(1f, 1f, 1f, 0.5f);
            super.render(batch);
            if (focused) batch.setColor(1f, 1f, 1f, 1f);
        }
    }

    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {
        if (focused && shapeRenderer != null && active) {
            Rectangle core = getCoreHitbox();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 4f);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 2f);
            shapeRenderer.end();
        }
    }

    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(x + width / 2f - 4f, y + height / 2f - 4f, 8f, 8f);
    }

    public void takeDamage(int damage) {
        takeDamage(damage, null, null);
    }

    public void takeDamage(int damage, BulletManager bulletManager, List<GameObject> entities) {
        if (isInvulnerable() || isDestroyed()) return;
        hp -= damage;
        if (hp < 0) hp = 0;
        notifyHpChanged();
        if (hp == 0) loseLife(bulletManager, entities);
    }

    public void loseLife(BulletManager bulletManager, List<GameObject> entities) {
        lives--;
        notifyLivesChanged();
        if (bulletManager != null) bulletManager.clearEnemyBullets();
        if (lives > 0) respawn();
        else destroy();
    }

    public void respawn() {
        active = true;
        x = 200f;
        y = 50f;
        hp = 100;
        invulnerableTimer = 3f;
        power = Math.max(10, power - 16);
        spellCards = 3;
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
        updatePowerStrategy();
    }

    public void resetGame() {
        lives = 3;
        hp = 100;
        power = 15;
        spellCards = 3;
        score = 0;
        grazeCount = 0;
        active = true;
        x = 200f;
        y = 50f;
        invulnerableTimer = 3f;
        notifyLivesChanged();
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
        notifyScoreChanged();
        notifyGrazeChanged();
        updatePowerStrategy();
    }

    public void collectItem(Item item) {
        if (item == null || item.isDestroyed()) return;
        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    power = Math.min(128, power + type.getPowerBonus());
                    updatePowerStrategy();
                    notifyPowerChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POWER item! Power increased to " + power);
                }
                case POINT -> addScore(item.getScoreValue());
                case BOMB -> {
                    spellCards++;
                    notifySpellCardsChanged();
                    addScore(item.getScoreValue());
                }
                case LIFE -> {
                    lives++;
                    hp = 100;
                    notifyLivesChanged();
                    notifyHpChanged();
                    addScore(item.getScoreValue());
                }
            }
        }
        item.destroy();
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); notifyHpChanged(); }
    public int getPower() { return power; }
    public void setPower(int power) {
        if (this.power != power) {
            this.power = Math.max(0, Math.min(128, power));
            updatePowerStrategy();
            notifyPowerChanged();
        }
    }
    public int getSpellCards() { return spellCards; }
    public void setSpellCards(int spellCards) { this.spellCards = Math.max(0, spellCards); notifySpellCardsChanged(); }
    public long getScore() { return score; }
    public boolean isFocused() { return focused; }
    public void setFocused(boolean focused) {
        if (this.focused != focused) {
            this.focused = focused;
            updatePowerStrategy();
        }
    }
    public int getGrazeCount() { return grazeCount; }
    public boolean isInvulnerable() { return invulnerableTimer > 0f; }
    public float getInvulnerableTimer() { return invulnerableTimer; }
    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = Math.max(0, lives); notifyLivesChanged(); }
    public boolean isGameOver() { return lives <= 0 || isDestroyed(); }
    public ShootingPattern getMainShootingPattern() { return mainShootingPattern; }
    public ShootingPattern getSideShootingPattern() { return sideShootingPattern; }
    public float getShootCooldown() { return shootCooldown; }
    public float getHomingCooldown() { return homingCooldown; }
}
