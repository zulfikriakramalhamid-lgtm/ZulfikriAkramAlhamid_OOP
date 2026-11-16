
package com.fikri.frontend.states;

import com.fikri.frontend.objects.Systems.LevelWaveManager;

public interface WaveState {
    void onEnter(LevelWaveManager manager);

    void update(LevelWaveManager manager, float delta);
}
