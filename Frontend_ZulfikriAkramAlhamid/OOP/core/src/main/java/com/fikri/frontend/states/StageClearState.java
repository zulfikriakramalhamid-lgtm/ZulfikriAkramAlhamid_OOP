
package com.fikri.frontend.states;

import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.Systems.LevelWaveManager;

public class StageClearState implements WaveState {

    @Override
    public void onEnter(LevelWaveManager manager) {
        System.out.println("STAGE CLEAR!");

        Boss boss = manager.getBoss();

        if (boss != null && !boss.isDestroyed()) {
            boss.destroy();
        }
    }

    @Override
    public void update(LevelWaveManager manager, float delta) {
        // Stage sudah selesai, tidak perlu update apa pun.
    }
}
