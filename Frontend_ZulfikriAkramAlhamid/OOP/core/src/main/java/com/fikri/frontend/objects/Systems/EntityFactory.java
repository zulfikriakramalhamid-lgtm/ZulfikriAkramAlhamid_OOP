package com.fikri.frontend.objects.Systems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.BulletType;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;

public class EntityFactory {

    public static Player createPlayer(float x, float y, String name, int hp, int power, int spellCards) {
        Player player = new Player(x, y, name, hp, power, spellCards);
        Animation<TextureRegion> anim = AssetManager.getInstance().getAnimation("player_idle");
        player.setAnimation(anim);
        return player;
    }

    public static Boss createBoss(float x, float y, String name, int hp) {

        // 1. Buat Boss baru dengan x, y, name, dan hp dari parameter;
        Boss boss = new Boss(x,y,name,hp);
        //    simpan pada variabel lokal bernama `boss`.
        // 2. Ambil animasi "boss_idle" melalui getAnimation(...)
        Animation<TextureRegion>idleAnim = AssetManager.getInstance().getAnimation("Boss_idle");
        //    dari AssetManager.getInstance(). Simpan hasilnya pada
        //    variabel lokal bernama `idleAnim`.
        // 3. Pasang idleAnim pada boss melalui boss.setAnimation(...).
        boss.setAnimation(idleAnim);
        // 4. Kembalikan boss.
        return boss;
    }

    public static Fairy createFairy(float x, float y, String name, int hp) {
        // TODO:
        // 1. Buat Fairy baru
        Fairy fairy = new Fairy(x,y,name,hp);
        // 2. Ambil animasi "fairy_idle_red"
        Animation<TextureRegion>idleAnim = AssetManager.getInstance().getAnimation("Fairy_idle_red");
        fairy.setAnimation(idleAnim);
        // 3. Pasang idleAnim pada fairy
        fairy.setAnimation(idleAnim);
        // 4. Kembalikan fairy
        return fairy;
    }


    public static Fairy createFairy(float x, float y, String name, int hp, String keyString) {
        // TODO:
        // 1. Buat Fairy baru
        Fairy fairy = new Fairy(x, y, name, hp);
        // 2. Ambil animasi untuk keyString
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation(keyString);
        // 3. Pasang idleAnim pada fairy
        fairy.setAnimation(idleAnim);
        // 4. Kembalikan fairy
        return fairy;
    }

    public static Item createItem(float x, float y, ItemType itemType) {
        Item item = new Item(x, y, itemType);
        String key = switch (itemType) {
            case POWER -> "item_power";
            case POINT -> "item_point";
            case BOMB -> "item_bomb";
            case LIFE -> "item_life";
        };
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion(key);
        item.setSprite(sprite);
        return item;
    }

    public static Bullet createEnemyBullet(float x, float y, int damage) {
        Bullet bullet = new Bullet(x, y, 0f, BulletType.DANMAKU, damage);
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion("bullet_danmaku");
        bullet.setSprite(sprite);
        return bullet;
    }

    // Part III — Soal 3 Langkah 3: Factory Player Bullet Custom Sprite
    public static Bullet createPlayerBullet(float x, float y, int damage, String spriteKey) {
        // TODO 1: Ambil TextureRegion untuk spriteKey
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion(spriteKey);
        // TODO 2: Buat Bullet baru
        Bullet bullet = new Bullet(x, y, BulletType.AMULET, damage);
        // TODO 3: Pasang sprite pada bullet
        bullet.setSprite(sprite);
        // TODO 4: Kembalikan bullet
        return bullet;
    }

    // Part III — Soal 3 Langkah 3: Factory Player Bullet Default Amulet
    public static Bullet createPlayerBullet(float x, float y, int damage) {
        // TODO 5: Kembalikan hasil call function createPlayerBullet sebelumnya
        return createPlayerBullet(x, y, damage, "bullet_amulet");
    }
}
