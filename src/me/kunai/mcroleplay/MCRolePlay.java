package me.kunai.mcroleplay;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static me.kunai.mcroleplay.Crafting.augmentItem;
import static me.kunai.mcroleplay.Crafting.transmuteItem;

public class MCRolePlay extends JavaPlugin {

    private DBManager dbManager;

    @Override
    public void onEnable() {
        dbManager = new DBManager();
    }

    @Override
    public void onDisable() {
        dbManager.disconnect();
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
        if (command.getName().equalsIgnoreCase("mcrp_augment_item")) {
            return handleAugmentItem(sender.getServer().getPlayer(sender.getName()));
        } else if (command.getName().equalsIgnoreCase("mcrp_transmute_item")) {
            return handleTransmuteItem(sender.getServer().getPlayer(sender.getName()));
        } else if (command.getName().startsWith("mcrp_levelup_")) {
            String classToLevel = command.getName().substring("mcrp_levelup_".length());
            return handleLevelUp(sender.getServer().getPlayer(sender.getName()), classToLevel);
        }
        return false;
    }

    private boolean handleAugmentItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getEnchantments().isEmpty()) {
            return handleTransmuteItem(player);
        }

        // Check for level / op
        boolean bypassCost = player.isOp() || player.getGameMode().equals(GameMode.CREATIVE);
        if (!bypassCost) {
            int numEnchants = item.getEnchantments().size();
            Material cost;
            switch (numEnchants) {
                case 1:
                    cost = Material.GOLD_INGOT;
                    break;
                case 2:
                    cost = Material.DIAMOND;
                    break;
                case 3:
                    cost = Material.EMERALD;
                    break;
                default:
                    cost = Material.NETHERITE_INGOT;
            }

            boolean costSatisfied = false;
            for (ItemStack playerItem : player.getInventory()) {
                if (playerItem == null) {
                    continue;
                }

                if (playerItem.getType().equals(cost)) {
                    playerItem.setAmount(playerItem.getAmount() - 1);
                    costSatisfied = true;
                    break;
                }
            }

            if (!costSatisfied) {
                player.sendMessage("You do not have the required item to augment the item!");
                return true;
            }
        }

        boolean success = augmentItem(item);
        if (!success) {
            player.sendMessage("Failed to augment item with enchantment. Is your selected item enchantable?");
        }
        return true;
    }

    private boolean handleTransmuteItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getEnchantments().isEmpty()) {
            player.sendMessage("Item is already enchanted!");
            return true;
        }

        // Check for artificer level if not op or creative
        int artificerRank;
        if (player.getGameMode().equals(GameMode.CREATIVE) || player.isOp()) {
            artificerRank = 6;
        } else {
            artificerRank = dbManager.getCurrentLevel(player.getName(), PlayerClass.Artificer);
            if (artificerRank < 0) {
                player.sendMessage("INTERNAL ERROR: Could not get artificer rank.");
                return true;
            }

            if (artificerRank == 0) {
                // Check for cost
                boolean costSatisfied = false;
                for (ItemStack playerItem : player.getInventory()) {
                    if (playerItem == null) {
                        continue;
                    }

                    if (playerItem.getType().equals(Material.IRON_INGOT)) {
                        playerItem.setAmount(playerItem.getAmount() - 1);
                        costSatisfied = true;
                        break;
                    }
                }

                if (!costSatisfied) {
                    player.sendMessage("You are not an artificer!");
                    player.sendMessage("You do not have the required item to augment the item!");
                    return true;
                }
            }
        }

        boolean success = transmuteItem(item, artificerRank);
        if (!success) {
            player.sendMessage("Failed to transmute item. Is your selected item enchantable?");
        }
        return true;
    }

    private boolean handleLevelUp(Player player, String classToLevel) {
        PlayerClass pClass = PlayerClass.Artificer;
        switch (classToLevel) {
            case "ranger":
                pClass = PlayerClass.Ranger;
                break;
            case "paladin":
                pClass = PlayerClass.Paladin;
                break;
            case "oceanmaster":
                pClass = PlayerClass.Oceanmaster;
                break;
            case "beastmaster":
                pClass = PlayerClass.Beastmaster;
                break;
            case "artificer":
                pClass = PlayerClass.Artificer;
                break;
            case "berserker":
                pClass = PlayerClass.Berserker;
                break;
            default:
                player.sendMessage("Invalid command: Not a valid class to level.");
                return true;
        }

        String playerName = player.getName();
        int availableSkillPoints = dbManager.getAvailableSkillPoints(playerName);
        if (availableSkillPoints < 0) {
            player.sendMessage("INTERNAL ERROR: Could not get number of available skill points.");
            return true;
        }
        if (availableSkillPoints == 0) {
            player.sendMessage("You do not have any skill points to level with!");
            return true;
        }
        int newLevel = dbManager.levelUp(playerName, pClass);
        if (newLevel <= 0) {
            player.sendMessage("INTERNAL ERROR: Failed to level up.");
            return true;
        }
        player.sendMessage(String.format("You have levelled up your %s class to level %d!", pClass.name(), newLevel));
        return true;
    }
}