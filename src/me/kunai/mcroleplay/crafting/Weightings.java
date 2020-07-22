package me.kunai.mcroleplay.crafting;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

class Weightings {

    static HashMap<Enchantment, List<Integer>> getWeightMapFor(ItemStack item, HashMap<Enchantment, List<Integer>> weightMap) {
        // Prune the weightmap based on the enchantments the item already has, and the item type.
        for (Map.Entry<Enchantment, List<Integer>> elem : weightMap.entrySet()) {
            List<Integer> weights = elem.getValue();

            if (!elem.getKey().canEnchantItem(item)) {
                weightMap.put(elem.getKey(), Collections.emptyList());
                continue;
            }

            if (item.containsEnchantment(elem.getKey())) {
                for (int i = 0; i < item.getEnchantmentLevel(elem.getKey()); i++) {
                    weights.set(i, 0);
                }
                weightMap.put(elem.getKey(), weights);
                continue;
            }

            for (Map.Entry<Enchantment, Integer> activeEnch : item.getEnchantments().entrySet()) {
                if (activeEnch.getKey().conflictsWith(elem.getKey())) {
                    weightMap.put(elem.getKey(), Collections.emptyList());
                }
            }
        }

        return weightMap;
    }

    static HashMap<Enchantment, List<Integer>> getAllWeights() {
        HashMap<Enchantment, List<Integer>> weightMap = new HashMap<>();
        weightMap.put(Enchantment.MENDING, Arrays.asList(1));
        weightMap.put(Enchantment.DURABILITY, Arrays.asList(50, 30, 10));

        weightMap.put(Enchantment.DIG_SPEED, Arrays.asList(50, 40, 30, 20, 10));
        weightMap.put(Enchantment.LOOT_BONUS_BLOCKS, Arrays.asList(2));
        weightMap.put(Enchantment.SILK_TOUCH, Arrays.asList(2));

        weightMap.put(Enchantment.DAMAGE_ARTHROPODS, Arrays.asList(50, 40, 30, 20, 10));
        weightMap.put(Enchantment.FIRE_ASPECT, Arrays.asList(30, 20));
        weightMap.put(Enchantment.DAMAGE_ALL, Arrays.asList(50, 40, 30, 20, 10));
        weightMap.put(Enchantment.KNOCKBACK, Arrays.asList(50, 30));
        weightMap.put(Enchantment.LOOT_BONUS_MOBS, Arrays.asList(20, 10, 5));
        weightMap.put(Enchantment.DAMAGE_UNDEAD, Arrays.asList(50, 40, 30, 20, 10));
        weightMap.put(Enchantment.SWEEPING_EDGE, Arrays.asList(30, 20, 10));

        weightMap.put(Enchantment.ARROW_FIRE, Arrays.asList(10));
        weightMap.put(Enchantment.ARROW_INFINITE, Arrays.asList(2));
        weightMap.put(Enchantment.ARROW_DAMAGE, Arrays.asList(50, 40, 30, 20, 10));
        weightMap.put(Enchantment.ARROW_KNOCKBACK, Arrays.asList(50, 30));

        weightMap.put(Enchantment.MULTISHOT, Arrays.asList(30));
        weightMap.put(Enchantment.PIERCING, Arrays.asList(50, 40, 30, 20));
        weightMap.put(Enchantment.QUICK_CHARGE, Arrays.asList(30, 15, 8));

        weightMap.put(Enchantment.WATER_WORKER, Arrays.asList(30));
        weightMap.put(Enchantment.OXYGEN, Arrays.asList(50, 40, 30));

        weightMap.put(Enchantment.DEPTH_STRIDER, Arrays.asList(30, 20, 10));
        weightMap.put(Enchantment.PROTECTION_FALL, Arrays.asList(40, 30, 20, 10));
        weightMap.put(Enchantment.FROST_WALKER, Arrays.asList(10, 5));
        weightMap.put(Enchantment.SOUL_SPEED, Arrays.asList(30, 20, 10));

        weightMap.put(Enchantment.PROTECTION_EXPLOSIONS, Arrays.asList(50, 40, 30, 20));
        weightMap.put(Enchantment.PROTECTION_FIRE, Arrays.asList(50, 40, 30, 20));
        weightMap.put(Enchantment.PROTECTION_PROJECTILE, Arrays.asList(50, 40, 30, 20));
        weightMap.put(Enchantment.PROTECTION_ENVIRONMENTAL, Arrays.asList(60, 50, 40, 30, 20, 1));
        weightMap.put(Enchantment.THORNS, Arrays.asList(30, 20, 10));

        weightMap.put(Enchantment.LURE, Arrays.asList(50, 40, 30));
        weightMap.put(Enchantment.LUCK, Arrays.asList(30, 20, 10));

        weightMap.put(Enchantment.IMPALING, Arrays.asList(50, 40, 30, 20, 10));
        weightMap.put(Enchantment.RIPTIDE, Arrays.asList(30, 20, 10));
        weightMap.put(Enchantment.LOYALTY, Arrays.asList(30, 20, 10));
        weightMap.put(Enchantment.CHANNELING, Arrays.asList(10));
        return weightMap;
    }

    static HashMap<Enchantment, List<Integer>> getTransmuteWeights(int artificerRank) {
        HashMap<Enchantment, List<Integer>> weightMap = new HashMap<>();

        weightMap.put(Enchantment.DURABILITY, Arrays.asList(100));
        weightMap.put(Enchantment.DIG_SPEED, Arrays.asList(100));
        weightMap.put(Enchantment.DAMAGE_ALL, Arrays.asList(40));
        weightMap.put(Enchantment.ARROW_DAMAGE, Arrays.asList(40));
        weightMap.put(Enchantment.PROTECTION_ENVIRONMENTAL, Arrays.asList(50));

        if (artificerRank >= 1) {
            weightMap.put(Enchantment.MENDING, Arrays.asList(1));
            weightMap.put(Enchantment.DURABILITY, Arrays.asList(100, 40));
            weightMap.put(Enchantment.DIG_SPEED, Arrays.asList(100, 40));
        }

        if (artificerRank >= 3) {
            weightMap.put(Enchantment.DAMAGE_ALL, Arrays.asList(40, 20));
            weightMap.put(Enchantment.LOOT_BONUS_BLOCKS, Arrays.asList(10));
            weightMap.put(Enchantment.LOOT_BONUS_MOBS, Arrays.asList(10));
            weightMap.put(Enchantment.SILK_TOUCH, Arrays.asList(5));
        }

        return weightMap;
    }

    static int getTotalWeight(Map<Enchantment, List<Integer>> weightMap) {
        int max = 0;
        for (Map.Entry<Enchantment, List<Integer>> elem : weightMap.entrySet()) {
            List<Integer> weights = weightMap.get(elem.getKey());
            for (Integer i : weights) {
                max += i;
            }
        }
        return max;
    }
}
