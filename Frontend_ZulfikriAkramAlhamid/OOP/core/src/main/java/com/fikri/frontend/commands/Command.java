package com.fikri.frontend.commands;

import com.fikri.frontend.objects.Player;
import com.fikri.frontend.objects.Systems.BulletManager;

public interface Command {
    void execute(Player player, BulletManager bulletManager);
}
