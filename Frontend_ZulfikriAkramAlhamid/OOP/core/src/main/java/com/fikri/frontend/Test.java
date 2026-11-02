
package com.fikri.frontend;

import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.enemies.Enemy;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Systems.BulletManager;

public class Test {

    public static void main(String[] args) {

        System.out.println(
            "=== TOUHOU OOP PRACTICUM ==="
        );

        // MODULE 1: BASIC CLASSES

        System.out.println("\n=== MODULE 1 ===");

        Player reimu = new Player(
            200, 50, "Reimu Hakurei", 100, 15, 3
        );

        Enemy fairyBoss = new Enemy(
            "Cirno (Stage 2 Boss)", 50
        );

        System.out.println("\n--- Initial Battle State ---");

        System.out.println(
            "Player: " + reimu.getName()
                + " | HP: " + reimu.getHp()
                + " | Power: " + reimu.getPower()
                + " | SpellCards: " + reimu.getSpellCards()
        );

        System.out.println(
            "Enemy: " + fairyBoss.getName()
                + " | HP: " + fairyBoss.getHp()
        );

        System.out.println("\n--- Player Attacks ---");

        fairyBoss.takeDamage(25);
        fairyBoss.takeDamage(25);

        System.out.println("\n--- Enemy Counter Attack ---");

        fairyBoss.attack(reimu, 30);

        System.out.println("\nPlayer HP: " + reimu.getHp());


        // MODULE 2: INHERITANCE AND ENCAPSULATION

        System.out.println("\n=== MODULE 2 ===");

        Player reimu2 = new Player(
            200, 50, "Reimu Hakurei", 100, 15, 3
        );

        Fairy fairy = new Fairy(
            "Stage 1 Fairy", 20
        );

        Boss cirno = new Boss(
            "Cirno (Stage 2 Boss)", 150
        );

        Item pointItem = new Item(
            200, 450, 12, 12, 120f,
            ItemType.POINT, 1000L
        );

        System.out.println("\n--- Object Information ---");

        System.out.println(
            "Player: " + reimu2.getName()
                + " | Position: ("
                + reimu2.getX() + ", " + reimu2.getY() + ")"
        );

        System.out.println(
            "Fairy: " + fairy.getName()
                + " | Defeat Worth: "
                + fairy.getScoreValue()
        );

        System.out.println(
            "Boss: " + cirno.getName()
                + " | Defeat Worth: "
                + cirno.getScoreValue()
        );

        System.out.println(
            "Item: " + pointItem.getItemType()
                + " | Value: "
                + pointItem.getScoreValue()
        );

        System.out.println("\n--- Item Movement ---");

        System.out.println(
            "Initial Item Y: " + pointItem.getY()
        );

        pointItem.update(0.5f);

        System.out.println(
            "Item Y after 0.5s: " + pointItem.getY()
        );

        System.out.println("\n--- Scoring System ---");

        System.out.println(
            "Initial Score: " + reimu2.getScore()
        );

        reimu2.collectItem(pointItem);

        System.out.println(
            "Final Score: " + reimu2.getScore()
        );


        // MODULE 4: BULLET SYSTEM

        System.out.println("\n=== MODULE 4 ===");

        Player reimuModule4 = new Player(
            200, 50, "Reimu Hakurei", 100, 15, 3
        );

        BulletManager bulletManager = new BulletManager();

        reimuModule4.shootBullet(bulletManager);

        System.out.println(
            "Active Player Bullets: "
                + bulletManager.getActivePlayerBullets().size()
        );

        bulletManager.update(0.1f);

        if (!bulletManager.getActivePlayerBullets().isEmpty()) {

            System.out.println(
                "Bullet Y after update: "
                    + bulletManager.getActivePlayerBullets()
                    .get(0).getY()
            );
        }

        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }
}
