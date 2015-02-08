package com.ullarah.uchest.command;

import com.ullarah.uchest.Init;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reset {

    public static void resetDonationChest(CommandSender sender) {

        if (sender.hasPermission("chest.reset") || !(sender instanceof Player)) {

            Init.getChestDonationInventory().clear();
            Bukkit.broadcastMessage(Init.getMsgPrefix() + ChatColor.RED + "Donation Chest has been reset!");

        } else sender.sendMessage(Init.getMsgPermDeny());

    }

}
