package me.kunai.mcroleplay;

import me.kunai.mcroleplay.commands.CommandHandler;
import me.kunai.mcroleplay.commands.crafting.AugmentItemCommand;
import me.kunai.mcroleplay.commands.crafting.TransmuteItemCommand;
import me.kunai.mcroleplay.commands.player.GrantSkillPointCommand;
import me.kunai.mcroleplay.commands.player.LevelUpCommand;
import me.kunai.mcroleplay.commands.player.ShowLevelDataCommand;
import me.kunai.mcroleplay.utils.DBManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class MCRolePlay extends JavaPlugin {

    private DBManager dbManager;
    private HashMap<String, CommandHandler> knownCommands;

    @Override
    public void onEnable() {
        dbManager = new DBManager();

        CommandHandler[] commandList = new CommandHandler[]{
                // Player-affecting commands
                new GrantSkillPointCommand(dbManager),
                new ShowLevelDataCommand(dbManager),
                new LevelUpCommand(dbManager),

                // Item crafting commands
                new TransmuteItemCommand(dbManager),
                new AugmentItemCommand(dbManager)
        };

        knownCommands = new HashMap<>();
        for (CommandHandler handler : commandList) {
            knownCommands.put(handler.getName(), handler);
        }
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
        if (knownCommands.containsKey(command.getName())) {
            knownCommands.get(command.getName()).handle(sender.getServer().getPlayer(sender.getName()), args);
            return true;
        }

        return false;
    }
}
