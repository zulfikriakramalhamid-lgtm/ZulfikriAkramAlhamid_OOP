package com.fikri.frontend.objects.patterns.entityStrategy;

import com.fikri.frontend.objects.GameObject;

public interface EntityMovementPattern {
    void move(GameObject entity, float delta);
}
