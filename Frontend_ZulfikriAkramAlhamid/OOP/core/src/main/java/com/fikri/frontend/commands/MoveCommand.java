package com.fikri.frontend.commands;

import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.Systems.BulletManager;

public class MoveCommand implements Command {

    private float dx;
    private float dy;

    public MoveCommand(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void set(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.move(dx, dy);
        }
    }
}
