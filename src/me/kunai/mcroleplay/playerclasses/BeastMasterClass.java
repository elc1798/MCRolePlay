package me.kunai.mcroleplay.playerclasses;

import me.kunai.mcroleplay.PlayerClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BeastMasterClass extends BasePlayerClass {
    @Override
    public PlayerClass getPlayerClass() {
        return PlayerClass.Beastmaster;
    }

    @Override
    public void triggerLevel(Player player, int level) {
        switch (level) {
            case 1:
                // Give player starting gear
                player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));

                player.getInventory().addItem(
                        new ItemStack(Material.WOLF_SPAWN_EGG, 1),
                        new ItemStack(Material.BONE, 64),
                        new ItemStack(Material.STONE_SWORD),
                        new ItemStack(Material.STONE_PICKAXE),
                        new ItemStack(Material.STONE_AXE),
                        new ItemStack(Material.STONE_SHOVEL),
                        new ItemStack(Material.STONE_HOE)
                );
            default:
        }
    }
}
