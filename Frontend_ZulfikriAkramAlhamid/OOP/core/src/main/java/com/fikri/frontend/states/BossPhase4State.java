package com.fikri.frontend.states;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.items.ItemType;
import com.fikri.frontend.objects.Systems.LevelWaveManager;

import java.util.List;
import java.util.function.Supplier;

public class BossPhase4State extends SpellCardState {
    @Override
    protected String getSpellName() {
        return "闇符「ディマーケイション」 (Darkness Sign 'Demarcation')";
    }

    @Override
    protected int getStartingLife() {
        return 800;
    }

    @Override
    protected boolean isFinalPhase() {
        return true;
    }

    @Override
    protected float getTimeoutSeconds() {
        return 25f;
    }

    @Override
    protected List<Supplier<Attack>> createAttackBlocks(GameObject player) {
        return List.of(
            () -> RumiaAttacks.demarcationRings(player),
            () -> RumiaAttacks.demarcationStreams(player)
        );
    }

    @Override
    protected void onSpellEnd(LevelWaveManager manager, Boss boss) {
        if (boss != null) {
            System.out.println("[BossPhase4State] STAGE 1 CLEARED!");
            manager.spawnItem(boss.getX() - 20f, boss.getY(), ItemType.POWER);
            manager.spawnItem(boss.getX(), boss.getY() + 10f, ItemType.BOMB);
            manager.spawnItem(boss.getX() + 20f, boss.getY(), ItemType.POINT);
        }
    }

    @Override
    protected WaveState createNextState() {
        return new StageClearState();
    }
}
