package com.fikri.frontend.objects.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static AssetManager instance;

    private Map<String, TextureRegion> textureRegionMap;
    private Map<String, Animation<TextureRegion>> animationMap;
    private Map<String, Texture> textureMap;

    private AssetManager() {
        textureRegionMap = new HashMap<>();
        animationMap = new HashMap<>();
        textureMap = new HashMap<>();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public Texture loadTexture(String filename) {
        if (textureMap.containsKey(filename)) {
            return textureMap.get(filename);
        }

        String path = filename;
        if (Gdx.files != null && !Gdx.files.internal(path).exists()) {
            path = "core/assets/" + filename;
        }

        if (Gdx.files != null && Gdx.files.internal(path).exists()) {
            Texture tex = new Texture(Gdx.files.internal(path));
            textureMap.put(filename, tex);
            return tex;
        }

        return null;
    }

    public void registerRegion(String key, TextureRegion region) {
        textureRegionMap.put(key, region);
    }

    public void registerRegionFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int col) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);
            textureRegionMap.put(key, grid[row][col]);
        }
    }

    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int numFrames, float frameDuration) {
        registerAnimationFromSheet(key, filename, tileWidth, tileHeight, row, 0, numFrames, frameDuration, Animation.PlayMode.LOOP);
    }

    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int startCol, int numFrames, float frameDuration, Animation.PlayMode playMode) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);
            TextureRegion[] frames = new TextureRegion[numFrames];
            for (int i = 0; i < numFrames; i++) {
                frames[i] = grid[row][startCol + i];
            }
            Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            anim.setPlayMode(playMode);
            animationMap.put(key, anim);

            if (frames.length > 0) {
                textureRegionMap.put(key, frames[0]);
            }
        }
    }

    public TextureRegion getTextureRegion(String key) {
        return textureRegionMap.get(key);
    }

    public TextureRegion getRegion(String key) {
        return getTextureRegion(key);
    }

    public Animation<TextureRegion> getAnimation(String key) {
        return animationMap.get(key);
    }

    // === UBAH 1: Lengkapi method init() ===
    public void init() {
        // Mendaftarkan Animasi Player & Enemies
        registerAnimationFromSheet("player_idle", "player.png", 32, 48, 0, 0, 8, 0.125f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("player_left", "player.png", 32, 48, 1, 0, 4, 0.12f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("player_right", "player.png", 32, 48, 2, 0, 4, 0.12f, Animation.PlayMode.LOOP);

        registerAnimationFromSheet("boss_idle", "rumia.png", 64, 64, 0, 0, 4, 0.2f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("boss_left", "rumia.png", 64, 64, 1, 0, 4, 0.15f, Animation.PlayMode.REVERSED);
        registerAnimationFromSheet("boss_right", "rumia.png", 64, 64, 2, 0, 4, 0.15f, Animation.PlayMode.NORMAL);

        registerAnimationFromSheet("fairy_idle_red", "fairy.png", 32, 32, 1, 0, 8, 0.125f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("fairy_idle_blue", "fairy.png", 32, 32, 0, 0, 8, 0.125f, Animation.PlayMode.LOOP);

        // Mendaftarkan Region Bullet & Items
        registerRegionFromSheet("bullet_amulet", "amulet_reimu.png", 16, 16, 0, 0);
        registerRegionFromSheet("bullet_amulet_homing", "amulet_reimu.png", 16, 16, 1, 0);
        registerRegionFromSheet("bullet_danmaku", "bullets_small.png", 16, 16, 2, 3);

        registerRegionFromSheet("item_power", "items.png", 16, 16, 0, 0);
        registerRegionFromSheet("item_point", "items.png", 16, 16, 0, 1);
        registerRegionFromSheet("item_bomb", "items.png", 16, 16, 0, 3);
        registerRegionFromSheet("item_life", "items.png", 16, 16, 0, 5);
    }

    public void dispose() {
        for (Texture texture : textureMap.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        textureMap.clear();
        textureRegionMap.clear();
        animationMap.clear();
    }
}
