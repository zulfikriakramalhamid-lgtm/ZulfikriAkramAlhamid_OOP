package com.fikri.frontend.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.fikri.frontend.objects.Collidable;
import com.fikri.frontend.objects.GameObject;

public class Item extends GameObject {
    private ItemType itemType;
    private String itemTypeName;
    private boolean collected = false;
    private long scoreValue = 100;

    public Item(String itemTypeName) {
        super(0, 0, 16, 16, 0f, Color.YELLOW);
        initItemType(itemTypeName);
    }

    public Item(String itemTypeName, long scoreValue) {
        super(0, 0, 16, 16, 0f, Color.YELLOW);
        initItemType(itemTypeName);
        this.scoreValue = scoreValue;
    }

    public Item(float x, float y, ItemType itemType) {
        super(x, y, 16, 16, 0f, Color.YELLOW);
        this.itemType = itemType;
        this.itemTypeName = itemType != null ? itemType.name() : "UNKNOWN";
        this.scoreValue = itemType != null ? itemType.getScoreValue() : 100;
    }

    public Item(float x, float y, String itemTypeName) {
        super(x, y, 16, 16, 0f, Color.YELLOW);
        initItemType(itemTypeName);
    }

    public Item(float x, float y, float width, float height, float speed, ItemType itemType, long scoreValue) {
        super(x, y, width, height, speed, Color.YELLOW);
        this.itemType = itemType;
        this.itemTypeName = itemType != null ? itemType.name() : "UNKNOWN";
        this.scoreValue = scoreValue;
    }

    public Item(float x, float y, float width, float height, float speed, String itemTypeName, long scoreValue) {
        super(x, y, width, height, speed, Color.YELLOW);
        initItemType(itemTypeName);
        this.scoreValue = scoreValue;
    }

    private void initItemType(String typeStr) {
        this.itemTypeName = typeStr;
        try {
            this.itemType = ItemType.valueOf(typeStr.toUpperCase());
            this.scoreValue = this.itemType.getScoreValue();
        } catch (Exception e) {
            this.itemType = null;
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public ItemType getItemTypeEnum() {
        return itemType;
    }

    public String getItemType() {
        return itemTypeName != null ? itemTypeName : (itemType != null ? itemType.name() : "UNKNOWN");
    }

    public long getScoreValue() {
        return scoreValue;
    }

    @Override
    public void update(float delta) {}

    @Override
    public void onCollision(Collidable other) {}
}
