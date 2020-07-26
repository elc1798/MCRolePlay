package me.kunai.mcroleplay.commands.game;

import me.kunai.mcroleplay.commands.CommandHandler;
import me.kunai.mcroleplay.game.GameState;
import me.kunai.mcroleplay.game.GameTree;
import me.kunai.mcroleplay.utils.DBManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GameStateCommand extends CommandHandler {

    private GameTree gameTree;

    public GameStateCommand(DBManager dbManager, GameTree gameTree) {
        super(dbManager, true);

        this.gameTree = gameTree;
    }

    @Override
    public String getName() {
        return "mcrp_game_state";
    }

    @Override
    protected void performCommand(Player invoker, String[] args) {
        if (args.length == 0) {
            invoker.sendMessage("No subcommand specified!");
        }

        String subcommand = args[0];

        switch (subcommand) {
            case "load":
                if (args.length < 2) {
                    invoker.sendMessage("No game state specified");
                }

                if (!gameTree.loadGameState(args[1])) {
                    invoker.sendMessage("Failed to load game state.");
                }
                return;
            case "reset_npc":
                gameTree.resetNPCs();
                return;
            case "show_curr_state":
                invoker.sendMessage(String.format("Current game state: %s", gameTree.getCurrentState().getName()));
                return;
            case "show_substates":
                ArrayList<String> substateNames = new ArrayList<>();
                GameState tmp = gameTree.getCurrentState();
                if (args.length >= 2) {
                    tmp = gameTree.getRootState().getSubState(args[1]);
                }

                for (GameState state : tmp.getSubstates()) {
                    substateNames.add(state.getName());
                }
                invoker.sendMessage(String.format("Substates: %s", String.join(", ", substateNames)));
                return;
            case "enter_substate":
                if (args.length < 2) {
                    invoker.sendMessage("No substate specified");
                }

                GameState substate = gameTree.getCurrentState().getSubState(args[1]);
                gameTree.loadGameState(substate);
                return;
            default:
                invoker.sendMessage("Invalid subcommand");
        }
    }
}
