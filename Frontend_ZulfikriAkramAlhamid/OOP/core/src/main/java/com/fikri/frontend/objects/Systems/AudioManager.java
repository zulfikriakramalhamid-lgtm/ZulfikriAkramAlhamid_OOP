package com.fikri.frontend.objects.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private static AudioManager instance;

    private Music bgm;
    private Sound sfxShoot;
    private Sound sfxHit;
    private Sound sfxItem;

    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void init() {

        String bgmPath = Gdx.files.internal("bell_avici.mp3").exists() ? "bell_avici.mp3" : "core/assets/bell_avici.mp3";
        if (Gdx.files.internal(bgmPath).exists()) {
            bgm = Gdx.audio.newMusic(Gdx.files.internal(bgmPath));
            bgm.setLooping(true);
            bgm.setVolume(0.4f);
        }


        String shootPath = Gdx.files.internal("freesound.mp3").exists() ? "freesound.mp3" : "core/assets/freesound.mp3";
        if (Gdx.files.internal(shootPath).exists()) {
            sfxShoot = Gdx.audio.newSound(Gdx.files.internal(shootPath));
        }

        String hitPath = Gdx.files.internal("universfie.mp3").exists() ? "universfie.mp3" : "core/assets/universfie.mp3";
        if (Gdx.files.internal(hitPath).exists()) {
            sfxHit = Gdx.audio.newSound(Gdx.files.internal(hitPath));
        }


        String itemPath = Gdx.files.internal("edr_power.mp3").exists() ? "edr_power.mp3" : "core/assets/edr_power.mp3";
        if (Gdx.files.internal(itemPath).exists()) {
            sfxItem = Gdx.audio.newSound(Gdx.files.internal(itemPath));
        }
    }

    public void playBgm() {
        if (bgm != null && !bgm.isPlaying()) {
            bgm.play();
        }
    }

    public void playShootSfx() {
        if (sfxShoot != null) sfxShoot.play(0.6f);
    }

    public void playHitSfx() {
        if (sfxHit != null) sfxHit.play(0.8f);
    }

    public void playItemSfx() {
        if (sfxItem != null) sfxItem.play(0.7f);
    }

    public void dispose() {
        if (bgm != null) bgm.dispose();
        if (sfxShoot != null) sfxShoot.dispose();
        if (sfxHit != null) sfxHit.dispose();
        if (sfxItem != null) sfxItem.dispose();
    }
}
