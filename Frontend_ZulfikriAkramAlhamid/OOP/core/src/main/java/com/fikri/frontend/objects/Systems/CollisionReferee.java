
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

    private int defeatCounter = 0;
    private int itemDropCount = 0;

    public void resolveCollisions(
        Player player,
        List<GameObject> entities,
        BulletManager bulletManager
    ) {
        if (player == null || entities == null || bulletManager == null) {
            return;
        }

        List<GameObject> newDrops = new ArrayList<>();

        // 1. Player Bullets vs Enemies
        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {
            if (bullet.isDestroyed()) continue;

            for (GameObject entity : entities) {
                if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {

                    if (bullet.getShotHitbox().overlaps(enemy.getShotHitbox())) {

                        if (enemy.isDamageable()) {
                            boolean defeated = enemy.takeDamage(bullet.getDamage());

                            if (defeated) {
                                player.addScore(enemy.getScoreValue());

                                ItemType dropType = null;

                                if (enemy.getItemDrop() != null) {
                                    dropType = enemy.getItemDrop();
                                } else if (defeatCounter++ % 3 == 0) {
                                    dropType = (itemDropCount++ % 2 == 0)
                                        ? ItemType.POWER
                                        : ItemType.POINT;
                                }

                                if (dropType != null) {
                                    Item drop = EntityFactory.createItem(
                                        enemy.getX(),
                                        enemy.getY(),
                                        dropType
                                    );

                                    newDrops.add(drop);

                                    System.out.println(
                                        "[CollisionReferee] Defeated "
                                            + enemy.getName()
                                            + "! Dropped "
                                            + dropType
                                    );
                                }
                            }
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

            if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {

                if (!player.isInvulnerable()) {
                    System.out.println(
                        "[CollisionReferee] CORE HIT! Enemy Bullet hit "
                            + player.getName()
                            + " for "
                            + bullet.getDamage()
                            + " DMG!"
                    );

                    player.takeDamage(100, bulletManager, entities);
                    bullet.destroy();
                    break;
                }

            } else if (
                !bullet.hasBeenGrazed()
                    && bullet.getCoreHitbox().overlaps(player.getGrazeHitbox())
            ) {
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
                    item.getCoreHitbox().overlaps(player.getCoreHitbox())
                        || item.getCoreHitbox().overlaps(player.getGrazeHitbox())
                ) {
                    player.collectItem(item);
                }

            } else if (entity instanceof Enemy enemy && enemy.isCollidable()) {

                if (
                    player.getCoreHitbox().overlaps(enemy.getCoreHitbox())
                        && enemy.canCollide()
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
