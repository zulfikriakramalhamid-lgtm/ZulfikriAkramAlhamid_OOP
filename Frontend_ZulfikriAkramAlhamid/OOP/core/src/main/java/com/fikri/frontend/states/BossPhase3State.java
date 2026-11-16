package com.fikri.frontend.states;

import com.fikri.frontend.objects.GameObject;

import java.util.List;
import java.util.function.Supplier;

public class BossPhase3State extends NonSpellState {
    @Override
    protected String getPhaseName() {
        return "Rumia Non-Spell 2 (ECL Sub18-21)";
    }

    @Override
    protected int getPhaseLife() {
        return 7500;
    }

    @Override
    protected int getLifeThreshold() {
        return 800;
    }

    @Override
    protected List<Supplier<Attack>> createAttacks(GameObject player) {
        return RumiaAttacks.nonSpell2(player);
    }

    @Override
    protected float getTimeoutSeconds() {
        return 30f;
    }

    @Override
    protected float getIntroSeconds() {
        return 200f / 60f;
    }

    @Override
    protected WaveState createNextState() {
        return new BossPhase4State();
    }
}
