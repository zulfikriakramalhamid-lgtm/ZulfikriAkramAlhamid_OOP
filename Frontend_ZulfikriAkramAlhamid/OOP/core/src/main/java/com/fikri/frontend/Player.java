package com.fikri.frontend;

public class Player {
    public String name;
    public int hp;
    public int power;
    public int spellCards;

    public Player(String name, int hp, int power, int spellCard) {
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCard;
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
}
