package me.kunai.mcroleplay.playerclasses;

import me.kunai.mcroleplay.PlayerClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArtificerClass extends BasePlayerClass {

    public ArtificerClass(Player player) {
        super(player, PlayerClass.Artificer);
    }

    @Override
    public void triggerLevel(int level) {
        switch (level) {
            case 1:
                // Give player starting gear
                super.getPlayer().getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                super.getPlayer().getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                super.getPlayer().getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                super.getPlayer().getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));

                super.getPlayer().getInventory().addItem(
                        new ItemStack(Material.IRON_PICKAXE),
                        new ItemStack(Material.IRON_AXE),
                        new ItemStack(Material.STONE_SHOVEL),
                        new ItemStack(Material.STONE_HOE),
                        new ItemStack(Material.STONE_SWORD),
                        new ItemStack(Material.GRINDSTONE),
                        new ItemStack(Material.CRAFTING_TABLE)
                );
            default:
        }
    }
}
