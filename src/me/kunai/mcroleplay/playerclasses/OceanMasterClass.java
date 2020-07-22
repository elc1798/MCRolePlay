package me.kunai.mcroleplay.playerclasses;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.kunai.mcroleplay.utils.ItemUtils.nameItem;

public class OceanMasterClass extends BasePlayerClass {

    public OceanMasterClass(Player player) {
        super(player, PlayerClass.Oceanmaster);
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

                ItemStack trident = new ItemStack(Material.TRIDENT);
                trident.addEnchantment(Enchantment.LOYALTY, 1);
                nameItem(trident, "Oceanmaster's Might");

                super.getPlayer().getInventory().addItem(
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
