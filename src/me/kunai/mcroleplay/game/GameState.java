package me.kunai.mcroleplay.game;

import com.google.gson.annotations.SerializedName;
import me.kunai.mcroleplay.npcs.BaseNPC;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * GameState represents a state the game is currently in. On a small scale, it can be as simple as the phase a boss or
 * fight is on. On a larger scale, it can be what chapter or quest the players are on.
 *
 * GameStates can also include SubStates, for instance Arc3.MidBoss.Phase2 to indicate the second phase of the midboss
 * of the third story arc.
 */
public class GameState {

    @SerializedName("name")
    private String name;

    @SerializedName("npcs")
    private BaseNPC[] npcs;

    @SerializedName("substates")
    private GameState[] substates;

    public GameState(String name, GameState[] substates) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public GameState getSubState(String id) {
        if (id == null) {
            return this;
        }

        LinkedList<String> ll = new LinkedList<>(Arrays.asList(id.split("\\.")));
        return getSubState(ll);
    }

    private GameState getSubState(LinkedList<String> components) {
        GameState curr = this;
        while (!components.isEmpty()) {
            String target = components.pop();

            boolean found = false;
            for (GameState subState : curr.getSubstates()) {
                if (subState.getName().equals(target)) {
                    found = true;
                    curr = subState;
                    break;
                }
            }

            if (!found) {
                return null;
            }
        }
        return curr;
    }

    public void loadNPCs(Plugin plugin) {
        for (BaseNPC npc : npcs) {
            npc.spawn(plugin);
        }
    }

    public void unloadNPCs() {
        for (BaseNPC npc : npcs) {
            npc.despawn();
        }
    }

    public String getName() {
        return name;
    }

    public BaseNPC[] getNpcs() {
        return npcs;
    }

    public GameState[] getSubstates() {
        return substates;
    }
}

