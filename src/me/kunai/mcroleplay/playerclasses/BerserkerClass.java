package me.kunai.mcroleplay.playerclasses;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

import static me.kunai.mcroleplay.utils.ItemUtils.nameItem;

public class BerserkerClass extends BasePlayerClass {

    public BerserkerClass(Player player) {
        super(player, PlayerClass.Berserker);
    }

    @Override
    public void triggerLevel(int level) {
        switch (level) {
            case 1:
                // Give player starting gear
                super.getPlayer().getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                super.getPlayer().getInventory().getHelmet().addEnchantment(Enchantment.BINDING_CURSE, 1);
                nameItem(super.getPlayer().getInventory().getHelmet(), "Berserker's Headdress");

                super.getPlayer().getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                super.getPlayer().getInventory().getChestplate().addEnchantment(Enchantment.BINDING_CURSE, 1);
                ItemMeta chestMeta = super.getPlayer().getInventory().getChestplate().getItemMeta();
                ListMultimap<Attribute, AttributeModifier> m = ArrayListMultimap.create();
                m.put(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_MAX_HEALTH.name(), -4, AttributeModifier.Operation.ADD_NUMBER));
                m.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ATTACK_DAMAGE.name(), +3, AttributeModifier.Operation.ADD_NUMBER));
                chestMeta.setAttributeModifiers(m);
                chestMeta.setDisplayName("Berserker's Garb");
                super.getPlayer().getInventory().getChestplate().setItemMeta(chestMeta);

                super.getPlayer().getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                super.getPlayer().getInventory().getLeggings().addEnchantment(Enchantment.BINDING_CURSE, 1);
                nameItem(super.getPlayer().getInventory().getLeggings(), "Berserker's Loincloth");

                super.getPlayer().getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                super.getPlayer().getInventory().getBoots().addEnchantment(Enchantment.BINDING_CURSE, 1);
                nameItem(super.getPlayer().getInventory().getBoots(), "Berserker's Boots");

                ItemStack sword = new ItemStack(Material.IRON_SWORD);
                sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);

                super.getPlayer().getInventory().addItem(
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
