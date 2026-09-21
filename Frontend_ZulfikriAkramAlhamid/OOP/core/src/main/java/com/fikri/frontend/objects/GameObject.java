package com.fikri.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject implements Collidable {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float speed;
    protected Color color;

    // Soal 5: Attribute active
    protected boolean active = true;

    public GameObject(float x, float y, float width, float height, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    public void update(float delta) {
    }

    // Soal 6: Method isDestroyed() dan destroy()
    public boolean isDestroyed() {
        return !this.active;
    }

    public void destroy() {
        this.active = false;
    }

    // Soal 7: Update render() dengan pengecekan active
    public void render(ShapeRenderer shapeRenderer) {
        if (shapeRenderer != null && color != null && this.active) {
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    // Soal 8: Method isOffScreen() dengan margin 50px
    public boolean isOffScreen(float screenWidth, float screenHeight) {
        return (this.x < -50f || this.x > screenWidth + 50f ||
            this.y < -50f || this.y > screenHeight + 50f);
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getWidth() { return width; }
    public void setWidth(float width) {
        if (width > 0) this.width = width;
    }

    public float getHeight() { return height; }
    public void setHeight(float height) {
        if (height > 0) this.height = height;
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) {
        if (speed >= 0) this.speed = speed;
    }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(x - 10, y - 10, width + 20, height + 20);
    }

    @Override
    public void onCollision(Collidable other) {
    }
}
