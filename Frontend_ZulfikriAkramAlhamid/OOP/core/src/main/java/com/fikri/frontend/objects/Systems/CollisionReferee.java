package com.fikri.frontend.objects.Systems;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.enemies.Enemy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;

import java.util.ArrayList;
import java.util.List;

public class CollisionReferee {

    public void resolveCollisions(
        Player player,
        List<GameObject> entities,
        BulletManager bulletManager
    ) {
        if (player == null || entities == null || bulletManager == null) {
            return;
        }

        List<GameObject> newDrops = new ArrayList<>();

        // 2. Mediator checks Enemy Bullets vs Player (Core Hitbox vs Graze Hitbox)


    // 1. Player Bullets vs Enemies
        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {
            if (bullet.isDestroyed()) continue;

            for (GameObject entity : entities) {
                if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {
                    if (bullet.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {

                        boolean defeated = enemy.takeDamage(bullet.getDamage());

                        if (defeated) {
                            player.addScore(enemy.getScoreValue());

                            Item powerDrop = EntityFactory.createItem(
                                enemy.getX(),
                                enemy.getY(),
                                ItemType.POWER
                            );

                            newDrops.add(powerDrop);
                        }

                        bullet.destroy();
                        break;
                    }
                }
            }
        }

        // 2. Enemy Bullets vs Player
        for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {
            if (bullet.isDestroyed()) continue;
            if (player.isDestroyed()) break;

            // Direct Core Hit
            if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {

                if (!player.isInvulnerable()) {
                    System.out.println(
                        "[CollisionReferee] CORE HIT! Enemy Bullet hit "
                            + player.getName() + " for "
                            + bullet.getDamage() + " DMG!"
                    );

                    player.takeDamage(100, bulletManager, entities);

                    bullet.destroy();
                    break;
                }

            } else if (
                !bullet.hasBeenGrazed() &&
                    bullet.getCoreHitbox().overlaps(player.getGrazeHitbox())
            ) {
                // Graze
                bullet.setGrazed(true);
                player.addGraze();

                System.out.println(
                    "[CollisionReferee] GRAZE! "
                        + player.getName()
                        + " grazed an enemy bullet (+50 pts)!"
                );
            }
        }

        // 3. Player vs Items and Enemies
        for (GameObject entity : entities) {
            if (entity.isDestroyed()) continue;

            if (entity instanceof Item item && !item.isCollected()) {

                if (
                    item.getCoreHitbox().overlaps(player.getCoreHitbox()) ||
                        item.getCoreHitbox().overlaps(player.getGrazeHitbox())
                ) {
                    player.collectItem(item);
                }

            } else if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {

                if (
                    player.getCoreHitbox().overlaps(enemy.getCoreHitbox()) &&
                        enemy.canCollide()
                ) {
                    if (!player.isInvulnerable()) {
                        System.out.println(
                            "[CollisionReferee] CORE COLLISION! Player collided with "
                                + enemy.getName() + "!"
                        );

                        player.takeDamage(100, bulletManager, entities);
                        enemy.resetCollisionCooldown();
                    }
                }
            }
        }

        entities.addAll(newDrops);
    }
}
