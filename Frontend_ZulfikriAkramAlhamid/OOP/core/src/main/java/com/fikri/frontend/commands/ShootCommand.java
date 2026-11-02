package com.fikri.frontend.commands;

import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.Systems.BulletManager;

public class ShootCommand implements Command {
    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null && bulletManager != null) {
            player.shootBullet(bulletManager);
        }
    }
}
