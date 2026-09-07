package com.fikri.frontend;

import com.badlogic.gdx.graphics.Color;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;

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

        setHp(getHp() - damage);

        if (this.hp > 0) {
            System.out.println(name + " took " + damage
                + " damage! Remaining HP: " + hp);
        } else {
            System.out.println(name + " has been defeated!");
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();
        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");


        target.takeDamage(damage);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void addScore(long points) {
        if (points > 0) {
            score += points;
            System.out.println(getName() + "Gained" + points + "pts! Total Score :"  + this.score);
        }
    }

    public void collectItem(Item item){
        System.out.println(getName() + "collected" + item.getScoreValue());

        if (item.getScoreValue() > 0) {
            addScore(item.getScoreValue());
        }
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getHp(){
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getPower(){
        return power;
    }
    public void setPower (int power){
        this.power = power;
    }
    public int getSpellCards(){
        return spellCards;
    }
    public void setSpellCards(int spellCards){
        this.spellCards = spellCards;
    }
    public long getScore(){
        return score;
    }}


