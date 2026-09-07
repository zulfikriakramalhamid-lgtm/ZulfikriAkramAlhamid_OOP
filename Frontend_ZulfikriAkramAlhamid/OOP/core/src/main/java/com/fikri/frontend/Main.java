package com.netlab.frontend;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.fikri.frontend.GameObject;
import com.fikri.frontend.Item;
import com.fikri.frontend.Player;


public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;





    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        gameObjects = new ArrayList<>();


        Player player = new Player(280, 40, "Reimu hakurei", 100, 15, 3);

        Fairy fairy = new Fairy(150, 380, "Stage 1 Fairy", 20);

        Boss boss = new Boss(380, 400, "Cirno (Stage 2 Boss)", 150);

        Item pointItem1 = new Item(200, 450,  12, 12, 120f, "Point Item", 1000L);
        Item pointItem2 = new Item(300, 550,  12, 12, 100f, "Point Item", 1000L);
        Item pointItem3 = new Item(400, 550,  12, 12, 80f, "Point Item", 1000L);


        List<GameObject> gameObject = new ArrayList<>();
        gameObject.add(player);
        gameObject.add(fairy);
        gameObject.add(boss);
        gameObject.add(pointItem1);
        gameObject.add(pointItem2);
        gameObject.add(pointItem3);

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 1. Polymorphic Update Loop: Items move downward automatically via Item.update(delta)
        for (GameObject obj : gameObjects) {
            obj.update(delta);
        }

        // 2. Clear Screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 3. Polymorphic Render Loop: Draw hitboxes with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (GameObject obj : gameObjects) {
            obj.render(shapeRenderer);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
