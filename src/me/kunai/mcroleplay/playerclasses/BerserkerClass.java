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

public class BerserkerClass extends BasePlayerClass {
    @Override
    public PlayerClass getPlayerClass() {
        return PlayerClass.Berserker;
    }

    @Override
    public void triggerLevel(Player player, int level) {
        switch (level) {
            case 1:
                // Give player starting gear
                player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                player.getInventory().getHelmet().addEnchantment(Enchantment.BINDING_CURSE, 1);
                player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));

                player.getInventory().getChestplate().addEnchantment(Enchantment.BINDING_CURSE, 1);
                ItemMeta chestMeta = player.getInventory().getChestplate().getItemMeta();
                ListMultimap<Attribute, AttributeModifier> m = ArrayListMultimap.create();
                m.put(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_MAX_HEALTH.name(), -4, AttributeModifier.Operation.ADD_NUMBER));
                chestMeta.setAttributeModifiers(m);
                player.getInventory().getChestplate().setItemMeta(chestMeta);

                player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                player.getInventory().getLeggings().addEnchantment(Enchantment.BINDING_CURSE, 1);
                player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                player.getInventory().getBoots().addEnchantment(Enchantment.BINDING_CURSE, 1);

                ItemStack sword = new ItemStack(Material.IRON_SWORD);
                sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);

                player.getInventory().addItem(
                        sword,
                        new ItemStack(Material.STONE_PICKAXE),
                        new ItemStack(Material.STONE_AXE),
                        new ItemStack(Material.STONE_SHOVEL),
                        new ItemStack(Material.STONE_HOE)
                );
            default:
        }
    }
}
