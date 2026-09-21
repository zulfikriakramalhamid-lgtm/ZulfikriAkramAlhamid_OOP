package com.fikri.frontend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.bullets.Bullet;

public class Main extends ApplicationAdapter {

    private ShapeRenderer shapeRenderer;
    private List<GameObject> entities;
    private Player player;          // Referensi ke player agar bisa dipanggil shootBullet()
    private float shootTimer = 0f;  // Cooldown tembakan

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        entities = new ArrayList<>();

        // Player
        player = new Player(
            280, 40,
            "Reimu Hakurei",
            100, 15, 3
        );

        // Fairy
        Fairy fairy = new Fairy(
            150, 380,
            "Stage 1 Fairy",
            20
        );

        // Boss
        Boss boss = new Boss(
            380, 400,
            "Cirno (Stage 2 Boss)",
            150
        );

        // Items
        Item powerItem = new Item(200, 450, 16, 16, 80f, ItemType.POWER, 500L);
        Item pointItem = new Item(320, 480, 12, 12, 120f, ItemType.POINT, 1000L);

        // Add semua entity ke List<GameObject>
        entities.add(player);
        entities.add(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }
    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        // 1. Dapatkan Iterator<T> dari list yang diberikan.
        Iterator<T> iterator = list.iterator();
        while(iterator.hasNext()){
            T entity = iterator.next();
            entity.update(delta);
            if(entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()){
                System.out.println("Remove via Generic iterator : " + entity.getClass().getSimpleName());
                iterator.remove();
            }
        }
        // 2. Selama masih ada elemen berikutnya (hasNext()):
        //    a. Ambil elemen saat ini menggunakan next(), simpan ke variabel bertipe T.
        //    b. Panggil update(delta) pada elemen tersebut.
        //    c. Jika elemen tersebut isOffScreen(screenWidth, screenHeight) ATAU isDestroyed():
        //       - Tampilkan pesan: "Removed via Generic Iterator: " + [nama class entity, pakai getClass().getSimpleName()]
        //       - Hapus elemen ini dari list menggunakan method milik Iterator (BUKAN list.remove()!).
    }


    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // TODO 1: Jika tombol Z baru saja ditekan, tambahkan bullet baru hasil player.shootBullet() ke dalam list entities.
        // Clue: Gdx.input.isKeyJustPressed()
        if(Gdx.input != null && Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            Bullet newBullet = player.shootBullet();
            entities.add(newBullet);
        }

        // TODO 2: Panggil updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        // untuk meng-update sekaligus membersihkan entity yang destroyed/off-screen.
        updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 3. Collision detection antar entity (skip entity yang sudah destroyed)
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (!a.isDestroyed() && !b.isDestroyed()) {
                    if (a.getCoreHitbox().overlaps(b.getCoreHitbox())) {
                        a.onCollision(b);
                        b.onCollision(a);
                    }
                }
            }
        }

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (GameObject entity : entities) {
            // TODO 3: Gunakan if statement untuk mengecek apakah entity belum hancur (!entity.isDestroyed()).
            if (!entity.isDestroyed()){
                entity.render(shapeRenderer);
            }
            // kalo iya, panggil method entity.render(shapeRenderer);
        }
        shapeRenderer.end();
    }


    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
