package com.ullarah.uchest;

import com.ullarah.uchest.command.Help;
import com.ullarah.uchest.command.Maintenance;
import com.ullarah.uchest.command.Random;
import com.ullarah.uchest.command.Reset;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static com.ullarah.uchest.Init.*;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (command.getName().equalsIgnoreCase("chest")) commandChest(sender, args);
        if (command.getName().equalsIgnoreCase("dchest")) commandDChest(sender, args);
        if (command.getName().equalsIgnoreCase("rchest")) commandRChest(sender);
        if (command.getName().equalsIgnoreCase("xchest")) commandXChest(sender);

        return true;

    }

    private void commandChest(CommandSender sender, String[] args) {

        String consoleTools = getMsgPrefix() + ChatColor.WHITE + "maintenance | random | reset";

        if (args.length == 0) if (!(sender instanceof Player))
            sender.sendMessage(consoleTools);
        else if (!getMaintenanceCheck())
            Help.runHelp(sender);
        else
            sender.sendMessage(getMaintenanceMessage());

        else try {

            switch (validCommands.valueOf(args[0].toUpperCase())) {

                case HELP:
                    if (!(sender instanceof Player))
                        sender.sendMessage(getMsgNoConsole());
                    else
                        Help.runHelp(sender);
                    break;

                case MAINTENANCE:
                    Maintenance.runMaintenance(sender, args);
                    break;

                case RANDOM:
                    if (!getMaintenanceCheck())
                        Random.fillDonationChest(sender);
                    else
                        sender.sendMessage(getMaintenanceMessage());
                    break;

                case RESET:
                    if (!getMaintenanceCheck())
                        Reset.resetDonationChest(sender);
                    else
                        sender.sendMessage(getMaintenanceMessage());
                    break;

                default:
                    if (!(sender instanceof Player))
                        sender.sendMessage(consoleTools);
                    else
                        Help.runHelp(sender);

            }

        } catch (IllegalArgumentException e) {

            if (!(sender instanceof Player))
                sender.sendMessage(consoleTools);
            else
                Help.runHelp(sender);

        }

    }

    private void commandDChest(CommandSender sender, String[] args) {

        String consoleTools = getMsgPrefix() + ChatColor.WHITE + "random";

        if (args.length == 0) if (!(sender instanceof Player))
            sender.sendMessage("Consoles can't use this command!");
        else if (!getMaintenanceCheck())
            ((Player) sender).openInventory(getChestDonationHolder().getInventory());
        else
            sender.sendMessage(getMaintenanceMessage());

        else try {

            switch (validCommands.valueOf(args[0].toUpperCase())) {

                case RANDOM:
                    if (!getMaintenanceCheck())
                        Random.fillDonationChest(sender);
                    else
                        sender.sendMessage(getMaintenanceMessage());
                    break;

                default:
                    if (!(sender instanceof Player))
                        sender.sendMessage(consoleTools);
                    else
                        ((Player) sender).openInventory(getChestDonationHolder().getInventory());

            }

        } catch (IllegalArgumentException e) {

            if (!(sender instanceof Player))
                sender.sendMessage(consoleTools);
            else
                Help.runHelp(sender);

        }

    }

    private void commandRChest(CommandSender sender) {

        if (!(sender instanceof Player))
            sender.sendMessage("Consoles can't use this command!");
        else if (!getMaintenanceCheck())
            ((Player) sender).openInventory(getChestRandomHolder().getInventory());
        else
            sender.sendMessage(getMaintenanceMessage());

    }

    private void commandXChest(CommandSender sender) {

        if (!(sender instanceof Player))
            sender.sendMessage("Consoles can't use this command!");
        else if (!getMaintenanceCheck()) {
            Player player = (Player) sender;
            Inventory chestExpInventory = Bukkit.createInventory(player, 54, ChatColor.DARK_GREEN + "Experience Chest");
            player.openInventory(chestExpInventory);
        } else
            sender.sendMessage(getMaintenanceMessage());

    }

    private enum validCommands {
        HELP, MAINTENANCE,
        RANDOM, RESET
    }

}
