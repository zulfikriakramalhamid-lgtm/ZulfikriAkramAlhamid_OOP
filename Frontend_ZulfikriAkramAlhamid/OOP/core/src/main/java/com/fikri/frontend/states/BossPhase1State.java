package com.fikri.frontend.states;

import com.fikri.frontend.objects.GameObject;
import com.fikri.frontend.objects.enemies.Boss;
import com.fikri.frontend.objects.patterns.entityStrategy.EntityMovementPattern;

import java.util.List;
import java.util.function.Supplier;

public class BossPhase1State extends NonSpellState {
    @Override
    protected String getPhaseName() {
        return "Rumia Non-Spell 1 (ECL Sub12-15)";
    }

    @Override
    protected int getPhaseLife() {
        return 7000;
    }

    @Override
    protected int getLifeThreshold() {
        return 900;
    }

    @Override
    protected List<Supplier<Attack>> createAttacks(GameObject player) {
        return RumiaAttacks.nonSpell1(player);
    }

    @Override
    protected float getTimeoutSeconds() {
        return 35f;
    }

    @Override
    protected float getIntroSeconds() {
        return 1f;
    }

    @Override
    protected boolean isCollidableDuringIntro() {
        return false;
    }

    @Override
    protected EntityMovementPattern createIntroMovement(Boss boss) {
        return BossGlide.home(boss, getIntroSeconds());
    }

    @Override
    protected WaveState createNextState() {
        return new BossPhase2State();
    }
}
