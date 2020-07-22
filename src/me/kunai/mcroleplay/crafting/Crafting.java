package me.kunai.mcroleplay.crafting;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Random;

/** Handles Path of Exile style item crafting
 */
public class Crafting {

    public static boolean augmentItem(ItemStack item ) {
        Map<Enchantment, List<Integer>> weightMap = Weightings.getWeightMapFor(item, Weightings.getAllWeights());
        return rollEnchantment(item, weightMap);
    }

    public static boolean transmuteItem(ItemStack item, int artificerRank) {
        Map<Enchantment, List<Integer>> weightMap = Weightings.getWeightMapFor(item, Weightings.getTransmuteWeights(artificerRank));
        return rollEnchantment(item, weightMap);
    }

    private static boolean rollEnchantment(ItemStack item, Map<Enchantment, List<Integer>> weightMap) {
        // Calculate new total weighting
        int max = Weightings.getTotalWeight(weightMap);
        if (max == 0) {
            return false;
        }

        Random rand = new Random();
        int index = rand.nextInt(max);

        for (Map.Entry<Enchantment, List<Integer>> elem : weightMap.entrySet()) {
            for (int i = 0; i < weightMap.get(elem.getKey()).size(); i++) {
                int currWeight = weightMap.get(elem.getKey()).get(i);
                if (currWeight <= 0) continue;

                if (index <= currWeight) {
                    item.addEnchantment(elem.getKey(), i + 1);
                    return true;
                }

                index -= currWeight;
            }
        }

        return false;
    }
}
