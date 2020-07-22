package me.kunai.mcroleplay.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
    public static void nameItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

}
