package me.kunai.mcroleplay.commands;

import me.kunai.mcroleplay.utils.DBManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class CommandHandler {

    private DBManager dbManager;
    private boolean opOnly;

    protected CommandHandler(DBManager dbManager, boolean opOnly) {
        this.dbManager = dbManager;
        this.opOnly = opOnly;
    }

    public final void handle(Player invoker, String[] args) {
        if (opOnly && invoker != null && !invoker.isOp()) {
            invoker.sendMessage(String.format("%s can only be used by ops.", getName()));
            return;
        }

        if (invoker != null && getCost(invoker) != null && !invoker.getGameMode().equals(GameMode.CREATIVE)) {
            List<ItemStack> unsatisfiedCost = handleCost(invoker);
            if (unsatisfiedCost.size() > 0) {
                invoker.sendMessage("The following materials are missing:");
                for (ItemStack missing : unsatisfiedCost) {
                    invoker.sendMessage(String.format(" * %s x%d", missing.getType().name(), missing.getAmount()));
                }
                return;
            }
        }

        performCommand(invoker, args);
    }

    protected final DBManager getDBManager() {
        return dbManager;
    }

    public List<ItemStack> getCost(Player invoker) {
        return null;
    }

    public abstract String getName();
    protected abstract void performCommand(Player invoker, String[] args);

    /**
     * Handles applying the item cost of the command to the invoker.
     *
     * @return A list of items that the invoker requires to satsify this command.
     */
    private List<ItemStack> handleCost(Player invoker) {
        ArrayList<ItemStack> required = new ArrayList<>();

        // Cache the found items, since we do not want to remove any if the cost cannot be fulfilled completely
        HashMap<Material, ArrayList<ItemStack>> found = new HashMap<>();

        List<ItemStack> cost = getCost(invoker);
        if (cost == null) {
            return required;
        }

        for (ItemStack costElem : cost) {
            int rollingSum = 0;
            ArrayList<ItemStack> forCost = new ArrayList<>();
            for (ItemStack stack : invoker.getInventory().all(costElem.getType()).values()) {
                forCost.add(stack);
                rollingSum += stack.getAmount();

                if (rollingSum >= costElem.getAmount()) {
                    break;
                }
            }
            found.put(costElem.getType(), forCost);

            if (rollingSum < costElem.getAmount()) {
                required.add(new ItemStack(costElem.getType(), costElem.getAmount() - rollingSum));
            }
        }

        if (required.size() > 0) {
            return required;
        }

        for (ItemStack costElem : cost) {
            int amtToRemove = costElem.getAmount();

            for (ItemStack target : found.get(costElem.getType())) {
                int removed = Math.min(target.getAmount(), amtToRemove);
                target.setAmount(target.getAmount() - removed);
                amtToRemove -= removed;

                if (amtToRemove <= 0) {
                    break;
                }
            }
        }
        return required;
    }
}
