package me.kunai.mcroleplay;

import com.google.gson.JsonDeserializer;
import me.kunai.mcroleplay.commands.CommandHandler;
import me.kunai.mcroleplay.commands.crafting.AugmentItemCommand;
import me.kunai.mcroleplay.commands.crafting.TransmuteItemCommand;
import me.kunai.mcroleplay.commands.game.GameStateCommand;
import me.kunai.mcroleplay.commands.game.ReloadWorldCommand;
import me.kunai.mcroleplay.commands.player.GrantSkillPointCommand;
import me.kunai.mcroleplay.commands.player.LevelUpCommand;
import me.kunai.mcroleplay.commands.player.ShowLevelDataCommand;
import me.kunai.mcroleplay.game.GameState;
import me.kunai.mcroleplay.game.GameTree;
import me.kunai.mcroleplay.npcs.BaseNPC;
import me.kunai.mcroleplay.npcs.BaseNPCDeserializer;
import me.kunai.mcroleplay.utils.DBManager;
import me.kunai.mcroleplay.utils.JSONReader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.HashMap;

import static me.kunai.mcroleplay.utils.Constants.PLUGIN_DIR;

public class MCRolePlay extends JavaPlugin {

    private static final String GAME_TREE_JSON_PATH = PLUGIN_DIR + "/gametree.json";

    private DBManager dbManager;
    private HashMap<String, CommandHandler> knownCommands;
    private GameTree gameTree;

    @Override
    public void onEnable() {
        dbManager = new DBManager();
        HashMap<Type, JsonDeserializer> customDeserializers = new HashMap<>();
        customDeserializers.put(BaseNPC.class, new BaseNPCDeserializer());
        try {
            gameTree = new GameTree(JSONReader.fromFile(GAME_TREE_JSON_PATH, GameState.class, customDeserializers), this);
        } catch (FileNotFoundException e) {
            gameTree = null;
        }

        CommandHandler[] commandList = new CommandHandler[]{
                // Player-affecting commands
                new GrantSkillPointCommand(dbManager),
                new ShowLevelDataCommand(dbManager),
                new LevelUpCommand(dbManager),

                // Item crafting commands
                new TransmuteItemCommand(dbManager),
                new AugmentItemCommand(dbManager),

                // GameTree commands
                new ReloadWorldCommand(dbManager, gameTree),
                new GameStateCommand(dbManager, gameTree)
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
