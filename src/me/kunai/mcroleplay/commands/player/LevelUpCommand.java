package me.kunai.mcroleplay.commands.player;

import me.kunai.mcroleplay.commands.CommandHandler;
import me.kunai.mcroleplay.playerclasses.*;
import me.kunai.mcroleplay.utils.DBManager;
import org.bukkit.entity.Player;

public class LevelUpCommand extends CommandHandler {

    public LevelUpCommand(DBManager dbManager) {
        super(dbManager, false);
    }

    @Override
    public String getName() {
        return "mcrp_levelup";
    }

    @Override
    protected void performCommand(Player invoker, String[] args) {
        if (args.length != 1) {
            invoker.sendMessage("You must specify a class to level!");
            return;
        }

        String classToLevel = args[0];

        BasePlayerClass bpClass;
        switch (classToLevel) {
            case "ranger":
                bpClass = new RangerClass(invoker);
                break;
            case "paladin":
                bpClass = new PaladinClass(invoker);
                break;
            case "oceanmaster":
                bpClass = new OceanMasterClass(invoker);
                break;
            case "beastmaster":
                bpClass = new BeastMasterClass(invoker);
                break;
            case "artificer":
                bpClass = new ArtificerClass(invoker);
                break;
            case "berserker":
                bpClass = new BerserkerClass(invoker);
                break;
            default:
                invoker.sendMessage("Invalid command: Not a valid class to level.");
                return;
        }

        String playerName = invoker.getName();
        int availableSkillPoints = getDBManager().getAvailableSkillPoints(playerName);
        if (availableSkillPoints < 0) {
            invoker.sendMessage("INTERNAL ERROR: Could not get number of available skill points.");
            return;
        }
        if (availableSkillPoints == 0) {
            invoker.sendMessage("You do not have any skill points to level with!");
            return;
        }
        int newLevel = getDBManager().levelUp(playerName, bpClass.getPlayerClass());
        if (newLevel <= 0) {
            invoker.sendMessage("INTERNAL ERROR: Failed to level up.");
            return;
        }
        bpClass.triggerLevel(newLevel);
        invoker.sendMessage(String.format("You have levelled up your %s class to level %d!", bpClass.getPlayerClass().name(), newLevel));
        getDBManager().incrementSkillPoints(playerName, -1);
    }
}
