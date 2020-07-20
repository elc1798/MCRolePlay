package me.kunai.mcroleplay.playerclasses;

import me.kunai.mcroleplay.PlayerClass;
import org.bukkit.entity.Player;

public abstract class BasePlayerClass {

    public abstract PlayerClass getPlayerClass();
    public abstract void triggerLevel(Player player, int level);

}
