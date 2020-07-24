package me.kunai.mcroleplay.commands.player;

import me.kunai.mcroleplay.commands.CommandHandler;
import me.kunai.mcroleplay.playerclasses.PlayerClass;
import me.kunai.mcroleplay.utils.DBManager;
import org.bukkit.entity.Player;

public class ShowLevelDataCommand extends CommandHandler {

    public ShowLevelDataCommand(DBManager dbManager) {
        super(dbManager, false);
    }

    @Override
    public String getName() {
        return "mcrp_show_level_data";
    }

    @Override
    protected void performCommand(Player invoker, String[] args) {
        invoker.sendMessage(String.format("======= %s's level data =======", invoker.getName()));

        // Show available skill points:
        int availSkillPts = getDBManager().getAvailableSkillPoints(invoker.getName());
        invoker.sendMessage(String.format("You have %d available skill points.", availSkillPts));

        for (PlayerClass pClass : PlayerClass.values()) {
            int currLevel = getDBManager().getCurrentLevel(invoker.getName(), pClass);
            invoker.sendMessage(String.format(" * %s: Level %d", pClass.name(), currLevel));
        }
    }
}
