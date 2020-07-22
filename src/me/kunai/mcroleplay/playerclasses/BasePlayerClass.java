package me.kunai.mcroleplay.playerclasses;

import me.kunai.mcroleplay.PlayerClass;
import org.bukkit.entity.Player;

public abstract class BasePlayerClass {

    private Player player;
    private PlayerClass playerClass;

    public BasePlayerClass(Player player, PlayerClass pClass) {
        this.player = player;
        this.playerClass = pClass;
    }

    public abstract void triggerLevel(int level);

    public Player getPlayer() {
        return this.player;
    }
    public PlayerClass getPlayerClass() {
        return this.playerClass;
    }
}
