
package com.fikri.frontend.objects.Systems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.BulletType;
import com.fikri.frontend.objects.bullets.Bullet;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.enemies.Enemy;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;

public class EntityFactory {

    public static Player createPlayer(
        float x, float y, String name, int hp, int power, int spellCards
    ) {
        Player player = new Player(x, y, name, hp, power, spellCards);
        Animation<TextureRegion> anim =
            AssetManager.getInstance().getAnimation("player_idle");
        player.setAnimation(anim);
        return player;
    }

    public static Boss createBoss(float x, float y, String name, int hp) {
        Boss boss = new Boss(x, y, name, hp);
        Animation<TextureRegion> idleAnim =
            AssetManager.getInstance().getAnimation("boss_idle");
        boss.setAnimation(idleAnim);
        return boss;
    }

    // Enemy biasa menggunakan animasi Fairy merah yang sudah tersedia.
    public static Enemy createEnemy(float x, float y, String name, int hp) {
        Enemy enemy = new Enemy(x, y, name, hp);
        Animation<TextureRegion> idleAnim =
            AssetManager.getInstance().getAnimation("fairy_idle_red");
        enemy.setAnimation(idleAnim);
        return enemy;
    }

    public static Fairy createFairy(float x, float y, String name, int hp) {
        Fairy fairy = new Fairy(x, y, name, hp);
        Animation<TextureRegion> idleAnim =
            AssetManager.getInstance().getAnimation("fairy_idle_red");
        fairy.setAnimation(idleAnim);
        return fairy;
    }

    public static Fairy createFairy(
        float x, float y, String name, int hp, String keyString
    ) {
        Fairy fairy = new Fairy(x, y, name, hp);
        Animation<TextureRegion> idleAnim =
            AssetManager.getInstance().getAnimation(keyString);
        fairy.setAnimation(idleAnim);
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

        TextureRegion sprite =
            AssetManager.getInstance().getTextureRegion(key);
        item.setSprite(sprite);
        return item;
    }

    public static Bullet createEnemyBullet(
        float x, float y, int damage, String spriteKey
    ) {
        String key = (spriteKey != null)
            ? spriteKey : "bullet_danmaku";

        TextureRegion sprite =
            AssetManager.getInstance().getTextureRegion(key);

        Bullet bullet = new Bullet(
            x, y, 0f, BulletType.DANMAKU, damage
        );

        bullet.setSprite(sprite);
        return bullet;
    }

    public static Bullet createEnemyBullet(
        float x, float y, int damage
    ) {
        return createEnemyBullet(x, y, damage, "bullet_danmaku");
    }

    public static Bullet createPlayerBullet(
        float x, float y, int damage, String spriteKey
    ) {
        TextureRegion sprite =
            AssetManager.getInstance().getTextureRegion(spriteKey);

        Bullet bullet = new Bullet(
            x, y, BulletType.AMULET, damage
        );

        bullet.setSprite(sprite);
        return bullet;
    }

    public static Bullet createPlayerBullet(
        float x, float y, int damage
    ) {
        return createPlayerBullet(x, y, damage, "bullet_amulet");
    }
}
