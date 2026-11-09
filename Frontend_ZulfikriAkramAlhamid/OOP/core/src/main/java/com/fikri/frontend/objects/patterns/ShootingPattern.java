package com.fikri.frontend.objects.patterns;

import com.fikri.frontend.objects.Systems.BulletManager;

public interface ShootingPattern {
    void execute(float originX, float originY, com.fikri.frontend.objects.Systems.BulletManager bulletManager, boolean isPlayer);
}

