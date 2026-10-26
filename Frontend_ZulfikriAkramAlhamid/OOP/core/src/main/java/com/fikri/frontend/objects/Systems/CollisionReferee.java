package com.fikri.frontend.objects.Systems;

import com.badlogic.gdx.Game;
import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.enemies.Enemy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CollisionReferee {
    public void resolveCollisions(Player player, List<GameObject> entities, BulletManager bulletManager) {
        if (player == null || entities == null || bulletManager == null) return;

        List<GameObject> newDrops = new ArrayList<>();

        // Logika tabrakan dibagi menjadi 3 tahap
        //
        // TODO: Kode Soal 3
        // 1. Evaluasi bullet Pemain vs Musuh
        // TODO:
        // - Lakukan loop untuk setiap bullet yang ada di dalam list ActivePlayerBullets.
        //   - Jika bullet itu sudah dihancurkan, lewati (continue)
        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {
            if (bullet.isDestroyed()) continue;
            //   - Lakukan loop untuk setiap entity di dalam entities: (HINT: variabel `entities` ada parameter di atas)
            //     - Cek apakah entity merupakan instance dari Enemy enemy dan belum hancur.
            //     - Jika ya, cek apakah Core Hitbox bullet bertabrakan dengan Core Hitbox musuh.
            //     - Jika bertabrakan:
            //       1. Beri damage ke musuh: boolean defeated = enemy.takeDamage(bullet.getDamage());
            //       2. Jika musuh terbunuh (defeated):
            //          - Tambahkan skor (scoreValue) musuh ke player.
            //          - Buat item drop POWER melalui factory di koordinat x, y musuh itu mati.
            //            lalu masukkan item tersebut ke dalam list newDrops
            //       3. Tandai bullet hancur agar nanti didaur ulang oleh BulletManager
                for (GameObject entity : entities) {
                    if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {
                        if (bullet.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {
                            boolean defeated = enemy.takeDamage(bullet.getDamage());
                            if (defeated) {
                                player.addScore(enemy.getScoreValue());
                                Item powerDrop = EntityFactory.createItem(enemy.getX(), enemy.getY(), ItemType.POWER);
                                newDrops.add(powerDrop);
                            }
                            bullet.destroy();
                            break;
                        }
                    }
                }
            }


        //
        // TODO: Kode Soal 4
// 2. Evaluasi bullet Musuh vs Pemain (Core Hit vs Graze Hit)
// TODO:
// - Lakukan loop untuk setiap bullet yang di dalam ActiveEnemyBullets.
//   - Jika bullet itu sudah dihancurkan, lewati (continue)
        for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {
            if (bullet.isDestroyed()) continue;
            if (!player.isDestroyed()) {

                //   - Cek jika player belum hancur (destroyed), maka:
//
//     - PRIORITAS 1: Core Hit (Pemain Tertembak)
//       - Cek apakah Core Hitbox bullet bertabrakan dengan Core Hitbox player.
//       - Jika YA:
//         * Berikan damage ke pemain: player.takeDamage(bullet.getDamage())
//         * Tandai bullet hancur/hancurkan bullet.
//
                if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {
                    player.takeDamage(bullet.getDamage());
                    bullet.destroy();
                    //
//     - PRIORITAS 2: Graze Hit (bullet Menyerempet Pemain)
//       - Cek jika bullet BELUM pernah di-graze
//         DAN Core Hitbox bullet menyentuh Graze Hitbox pemain.
//       - Jika YA:
//         * Tandai bullet sudah di-graze.
//         * Berikan bonus skor graze ke pemain: player.addScore(50)
                } else if (!bullet.hasBeenGrazed() && bullet.getCoreHitbox().overlaps(player.getGrazeHitbox())) {
                    bullet.setGrazed(true);
                    player.addScore(50);
                }
            }
        }

        // TODO: Kode Soal 5
        // 3. Evaluasi Pemain vs Item dan Kontak Fisik Musuh
        // TODO:
        // - Lakukan loop untuk setiap entity di dalam entities:
        //   - Jika entity itu sudah dihancurkan, lewati (continue)
        //
        //   - KASUS A: Pengumpulan Item
        //     - Cek apakah entity adalah instance dari Item item
        //     - Cek apakah Core Hitbox item bersentuhan dengan Core Hitbox ATAU Graze Hitbox pemain.
        //     - Jika YA: panggil player.collectItem(item)
        //
        //   - KASUS B: Kontak Fisik dengan Musuh
        //     - Cek apakah entity adalah instance dari Enemy enemy
        //     - Jika Core Hitbox pemain menabrak Core Hitbox musuh.
        //     - Jika YA: berikan damage tabrakan badan sebesar 10 ke pemain: player.takeDamage(10)

        // ...

        entities.addAll(newDrops);
    }

}









        for (GameObject entity : entities) {
            if (entity.isDestroyed()) continue;

            if (entity instanceof Item item && !item.isCollected()) {
                if (item.getCoreHitbox().overlaps(player.getCoreHitbox()) || item.getCoreHitbox().overlaps(player.getGrazeHitbox())) {
                    player.collectItem(item);
                }
            } else if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {
                if (player.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {
                    if (enemy.canCollide()) {
                        player.takeDamage(10);
                        enemy.resetCollisionCooldown();
                    }
                }
            }
        }


