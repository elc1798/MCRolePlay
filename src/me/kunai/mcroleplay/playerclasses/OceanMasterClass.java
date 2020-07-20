package me.kunai.mcroleplay.playerclasses;

import me.kunai.mcroleplay.PlayerClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OceanMasterClass extends BasePlayerClass {

    @Override
    public PlayerClass getPlayerClass() {
        return PlayerClass.Oceanmaster;
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

                ItemStack trident = new ItemStack(Material.TRIDENT);
                trident.addEnchantment(Enchantment.LOYALTY, 1);

                player.getInventory().addItem(
                        trident,
                        new ItemStack(Material.STONE_PICKAXE),
                        new ItemStack(Material.STONE_AXE),
                        new ItemStack(Material.STONE_SHOVEL),
                        new ItemStack(Material.STONE_HOE)
                );
            default:
        }
    }

}
