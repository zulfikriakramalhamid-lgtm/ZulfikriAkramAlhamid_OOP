package com.fikri.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject implements Collidable {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float speed;
    protected Color color;
    protected boolean active = true;

    // Field baru untuk visual grafis (Part III)
    protected TextureRegion sprite;
    protected Animation<TextureRegion> animation;
    protected float stateTime = 0f;

    public GameObject(float x, float y, float width, float height, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    public void update(float delta) {
        this.stateTime += delta; // Tambah waktu internal objek agar animasi bergerak
    }

    public boolean isDestroyed() {
        return !this.active;
    }

    public void destroy() {
        this.active = false;
    }

    // Render fallback berbentuk Shape / Kotak
    public void render(ShapeRenderer shapeRenderer) {
        if (shapeRenderer != null && color != null && this.active) {
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    // Overload render dengan SpriteBatch untuk Aset Grafis (Part III)
    public void render(SpriteBatch batch) {
        if (batch != null && active) {
            if (animation != null) {
                TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
                batch.draw(currentFrame, x, y, width, height);
            } else if (sprite != null) {
                batch.draw(sprite, x, y, width, height);
            }
        }
    }

    public boolean isOffScreen(float screenWidth, float screenHeight) {
        return (this.x < -50f || this.x > screenWidth + 50f ||
            this.y < -50f || this.y > screenHeight + 50f);
    }

    // Getter dan Setter Sprite & Animation
    public TextureRegion getSprite() { return sprite; }
    public void setSprite(TextureRegion sprite) { this.sprite = sprite; }

    public Animation<TextureRegion> getAnimation() { return animation; }
    public void setAnimation(Animation<TextureRegion> animation) { this.animation = animation; }

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
