package me.kunai.mcroleplay.commands.game;

import com.google.gson.JsonDeserializer;
import me.kunai.mcroleplay.commands.CommandHandler;
import me.kunai.mcroleplay.game.GameState;
import me.kunai.mcroleplay.game.GameTree;
import me.kunai.mcroleplay.npcs.BaseNPC;
import me.kunai.mcroleplay.npcs.BaseNPCDeserializer;
import me.kunai.mcroleplay.utils.DBManager;
import me.kunai.mcroleplay.utils.JSONReader;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.HashMap;

import static me.kunai.mcroleplay.utils.Constants.PLUGIN_DIR;

public class ReloadWorldCommand extends CommandHandler {

    private GameTree gameTree;

    public ReloadWorldCommand(DBManager dbManager, GameTree gameTree) {
        super(dbManager, true);

        this.gameTree = gameTree;
    }

    @Override
    public String getName() {
        return "mcrp_reload_world";
    }

    @Override
    protected void performCommand(Player invoker, String[] args) {
        GameState newRootState = gameTree.getRootState();
        if (args.length > 0) {
            String path = String.format("%s/%s", PLUGIN_DIR, args[0]);
            HashMap<Type, JsonDeserializer> customDeserializers = new HashMap<>();
            customDeserializers.put(BaseNPC.class, new BaseNPCDeserializer());
            try {
                newRootState = JSONReader.fromFile(path, GameState.class, customDeserializers);
            } catch (FileNotFoundException e) {
                invoker.sendMessage(String.format("Could not find file %s!", path));
                return;
            }
        }

        if (!gameTree.reloadGameTree(newRootState)) {
            invoker.sendMessage("INTERNAL ERROR: Could not load GameTree");
        }
    }
}
