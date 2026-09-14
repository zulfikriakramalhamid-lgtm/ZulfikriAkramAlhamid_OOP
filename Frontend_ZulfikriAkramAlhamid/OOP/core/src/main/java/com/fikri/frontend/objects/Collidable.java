package com.fikri.frontend.objects;

import com.badlogic.gdx.math.Rectangle;
import com.fikri.frontend.objects.Player;
import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.GameObject;


public interface Collidable {
    Rectangle getCoreHitbox();
    Rectangle getGrazeHitbox();
    void onCollision(Collidable other);
}
