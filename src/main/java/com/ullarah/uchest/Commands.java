package com.ullarah.uchest;

import com.ullarah.uchest.command.*;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args ) {

        if( command.getName().equalsIgnoreCase( "chest" ) ) commandChest(sender, args);
        if( command.getName().equalsIgnoreCase( "dchest" ) ) commandDChest(sender);
        if( command.getName().equalsIgnoreCase( "rchest" ) ) commandRChest(sender);

        return true;

    }

    private void commandChest(CommandSender sender, String[] args){

        String consoleTools = Init.getMsgPrefix() + ChatColor.WHITE + "maintenance | random | reset";

        if ( args.length == 0 ) if (!(sender instanceof Player))
            sender.sendMessage(consoleTools);
        else if (!Init.getMaintenanceCheck())
            Help.runHelp(sender);
        else
            sender.sendMessage(Init.getMaintenanceMessage());

        else try {

            switch ( validCommands.valueOf( args[0].toUpperCase() ) ) {

                case HELP:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(Init.getMsgNoConsole());
                    else
                        Help.runHelp(sender);
                    break;

                case MAINTENANCE:
                    Maintenance.runMaintenance(sender, args);
                    break;

                case RANDOM:
                    if( !Init.getMaintenanceCheck())
                        Random.fillDonationChest(sender);
                    else
                        sender.sendMessage(Init.getMaintenanceMessage());
                    break;

                case RESET:
                    if( !Init.getMaintenanceCheck())
                        Reset.resetDonationChest(sender);
                    else
                        sender.sendMessage(Init.getMaintenanceMessage());
                    break;

                default:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage( consoleTools );
                    else
                        Help.runHelp(sender);

            }

        } catch ( IllegalArgumentException e ) {

            if( !( sender instanceof Player ) )
                sender.sendMessage(consoleTools);
            else
                Help.runHelp(sender);

        }

    }

    private void commandDChest(CommandSender sender){

        if (!(sender instanceof Player))
            sender.sendMessage("Consoles can't use this command!");
        else if (!Init.getMaintenanceCheck())
            ((Player) sender).openInventory(Init.getChestDonationHolder().getInventory());
        else
            sender.sendMessage(Init.getMaintenanceMessage());

    }

    private void commandRChest(CommandSender sender){

        if (!(sender instanceof Player))
            sender.sendMessage("Consoles can't use this command!");
        else if (!Init.getMaintenanceCheck())
            ((Player) sender).openInventory(Init.getChestRandomHolder().getInventory());
        else
            sender.sendMessage(Init.getMaintenanceMessage());

    }

    private enum validCommands {
        HELP, MAINTENANCE,
        RANDOM, RESET
    }

}
