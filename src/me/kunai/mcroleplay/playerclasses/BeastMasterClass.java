package me.kunai.mcroleplay.playerclasses;

import me.kunai.mcroleplay.PlayerClass;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class BeastMasterClass extends BasePlayerClass {

    public BeastMasterClass(Player player) {
        super(player, PlayerClass.Beastmaster);
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
                        new ItemStack(Material.STONE_SWORD),
                        new ItemStack(Material.STONE_PICKAXE),
                        new ItemStack(Material.STONE_AXE),
                        new ItemStack(Material.STONE_SHOVEL),
                        new ItemStack(Material.STONE_HOE)
                );

                // Spawn a wolf
                spawnWolf("Pupper", DyeColor.BLUE);
            default:
        }
    }

    private void spawnWolf(String wolfName, DyeColor collarColor) {
        Wolf w = (Wolf) super.getPlayer().getWorld().spawnEntity(super.getPlayer().getLocation(), EntityType.WOLF);
        w.setAdult();
        w.setTamed(true);
        w.setOwner(super.getPlayer());
        w.setBreed(false);

        w.setCustomName(ChatColor.YELLOW + String.format("[%s]", super.getPlayer().getName()) + " " + ChatColor.GRAY + wolfName);
        w.setCustomNameVisible(true);

        w.setCollarColor(collarColor);
        w.setInvulnerable(true);
        w.setCanPickupItems(false);
        w.setGlowing(true);
    }
}
