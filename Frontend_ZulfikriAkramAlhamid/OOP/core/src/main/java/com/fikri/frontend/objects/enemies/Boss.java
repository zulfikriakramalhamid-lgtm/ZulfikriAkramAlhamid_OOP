package com.fikri.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.fikri.frontend.objects.patterns.shootingStrategy.SpreadShot;

public class Boss extends Enemy {

    private boolean survivesAtZeroHp = false;

    public Boss(String name, int hp) {
        super(380f, 400f, 64f, 64f, 100f, Color.BLUE, name, hp, 5000L);
        setShotHitboxSize(48f, 56f);
        injectDefaultStrategies();
    }

    public Boss(float x, float y) {
        super(x, y, 64f, 64f, 100f, Color.BLUE, "Rumia", 200, 5000L);
        setShotHitboxSize(48f, 56f);
        injectDefaultStrategies();
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64f, 64f, 100f, Color.BLUE, name, hp, 5000L);
        setShotHitboxSize(48f, 56f);
        injectDefaultStrategies();
    }

    private void injectDefaultStrategies() {
        setShootingPattern(new SpreadShot(200f, 3, 30f, 10));
        setMovementPattern(new FixedMovement());
    }

    public void setLife(int life) {
        this.hp = Math.max(0, life);
        this.maxHp = this.hp;
    }

    @Override
    public boolean takeDamage(int damage) {
        if (!damageable) {
            return false;
        }

        if (!survivesAtZeroHp) {
            return super.takeDamage(damage);
        }

        this.hp = Math.max(0, this.hp - damage);
        System.out.println(name + " took " + damage + " damage! HP: " + this.hp + "/" + this.maxHp);
        return false;
    }

    public boolean isSurvivesAtZeroHp() {
        return survivesAtZeroHp;
    }

    public void setSurvivesAtZeroHp(boolean survivesAtZeroHp) {
        this.survivesAtZeroHp = survivesAtZeroHp;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player player && canCollide()) {
            player.takeDamage(20);
            resetCollisionCooldown();
        }
    }
}
