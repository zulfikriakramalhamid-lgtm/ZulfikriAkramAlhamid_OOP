package com.fikri.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.fikri.frontend.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {
    private int keyShoot = Input.Keys.Z;
    private int keyBomb = Input.Keys.X;
    private int keyFocus = Input.Keys.SHIFT_LEFT;

    private ShootCommand shootCommand = new ShootCommand();
    private BombCommand bombCommand = new BombCommand();
    private FocusCommand focusOn = new FocusCommand(true);
    private FocusCommand focusOff = new FocusCommand(false);
    private final MoveCommand moveCommand = new MoveCommand(0, 0);

    private final List<Command> activeCommands = new ArrayList<>();

    public InputHandler() {}

    public InputHandler(List<GameObject> entities) {
        this.bombCommand.setEntities(entities);
    }

    public void setEntities(List<GameObject> entities) {
        this.bombCommand.setEntities(entities);
    }

    public int getKeyShoot() { return keyShoot; }
    public void setKeyShoot(int keyShoot) { this.keyShoot = keyShoot; }

    public int getKeyBomb() { return keyBomb; }
    public void setKeyBomb(int keyBomb) { this.keyBomb = keyBomb; }

    public int getKeyFocus() { return keyFocus; }
    public void setKeyFocus(int keyFocus) { this.keyFocus = keyFocus; }
    public List<Command> handleInput(float delta) {
        activeCommands.clear();
        if (Gdx.input == null) return activeCommands;

        // 1. Focus Mode Toggle (Shift Kiri atau Kanan) (3 poin)
        // TODO:
        // - Cek apakah tombol keyFocus atau SHIFT_RIGHT sedang ditekan (isKeyPressed).
        // - Simpan ke variabel boolean `isFocusHeld`.
        // - Tambahkan (add) ke activeCommands: jika isFocusHeld maka focusOn, jika tidak maka focusOff.
        boolean isFocusHeld =
            Gdx.input.isKeyPressed(keyFocus) ||
                Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

        activeCommands.add(isFocusHeld ? focusOn : focusOff );
        // 2. Pergerakan 8-Arah (WASD atau Arrow Keys) (5 poin)
        float dx = 0;
        float dy = 0;
        // TODO:
        // - Jika W atau UP ditekan (isKeyPressed), tambahkan dy sebesar 1
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)){
            dy += 1f; }
        // - Jika S atau DOWN ditekan (isKeyPressed), kurangi dy sebesar 1
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            dy -= 1f; }
        // - Jika A atau LEFT ditekan (isKeyPressed), kurangi dx sebesar 1
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            dx -= 1f; }
        // - Jika D atau RIGHT ditekan (isKeyPressed), tambahkan dx sebesar 1
        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            dx += 1f; }
        // - Hitung float speedMult = isFocusHeld ? 0.45f : 1.0f;
        float speedMult = isFocusHeld ? 0.45f : 1.0f;
        // - Perbarui perpindahan: moveCommand.set(dx * delta * speedMult, dy * delta * speedMult);
        moveCommand.set(dx * delta * speedMult, dy * delta * speedMult);
        // - Tambahkan moveCommand ke dalam activeCommands.
        activeCommands.add(moveCommand);
            if(Gdx.input.isKeyPressed(keyShoot)){
                activeCommands.add(shootCommand);
            }
            if(Gdx.input.isKeyJustPressed(keyBomb)){
                activeCommands.add(bombCommand);
            }
        return activeCommands;
    }
}
