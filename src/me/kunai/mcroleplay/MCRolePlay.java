package me.kunai.mcroleplay;

import me.kunai.mcroleplay.playerclasses.*;
import me.kunai.mcroleplay.utils.DBManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static me.kunai.mcroleplay.crafting.Crafting.augmentItem;
import static me.kunai.mcroleplay.crafting.Crafting.transmuteItem;

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
        } else if (command.getName().equalsIgnoreCase("mcrp_levelup")) {
            if (args.length != 1) {
                sender.sendMessage("You must specify a class to level!");
                return true;
            }
            return handleLevelUp(sender.getServer().getPlayer(sender.getName()), args[0]);
        } else if (command.getName().equalsIgnoreCase("mcrp_grant_level")) {
            return handleGrantLevel(sender, args);
        } else if (command.getName().equalsIgnoreCase("mcrp_show_level_data")) {
            return handleShowLevelData(sender);
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
            for (ItemStack stack : player.getInventory().all(cost).values()) {
                stack.setAmount(stack.getAmount() - 1);
                costSatisfied = true;
                break;
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
                for (ItemStack stack : player.getInventory().all(Material.IRON_INGOT).values()) {
                    stack.setAmount(stack.getAmount() - 1);
                    costSatisfied = true;
                    break;
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
        BasePlayerClass bpClass;
        switch (classToLevel) {
            case "ranger":
                bpClass = new RangerClass(player);
                break;
            case "paladin":
                bpClass = new PaladinClass(player);
                break;
            case "oceanmaster":
                bpClass = new OceanMasterClass(player);
                break;
            case "beastmaster":
                bpClass = new BeastMasterClass(player);
                break;
            case "artificer":
                bpClass = new ArtificerClass(player);
                break;
            case "berserker":
                bpClass = new BerserkerClass(player);
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
        int newLevel = dbManager.levelUp(playerName, bpClass.getPlayerClass());
        if (newLevel <= 0) {
            player.sendMessage("INTERNAL ERROR: Failed to level up.");
            return true;
        }
        bpClass.triggerLevel(newLevel);
        player.sendMessage(String.format("You have levelled up your %s class to level %d!", bpClass.getPlayerClass().name(), newLevel));
        dbManager.incrementSkillPoints(playerName, -1);

        return true;
    }

    private boolean handleGrantLevel(CommandSender sender, String[] playerNames) {
        if (!sender.isOp()) {
            sender.sendMessage("Only ops can grant levels!");
            return true;
        }

        for (String name : playerNames) {
            Player player = sender.getServer().getPlayer(name);
            if (player == null || !player.isValid() || !player.isOnline() || !player.isWhitelisted()) {
                sender.sendMessage(String.format("Player %s does not exist on the server", name));
                continue;
            }

            boolean success = dbManager.incrementSkillPoints(name, 1);
            if (success) {
                sender.sendMessage(String.format("Player %s has been granted a skill point!", name));
            } else {
                sender.sendMessage("INTERNAL ERROR: Failed to grant skill point to " + name);
            }
        }
        return true;
    }

    private boolean handleShowLevelData(CommandSender sender) {
        sender.sendMessage(String.format("======= %s's level data =======", sender.getName()));

        // Show available skill points:
        int availSkilPts = dbManager.getAvailableSkillPoints(sender.getName());
        sender.sendMessage(String.format("You have %d available skill points.", availSkilPts));

        for (PlayerClass pClass : PlayerClass.values()) {
            int currLevel = dbManager.getCurrentLevel(sender.getName(), pClass);
            sender.sendMessage(String.format(" * %s: Level %d", pClass.name(), currLevel));
        }
        return true;
    }
}
