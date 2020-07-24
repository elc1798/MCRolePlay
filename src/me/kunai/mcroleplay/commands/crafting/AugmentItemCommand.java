package me.kunai.mcroleplay.commands.crafting;

import me.kunai.mcroleplay.utils.DBManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

import static me.kunai.mcroleplay.crafting.Crafting.augmentItem;

public class AugmentItemCommand extends TransmuteItemCommand {
    public AugmentItemCommand(DBManager dbManager) {
        super(dbManager);
    }

    @Override
    public List<ItemStack> getCost(Player invoker) {
        // Check for level / op
        if (invoker.isOp() || invoker.getGameMode().equals(GameMode.CREATIVE)) {
            return null;
        }

        ItemStack item = invoker.getInventory().getItemInMainHand();
        int numEnchants = item.getEnchantments().size();
        switch (numEnchants) {
            case 0:
                return super.getCost(invoker);
            case 1:
                return Collections.singletonList(new ItemStack(Material.GOLD_INGOT, 1));
            case 2:
                return Collections.singletonList(new ItemStack(Material.DIAMOND, 1));
            case 3:
                return Collections.singletonList(new ItemStack(Material.EMERALD, 1));
            default:
                return Collections.singletonList(new ItemStack(Material.NETHERITE_INGOT, 1));
        }
    }

    @Override
    public String getName() {
        return "mcrp_augment_item";
    }

    @Override
    public void performCommand(Player invoker, String[] args) {
        ItemStack item = invoker.getInventory().getItemInMainHand();
        if (item.getEnchantments().isEmpty()) {
            super.performCommand(invoker, args);
            return;
        }

        boolean success = augmentItem(item);
        if (!success) {
            invoker.sendMessage("Failed to augment item with enchantment. Is your selected item enchantable?");
        }
    }
}
