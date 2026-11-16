
package com.fikri.frontend.objects.Systems;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.enemies.Fairy;
import com.fikri.frontend.objects.items.Item;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Systems.BulletManager;
import com.fikri.frontend.objects.Systems.EntityFactory;
import com.fikri.frontend.states.FairyWaveState;
import com.fikri.frontend.states.WaveState;

import java.util.ArrayList;
import java.util.List;

public class LevelWaveManager {
    private List<GameObject> entities;
    private BulletManager bulletManager;
    private Player player;
    private Boss boss;
    private Fairy fairy;
    private List<Fairy> fairies = new ArrayList<>();

    private WaveState currentState;

    // Constructor
    public LevelWaveManager(List<GameObject> entities,
                            BulletManager bulletManager,
                            Player player) {
        this.entities = entities;
        this.bulletManager = bulletManager;
        this.player = player;
        setState(new FairyWaveState());
    }

    public LevelWaveManager(List<GameObject> entities,
                            BulletManager bulletManager,
                            Boss boss,
                            Fairy fairy) {
        this.entities = entities;
        this.bulletManager = bulletManager;
        this.boss = boss;
        this.fairy = fairy;

        if (fairy != null) {
            fairies.add(fairy);
        }

        this.currentState = null;
    }

    // Core Methods
    public void setState(WaveState newState) {
        currentState = newState;

        if (currentState != null) {
            currentState.onEnter(this);
        }
    }

    public void update(float delta) {
        if (currentState != null) {
            currentState.update(this, delta);
        }
    }

    // Helper
    public Fairy spawnFairy(float x, float y, String name, int hp) {
        Fairy f = EntityFactory.createFairy(
            x, y, name, hp, "fairy_idle_blue"
        );

        if (entities != null && !entities.contains(f)) {
            entities.add(f);
        }

        if (!fairies.contains(f)) {
            fairies.add(f);
        }

        this.fairy = f;
        return f;
    }

    public Boss spawnBoss(float x, float y, String name, int hp) {
        Boss b = EntityFactory.createBoss(x, y, name, hp);

        if (entities != null && !entities.contains(b)) {
            entities.add(b);
        }

        this.boss = b;
        return b;
    }

    public Item spawnItem(float x, float y, ItemType type) {
        Item item = EntityFactory.createItem(x, y, type);

        if (entities != null && !entities.contains(item)) {
            entities.add(item);
        }

        return item;
    }

    public boolean areAllFairiesDefeated() {
        if (fairies == null || fairies.isEmpty()) {
            return true;
        }

        for (Fairy f : fairies) {
            if (f != null && !f.isDestroyed()) {
                return false;
            }
        }

        return true;
    }

    // Getter
    public List<GameObject> getEntities() { return entities; }
    public BulletManager getBulletManager() { return bulletManager; }
    public Player getPlayer() { return player; }
    public Boss getBoss() { return boss; }
    public Fairy getFairy() { return fairy; }
    public List<Fairy> getFairies() { return fairies; }
    public WaveState getCurrentState() { return currentState; }
}
