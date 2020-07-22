package me.kunai.mcroleplay.playerclasses;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.kunai.mcroleplay.utils.ItemUtils.nameItem;

public class PaladinClass extends BasePlayerClass {

    public PaladinClass(Player player) {
        super(player, PlayerClass.Paladin);
    }

    @Override
    public void triggerLevel(int level) {
        switch (level) {
            case 1:
                // Give player starting gear
                super.getPlayer().getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                super.getPlayer().getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                super.getPlayer().getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                super.getPlayer().getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

                ItemStack smiteSword = new ItemStack(Material.IRON_SWORD);
                smiteSword.addEnchantment(Enchantment.DAMAGE_UNDEAD, 1);
                nameItem(smiteSword, "Paladin's Honor");

                super.getPlayer().getInventory().addItem(
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
