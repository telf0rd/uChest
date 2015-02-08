package com.ullarah.uchest.command;

import com.ullarah.uchest.Init;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Random {

    public static void fillDonationChest(CommandSender sender){

        if ( sender.hasPermission("chest.random") || !( sender instanceof Player ) ) {

            for (int i = 0; i < Init.getChestDonationInventory().getSize(); i++) {

                java.util.Random chestRandomItem = new java.util.Random();

                if (chestRandomItem.nextInt() > 50) {
                    List<String> materialList = Init.getPlugin().getConfig().getStringList("allowedmaterials");
                    java.util.Random randomItem = new java.util.Random();

                    Init.getChestDonationInventory().setItem(i, new ItemStack(Material.getMaterial(materialList.get(randomItem.nextInt(materialList.size())))));
                }

            }

            Bukkit.broadcastMessage(Init.getMsgPrefix() + ChatColor.GREEN + "Donation Chest has been randomized with new items!");
            Bukkit.broadcastMessage(Init.getMsgPrefix() + ChatColor.GREEN + "Quick! Use /dchest to open it up!");

        } else sender.sendMessage(Init.getMsgPermDeny());

    }

    public static void setRandomItem(Integer amount){

        java.util.Random chestRandomItem = new java.util.Random();
        List<String> materialList = Init.getPlugin().getConfig().getStringList("allowedmaterials");

        for (int i = 0; i < amount; ++i)
            Init.getChestRandomInventory().setItem(chestRandomItem.nextInt(53) + 1,
                    new ItemStack(Material.getMaterial(materialList.get(chestRandomItem.nextInt(materialList.size())))));

    }

}
