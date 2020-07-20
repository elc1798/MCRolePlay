package me.kunai.mcroleplay.playerclasses;

import org.bukkit.entity.Player;

public abstract class BasePlayerClass {

    // Yes I know it should be `void triggerLevel(Player player, int level)`. But I designed each class to only scale
    // to level 6.
    public abstract void triggerLevel1(Player player);
    public abstract void triggerLevel2(Player player);
    public abstract void triggerLevel3(Player player);
    public abstract void triggerLevel4(Player player);
    public abstract void triggerLevel5(Player player);
    public abstract void triggerLevel6(Player player);

}
