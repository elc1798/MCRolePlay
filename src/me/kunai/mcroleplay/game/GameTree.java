package me.kunai.mcroleplay.game;

import me.kunai.mcroleplay.npcs.BaseNPC;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class GameTree {

    private GameState gameTreeRoot;
    private Plugin plugin;

    private GameState currGameState;

    public GameTree(GameState gameTreeRoot, Plugin plugin) {
        if (gameTreeRoot == null) {
            System.err.println("INTERNAL ERROR: GameTreeRoot is null");
        }
        this.gameTreeRoot = gameTreeRoot;
        this.plugin = plugin;

        currGameState = gameTreeRoot;
        if (gameTreeRoot != null) {
            gameTreeRoot.loadNPCs(plugin);
        }
    }

    public boolean loadGameState(String stateID) {
        GameState tmp = gameTreeRoot.getSubState(stateID);
        if (tmp == null) {
            return false;
        }

        loadGameState(tmp);
        return true;
    }

    public void loadGameState(GameState state) {
        // Unload previous game state
        if (currGameState != null) {
            currGameState.unloadNPCs();
        }

        // Set new game state
        currGameState = state;
        currGameState.loadNPCs(plugin);
    }

    public boolean reloadGameTree(GameState newGameTreeRoot) {
        if (newGameTreeRoot == null) {
            return false;
        }

        if (currGameState != null) {
            currGameState.unloadNPCs();
        }

        gameTreeRoot = newGameTreeRoot;
        currGameState = newGameTreeRoot;
        currGameState.loadNPCs(plugin);

        return true;
    }

    public GameState getRootState() {
        return gameTreeRoot;
    }

    public GameState getCurrentState() {
        return currGameState;
    }

    public List<BaseNPC> getCurrentNPCs() {
        return Arrays.asList(currGameState.getNpcs());
    }

    public void resetNPCs() {
        currGameState.loadNPCs(plugin);
    }
}
