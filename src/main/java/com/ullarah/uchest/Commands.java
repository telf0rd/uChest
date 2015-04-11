package com.ullarah.uchest;

import com.ullarah.uchest.command.Help;
import com.ullarah.uchest.command.Maintenance;
import com.ullarah.uchest.command.Random;
import com.ullarah.uchest.command.Reset;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.ullarah.uchest.Init.*;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (command.getName().equalsIgnoreCase("chest")) commandChest(sender, args);
        if (command.getName().equalsIgnoreCase("dchest")) commandDChest(sender, args);
        if (command.getName().equalsIgnoreCase("rchest")) commandRChest(sender);
        if (command.getName().equalsIgnoreCase("xchest")) commandXChest(sender);
        if (command.getName().equalsIgnoreCase("hchest")) commandHChest(sender);
        if (command.getName().equalsIgnoreCase("schest")) commandSChest(sender);

        return true;

    }

    private void commandChest(CommandSender sender, String[] args) {

        String consoleTools = getMsgPrefix() + ChatColor.WHITE + "maintenance | random | reset";

        if (args.length == 0) if (!(sender instanceof Player))
            sender.sendMessage(consoleTools);
        else if (!getMaintenanceCheck())
            commandChestGUI(sender);
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
                        commandChestGUI(sender);


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
            Inventory chestExpInventory = Bukkit.createInventory(
                    player, 27, ChatColor.DARK_GREEN + "Experience Chest");
            player.openInventory(chestExpInventory);
        } else
            sender.sendMessage(getMaintenanceMessage());

    }

    private void commandHChest(CommandSender sender) {

        sender.sendMessage(getMsgPrefix() + "Holding chest coming soon!");

    }

    private void commandSChest(CommandSender sender) {

        if (!(sender instanceof Player))
            sender.sendMessage("Consoles can't use this command!");
        else if (!getMaintenanceCheck()) {
            if (chestSwapBusy) {
                sender.sendMessage(getMsgPrefix() + "The swap chest is busy. Try again later!");
            } else {
                ((Player) sender).openInventory(getChestSwapHolder().getInventory());
            }
        } else
            sender.sendMessage(getMaintenanceMessage());

    }

    private void commandChestGUI(CommandSender sender) {

        ItemStack dChestIcon = new ItemStack(Material.JUKEBOX);
        ItemMeta dChestIconMeta = dChestIcon.getItemMeta();
        dChestIconMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Donation Chest");
        dChestIconMeta.setLore(Arrays.asList(
                ChatColor.WHITE + "Opens the donation chest.",
                ChatColor.WHITE + "Give what you can, take what you need!",
                ChatColor.RESET + "",
                ChatColor.RED + "This chest is player supported!"));
        dChestIcon.setItemMeta(dChestIconMeta);

        ItemStack rChestIcon = new ItemStack(Material.SPONGE);
        ItemMeta rChestIconMeta = rChestIcon.getItemMeta();
        rChestIconMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Random Chest");
        rChestIconMeta.setLore(Arrays.asList(
                ChatColor.WHITE + "Opens the random chest.",
                ChatColor.WHITE + "Random items in random slots,",
                ChatColor.WHITE + "at short random intervals!",
                ChatColor.RESET + "",
                ChatColor.RED + "You have to be quick to grab them!"));
        rChestIcon.setItemMeta(rChestIconMeta);

        ItemStack xChestIcon = new ItemStack(Material.SEA_LANTERN);
        ItemMeta xChestIconMeta = xChestIcon.getItemMeta();
        xChestIconMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Experience Chest");
        xChestIconMeta.setLore(Arrays.asList(
                ChatColor.WHITE + "Opens the experience chest.",
                ChatColor.WHITE + "Allows you to convert items to XP!",
                ChatColor.RESET + "",
                ChatColor.RED + "Some items do not return XP!"));
        xChestIcon.setItemMeta(xChestIconMeta);

        ItemStack hChestIcon = new ItemStack(Material.CHEST);
        ItemMeta hChestIconMeta = hChestIcon.getItemMeta();
        hChestIconMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Holding Chest");
        hChestIconMeta.setLore(Arrays.asList(
                ChatColor.WHITE + "Opens your personal holding chest.",
                ChatColor.WHITE + "Store more items on the go!",
                ChatColor.RESET + "",
                ChatColor.RED + "Chest will reset back to 9 slots,",
                ChatColor.RED + "and drop it's contents on death!"));
        hChestIcon.setItemMeta(hChestIconMeta);

        ItemStack sChestIcon = new ItemStack(Material.GLASS);
        ItemMeta sChestIconMeta = sChestIcon.getItemMeta();
        sChestIconMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Swap Chest");
        sChestIconMeta.setLore(Arrays.asList(
                ChatColor.WHITE + "Opens the swapping chest.",
                ChatColor.WHITE + "Put random items in, get random items out!",
                ChatColor.RESET + "",
                ChatColor.RED + "This chest is player supported!"));
        sChestIcon.setItemMeta(sChestIconMeta);

        Player player = (Player) sender;
        Inventory hopperGUI = Bukkit.getServer().createInventory(
                null, InventoryType.HOPPER, "" + ChatColor.GOLD + ChatColor.BOLD + "Mixed Chests");
        hopperGUI.addItem(dChestIcon, rChestIcon, xChestIcon, hChestIcon, sChestIcon);
        player.openInventory(hopperGUI);

    }

    private enum validCommands {
        HELP, MAINTENANCE,
        RANDOM, RESET
    }

}
