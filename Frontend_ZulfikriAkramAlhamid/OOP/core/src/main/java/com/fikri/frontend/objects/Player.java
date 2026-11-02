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

import java.util.ArrayList;
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

    private final List<GameObserver> observers = new ArrayList<>();

    public Player(String name, int hp, int power, int spellCards) {
        this(200f, 50f, name, hp, power, spellCards);
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32f, 48f, 200f, Color.WHITE);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
    }

    public void registerObserver(GameObserver observer) {
        // TODO:
        // 1. Cek apakah observer tidak null DAN belum ada di dalam list observers (!observers.contains(observer)).
        if(observer != null && !observers.contains(observer)){

        }
        // 2. Jika memenuhi syarat:
        //    - Tambahkan observer ke dalam list observers.
        observers.add(observer);
        //    - Pancarkan seluruh state awal pemain saat ini ke observer tersebut:
            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onLivesChanged(lives);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
            observer.onGrazeChanged(grazeCount);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    // Metode Pemancar Notifikasi ke Seluruh Observer (4 poin)
    // TODO: Buat 6 metode private void berikut yang MELAKUKAN LOOP KE SELURUH ELEMEN DI 'observers'
    //       dan memanggil metode callback yang bersesuaian:
    // 1. notifyScoreChanged()      -> memanggil obs.onScoreChanged(score)
    private void notifyScoreChanged(){
        for(GameObserver obs : observers){
            obs.onScoreChanged(score);
        }
    }
    // 2. notifyHpChanged()         -> memanggil obs.onHpChanged(hp)
    private void notifyHpChanged(){
        for(GameObserver obs : observers){
            obs.onHpChanged(hp);
        }
    }
    // 3. notifyLivesChanged()      -> memanggil obs.onLivesChanged(lives) (Sistem Nyawa Touhou)
    private void notifyLivesChanged(){
        for(GameObserver obs : observers){
            obs.onLivesChanged(lives);
        }
    }
    // 4. notifySpellCardsChanged() -> memanggil obs.onSpellCardsChanged(spellCards)
    private void notifySpellCardsChanged(){
        for(GameObserver obs : observers){
            obs.onSpellCardsChanged(spellCards);
        }
    }
    // 5. notifyPowerChanged()      -> memanggil obs.onPowerChanged(power)
    private void notifyPowerChanged(){
        for(GameObserver obs : observers){
            obs.onPowerChanged(power);
        }
    }
    // 6. notifyGrazeChanged()      -> memanggil obs.onGrazeChanged(grazeCount)
    private void notifyGrazeChanged(){
        for(GameObserver obs : observers){
            obs.onGrazeChanged(grazeCount);
        }
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
        float minX = 32f;
        float maxX = 416f - width;
        float minY = 16f;
        float maxY = 560f - height;

        if (x < minX) x = minX;
        if (x > maxX) x = maxX;
        if (y < minY) y = minY;
        if (y > maxY) y = maxY;
    }

    public void shoot(Enemy enemy) {
        if (enemy == null || !enemy.isAlive()) return;

        if (enemy.takeDamage(power)) {
            addScore(enemy.getScoreValue());
        }
    }

    public Bullet shootBullet() {
        return new Bullet(
            x + width / 2f - 8f,
            y + height,
            BulletType.PLAYER_BULLET,
            10 + power
        );
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
                    0f,
                    400f,
                    10 + power
                );
            }

            shootTimer = 0f;
        }
    }

    public void useBomb(BulletManager bulletManager, List<GameObject> entities) {
        if (spellCards > 0) {
            spellCards--;
            notifySpellCardsChanged();

            invulnerableTimer = 5f;

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
            if (invulnerableTimer > 0f &&
                ((int) (invulnerableTimer * 16f)) % 2 != 0) {
                return;
            }

            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f);
            }

            super.render(batch);

            if (focused) {
                batch.setColor(1f, 1f, 1f, 1f);
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

        hp -= damage;

        if (hp < 0) {
            hp = 0;
        }

        notifyHpChanged();

        if (hp == 0) {
            loseLife(bulletManager, entities);
        }
    }

    public void loseLife(
        BulletManager bulletManager,
        List<GameObject> entities
    ) {
        lives--;
        notifyLivesChanged();

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
        }

        if (lives > 0) {
            respawn();
        } else {
            destroy();
        }
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
    }

    public void collectItem(Item item) {
        if (item == null || item.isDestroyed()) return;

        ItemType type = item.getItemTypeEnum();

        if (type != null) {
            switch (type) {
                case POWER -> {
                    power += type.getPowerBonus();
                    notifyPowerChanged();
                    addScore(item.getScoreValue());
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
    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
        notifyHpChanged();
    }

    public int getPower() { return power; }
    public void setPower(int power) {
        this.power = Math.max(0, power);
        notifyPowerChanged();
    }

    public int getSpellCards() { return spellCards; }
    public void setSpellCards(int spellCards) {
        this.spellCards = Math.max(0, spellCards);
        notifySpellCardsChanged();
    }

    public long getScore() { return score; }

    public boolean isFocused() { return focused; }
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public int getGrazeCount() { return grazeCount; }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    public int getLives() { return lives; }
    public void setLives(int lives) {
        this.lives = Math.max(0, lives);
        notifyLivesChanged();
    }

    public boolean isGameOver() {
        return lives <= 0 || isDestroyed();
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }
}

