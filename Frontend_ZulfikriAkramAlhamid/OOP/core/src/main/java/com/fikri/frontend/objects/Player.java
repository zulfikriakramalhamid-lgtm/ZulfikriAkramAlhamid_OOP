package com.fikri.frontend.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.enemies.Enemy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;

import static com.badlogic.gdx.Input.Keys.*;


import static java.lang.Character.getName;

public class Player extends GameObject {
    String name;
    int hp;
    int power;
    int spellCards;
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

    public void shoot(Enemy target) {
        int damage = 10 + getPower();
        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");
        boolean defeated = target.takeDamage(damage);
        if (defeated) {
            addScore(target.getScoreValue());
        }
    }

    private int getPower() {
            return Player;
    }

    public void collectItem(Item item) {
        System.out.println(getName() + " collected " + item.getItemType() + "!");
        if (item.getScoreValue() > 0) {
            addScore(item.getScoreValue());
        }
        public void collectItem(Item item) {
            ItemType type = item.getItemTypeEnum();
            if (type != null) {
                switch (type) {
                    case POWER -> {
                        // 1. Tambahkan power sebesar type.getPowerBonus() lewat this.power
                        public long tpe.type.getPowerBonus(){
                            this.power = power;
                        }
                        // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore() (addScore() sudah otomatis mencetak "gained X pts!")
                        addScore(item.getScoreValue()){
                            System.out.println("Gained X pts!");
                        }
                        }
                        // 3. Cetak: [name] collected POWER item! Power increased to [power]
                    }
                    case POINT -> {
                        // 1. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                        addScore(item.getScoreValue());
                        // 2. Cetak: [name] collected POINT item!
                        System.out.println(name + " Collected POIN item!");
                    }
                    case BOMB -> {
                        // 1. Tambahkan spellCards sebesar 1
                        spellCards += 1;
                        // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                        addScore(item.getScoreValue());
                        // 3. Cetak: [name] collected BOMB item! SpellCards: [spellCards]
                        System.out.println(name + " Collected BOMB item! SpellCards : " + spellCards);
                    }
                    case LIFE -> {
                        hp += 20;
                        addScore(item.getScoreValue())
                        System.out.println(name + " Collected Life item! HP : " + hp);
                    }
                }
            } else {
                addScore(item.getScoreValue());
                System.out.println(name + " collected " + item.getItemType() + "!");
            }
        }

    }

    private void addScore(long scoreValue) {
    }

    @Override
    public void update(float delta) {
        if (Gdx.input != null) {
            // TODO: Cek input W / UP   → y += speed * delta
            if(Gdx.input.isKeyPressed(W) ){
                y += speed * delta
            }
            // TODO: Cek input S / DOWN → y -= speed * delta
            if(Gdx.input.isKeyPressed(S)  ){
                y -= speed * delta
            }
            // TODO: Cek input A / LEFT → x -= speed * delta
            if(Gdx.input.isKeyPressed(A)  ){
                x += speed * delta
            }

            // TODO: Cek input D / RIGHT → x += speed * delta
            if(Gdx.input.isKeyPressed(D)  ){
                x -= speed * delta
            }
        }
    }
    @Override
    public void onCollision(Collidable other) {
        // TODO: Cek apakah other yang diterima method ini adalah Item
        // TODO: Cetak "Player touches items" lalu panggil collectItem((Item) other)
        System.out.println("Player touches items");
        collectItem((Item)other);

    // Encapsulation getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    public int getSpellCards() { return spellCards; }
    public void setSpellCards(int spellCards) { this.spellCards = spellCards; }

    public long getScore() { return score; }

}
