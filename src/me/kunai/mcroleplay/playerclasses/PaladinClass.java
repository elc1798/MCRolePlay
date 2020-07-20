package me.kunai.mcroleplay.playerclasses;

import me.kunai.mcroleplay.PlayerClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PaladinClass extends BasePlayerClass {
    @Override
    public PlayerClass getPlayerClass() {
        return PlayerClass.Paladin;
    }

    @Override
    public void triggerLevel(Player player, int level) {
        switch (level) {
            case 1:
                // Give player starting gear
                player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

                ItemStack smiteSword = new ItemStack(Material.IRON_SWORD);
                smiteSword.addEnchantment(Enchantment.DAMAGE_UNDEAD, 1);
                if (smiteSword.hasItemMeta()) {
                    ItemMeta meta = smiteSword.getItemMeta();
                    meta.setDisplayName("Paladin's Honor");
                    smiteSword.setItemMeta(meta);
                }

                player.getInventory().addItem(
                        smiteSword,
                        new ItemStack(Material.STONE_PICKAXE),
                        new ItemStack(Material.IRON_AXE),
                        new ItemStack(Material.STONE_SHOVEL),
                        new ItemStack(Material.STONE_HOE)
                );
            default:
        }
    }
}
