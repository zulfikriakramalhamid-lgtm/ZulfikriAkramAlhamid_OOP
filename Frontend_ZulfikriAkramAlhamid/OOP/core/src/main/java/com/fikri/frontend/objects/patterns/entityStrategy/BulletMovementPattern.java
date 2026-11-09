package com.fikri.frontend.objects.patterns.entityStrategy;

import com.fikri.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {
    void move(Bullet bullet, float delta);
}
