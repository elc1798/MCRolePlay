package me.kunai.mcroleplay.commands.crafting;

import me.kunai.mcroleplay.commands.CommandHandler;
import me.kunai.mcroleplay.playerclasses.PlayerClass;
import me.kunai.mcroleplay.utils.DBManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

import static me.kunai.mcroleplay.crafting.Crafting.transmuteItem;

public class TransmuteItemCommand extends CommandHandler {
    public TransmuteItemCommand(DBManager dbManager) {
        super(dbManager, false);
    }

    @Override
    public List<ItemStack> getCost(Player invoker) {
        if (invoker.getGameMode().equals(GameMode.CREATIVE) || invoker.isOp()) {
            return null;
        }
        int artificerRank = getDBManager().getCurrentLevel(invoker.getName(), PlayerClass.Artificer);
        if (artificerRank < 0) {
            invoker.sendMessage("INTERNAL ERROR: Could not get artificer rank.");
        }

        if (artificerRank > 0) {
            return null;
        };

        return Collections.singletonList(new ItemStack(Material.IRON_INGOT, 1));
    }

    @Override
    public String getName() {
        return "mcrp_transmute_item";
    }

    @Override
    public void performCommand(Player invoker, String[] args) {
        ItemStack item = invoker.getInventory().getItemInMainHand();
        if (!item.getEnchantments().isEmpty()) {
            invoker.sendMessage("Item is already enchanted!");
            return;
        }

        // Check for artificer level if not op or creative
        int artificerRank = 6;
        if (!invoker.getGameMode().equals(GameMode.CREATIVE) && !invoker.isOp()) {
            artificerRank = getDBManager().getCurrentLevel(invoker.getName(), PlayerClass.Artificer);
            if (artificerRank < 0) {
                invoker.sendMessage("INTERNAL ERROR: Could not get artificer rank.");
                return;
            }
        }

        boolean success = transmuteItem(item, artificerRank);
        if (!success) {
            invoker.sendMessage("Failed to transmute item. Is your selected item enchantable?");
        }
    }
}
