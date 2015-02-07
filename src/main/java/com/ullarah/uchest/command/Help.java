package com.ullarah.uchest.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help {

    public static void runHelp(CommandSender sender){

        if(sender instanceof Player) {

            sender.sendMessage(new String[] {
                    ChatColor.AQUA + "Mixed Chest Help",
                    "------------------------------------------",
                    ChatColor.GOLD + " ▪ /dchest",
                    ChatColor.YELLOW + "   Opens the donation chest.",
                    ChatColor.GOLD + " ▪ /rchest",
                    ChatColor.YELLOW + "   Opens the random chest.",
                    "------------------------------------------",
                    ChatColor.RED + "Just remember that the chest resets every 3 hours."
            });

            if(sender.hasPermission("chest.staff")){

                sender.sendMessage(new String[] {
                        "------------------------------------------",
                        ChatColor.GOLD + " ▪ /chest maintenance <on|off>",
                        ChatColor.YELLOW + "   Puts the donation chest in maintenance mode.",
                        ChatColor.GOLD + " ▪ /dchest random",
                        ChatColor.YELLOW + "   Will override items in chest with random items.",
                        ChatColor.GOLD + " ▪ /dchest reset",
                        ChatColor.YELLOW + "   Clears the entire donation chest.",
                        ChatColor.GOLD + " ▪ /rchest toggle",
                        ChatColor.YELLOW + "   Starts / Stops the random chest."
                });

            }

        }

    }

}
