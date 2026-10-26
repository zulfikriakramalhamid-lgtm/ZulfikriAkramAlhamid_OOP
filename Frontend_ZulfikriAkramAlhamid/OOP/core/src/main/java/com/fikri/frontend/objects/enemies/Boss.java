package com.fikri.frontend.objects.enemies;

import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.Systems.BulletManager;
import com.fikri.frontend.objects.patterns.ShootingPattern;
import com.fikri.frontend.objects.patterns.shootingStrategy.SpreadShot;

public class Boss extends Enemy {
    private ShootingPattern shootingPattern;

    public Boss(String name, int hp) {
        super(name, hp);
        this.shootingPattern = new SpreadShot(200f, 3, 30f);
    }

    public Boss(float x, float y) {
        super(x, y, 64, 64, 100f, null, "Rumia", 200, 500);
        this.shootingPattern = new SpreadShot(200f, 3, 30f);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, 100f, null, name, hp, 500);
        this.shootingPattern = new SpreadShot(200f, 3, 30f);
    }

    public void shootBullet(BulletManager bulletManager) {
        if (shootingPattern != null && isAlive()) {
            shootingPattern.execute(x + width / 2f - 8f, y, bulletManager, false);
        }
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player player) {
            if (canCollide()) {
                player.takeDamage(20);
                resetCollisionCooldown();
            }
        }
    }
}
