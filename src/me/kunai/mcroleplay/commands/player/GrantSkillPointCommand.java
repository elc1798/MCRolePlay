package me.kunai.mcroleplay.commands.player;

import me.kunai.mcroleplay.commands.CommandHandler;
import me.kunai.mcroleplay.utils.DBManager;
import org.bukkit.entity.Player;

public class GrantSkillPointCommand extends CommandHandler {

    public GrantSkillPointCommand(DBManager db) {
        super(db, true);
    }

    @Override
    public String getName() {
        return "mcrp_grant_level";
    }

    @Override
    protected void performCommand(Player invoker, String[] args) {
        for (String name : args) {
            Player player = invoker.getServer().getPlayer(name);
            if (player == null || !player.isValid() || !player.isOnline() || !player.isWhitelisted()) {
                invoker.sendMessage(String.format("Player %s does not exist on the server", name));
                continue;
            }

            boolean success = getDBManager().incrementSkillPoints(name, 1);
            if (success) {
                invoker.sendMessage(String.format("Player %s has been granted a skill point!", name));
            } else {
                invoker.sendMessage("INTERNAL ERROR: Failed to grant skill point to " + name);
            }
        }
    }
}
