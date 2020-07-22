package me.kunai.mcroleplay.playerclasses;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import me.kunai.mcroleplay.PlayerClass;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

import static me.kunai.mcroleplay.utils.ItemUtils.nameItem;

public class RangerClass extends BasePlayerClass {

    public RangerClass(Player player) {
        super(player, PlayerClass.Ranger);
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
                super.getPlayer().getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_FALL, 1);
                nameItem(super.getPlayer().getInventory().getBoots(), "Ranger's Nimbleness");
                ItemMeta bootMeta = super.getPlayer().getInventory().getBoots().getItemMeta();
                ListMultimap<Attribute, AttributeModifier> m = ArrayListMultimap.create();
                m.put(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_MOVEMENT_SPEED.name(), 0.2, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
                bootMeta.setAttributeModifiers(m);
                super.getPlayer().getInventory().getBoots().setItemMeta(bootMeta);

                super.getPlayer().getInventory().addItem(
                        new ItemStack(Material.BOW),
                        new ItemStack(Material.ARROW, 64),
                        new ItemStack(Material.STONE_PICKAXE),
                        new ItemStack(Material.STONE_AXE),
                        new ItemStack(Material.STONE_SHOVEL),
                        new ItemStack(Material.STONE_HOE),
                        new ItemStack(Material.FLINT_AND_STEEL)
                );
            default:
        }
    }

}
