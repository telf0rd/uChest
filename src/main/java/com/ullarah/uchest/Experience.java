package com.ullarah.uchest;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.ullarah.uchest.Init.getMsgPrefix;
import static com.ullarah.uchest.Init.getPlugin;

class Experience {

    public static void convertXP(Player player, ItemStack[] items) {

        double newXP = 0.0;

        for (ItemStack item : items)
            if (item != null) {
                Integer itemAmount = item.getAmount();
                String itemMaterial = item.getType().name();

                double itemXP = getPlugin().getConfig().getConfigurationSection("materials").getDouble(itemMaterial);
                newXP += (itemXP * itemAmount);
            }

        ExperienceManager expMan = new ExperienceManager(player);
        expMan.changeExp(newXP);

        player.sendMessage(newXP > 0 ? getMsgPrefix() + "You gained " + ChatColor.GREEN + newXP +
                "xp" + ChatColor.RESET + "!" : getMsgPrefix() + "You gained no xp.");

    }

}