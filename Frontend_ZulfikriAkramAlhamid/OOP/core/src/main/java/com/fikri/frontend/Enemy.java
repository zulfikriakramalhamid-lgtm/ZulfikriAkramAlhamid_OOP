package com.fikri.frontend;

import com.badlogic.gdx.graphics.Color;

public class Enemy extends GameObject {

    private String name;
    private int hp;
    private int maxHp;
    protected long scoreValue;


    public Enemy(String name, int hp) {
        super(200, 380, 24, 24, 0, Color.PINK);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100;
    }


    public Enemy(float x, float y, float width, float height,
                 Color color, String name, int hp, long scoreValue) {
        super(x, y, width, height, 0, color);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    public boolean takeDamage(int damage) {


        if (this.hp <= 0) {
            return false;
        }

        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        System.out.println(name + " took " + damage
            + " damage! HP: " + hp + "/" + maxHp);

        if (this.hp == 0) {
            System.out.println(name + " was defeated!");
            return true;
        }

        return false;
    }

    public void attack(Player player, int damage) {
        System.out.println(name
            + " unleashes bullet barrage on "
            + "Player" + "!");

        player.takeDamage(damage);
    }

    public boolean isAlive() {
        return hp > 0;
    }
    public String getName() {
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

    public int getMaxHp(){
        return maxHp;

    }

    public long getScoreValue(){
        return scoreValue;
    }

    public void setScoreValue(long scoreValue){
        this.scoreValue = scoreValue;
    }
}
