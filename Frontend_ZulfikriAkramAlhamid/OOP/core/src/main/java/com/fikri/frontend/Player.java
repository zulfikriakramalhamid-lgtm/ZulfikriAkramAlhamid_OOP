package com.fikri.frontend;

import com.badlogic.gdx.graphics.Color;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int power;
    private int spellCards;
    protected long score;

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 32, 0, Color.RED);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }


    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 32, 0, Color.RED);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        if (this.hp > 0) {
            System.out.println(name + " took " + damage
                + " damage! Remaining HP: " + hp);
        } else {
            System.out.println(name + " has been defeated!");
        }
    }

    public void shoot(Enemy target) {
        int damage = power + 10;

        System.out.println(name + " shoots " + target.name
            + " dealing " + damage + " DMG!");

        target.takeDamage(damage);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void addScore(long points) {
        if (points > 0) {
            score += points;
        }
    }
}
