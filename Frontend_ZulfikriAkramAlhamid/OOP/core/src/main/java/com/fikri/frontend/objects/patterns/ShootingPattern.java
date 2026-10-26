package com.fikri.frontend.objects.patterns;

import com.fikri.frontend.objects.Systems.BulletManager;

public interface ShootingPattern {
    void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer);
}
