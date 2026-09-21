package com.fikri.frontend.objects.bullets;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.BulletType;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.enemies.Enemy;

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


    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Enemy enemy) {
            // 1. Tampilkan pesan bahwa Bullet mengenai Enemy dalam format:
            System.out.println("Bullet Hit" + enemy.getName() + " For" + damage + " DMG!");
            //    Bullet hit [EnemyName] for [damage] DMG!
            enemy.takeDamage(damage);
            destroy();

            // 2. Panggil takeDamage() milik Enemy dengan damage milik Bullet ini.

            // 3. Bikin si bullet hancur (destroy) setelah mengenai Enemy, apapun hasilnya
            //    (baik enemy kalah atau masih hidup), krn satu bullet cuma boleh kena satu target.
        }
    }
    public BulletType getBulletType() {
        return bulletType;
    }

    public int getDamage() {
        return damage;
    }

}
