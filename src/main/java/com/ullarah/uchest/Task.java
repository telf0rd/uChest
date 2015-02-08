package com.ullarah.uchest;

import com.ullarah.uchest.command.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

class Task {

    private static final long chestIntervalFinal = Init.getPlugin().getConfig().getLong("raninterval") * 20;
    private static final long chestStayFinal = Init.getPlugin().getConfig().getLong("ranstay") * 20;
    private static final long chestAmountFinal = Init.getPlugin().getConfig().getLong("ranamount");

    private static final long chestCountdownFinal = Init.getPlugin().getConfig().getLong("countdown") * 20;

    // 10 minutes before the final clearing.
    private static final long chestCountdownWarning = (Init.getPlugin().getConfig().getLong("countdown") - 600) * 20;

    // 1 minute before the final clearing.
    private static final long chestCountdownCritical = (Init.getPlugin().getConfig().getLong("countdown") - 60) * 20;

    public static void runCleanChest() {

        if (chestCountdownFinal > 0)
            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Init.getPlugin(), new Runnable() {

                public void run() {

                    Init.getChestDonationInventory().clear();

                    Bukkit.getLogger().info("[" + Init.getPlugin().getName() + "] " + "Cleaning Donation Chest Items...");
                    Bukkit.broadcastMessage(Init.getMsgPrefix() + ChatColor.YELLOW + "Donation Chest has been emptied!");

                }

            }, chestCountdownFinal, chestCountdownFinal);

    }

    public static void runAnnouncements() {

        if (chestCountdownFinal > 0) {

            // 10 Minute Warning
            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Init.getPlugin(), new Runnable() {
                public void run() {
                    Bukkit.broadcastMessage(Init.getMsgPrefix() + ChatColor.AQUA + "10 minutes left until the Donation Chest is emptied!");
                }
            }, chestCountdownWarning, chestCountdownWarning);

            // 1 Minute Warning
            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Init.getPlugin(), new Runnable() {
                public void run() {
                    Bukkit.broadcastMessage(Init.getMsgPrefix() + ChatColor.RED + "60 seconds left until the Donation Chest is emptied!");
                }
            }, chestCountdownCritical, chestCountdownCritical);

        }

    }

    public static void runRandomTimer() {

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Init.getPlugin(), new Runnable() {

            @Override
            public void run() {

                Init.getChestRandomInventory().clear();
                Random.setRandomItem((int) chestAmountFinal);

                Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Init.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        Init.getChestRandomInventory().clear();
                    }
                }, chestStayFinal);

            }

        }, chestIntervalFinal, chestIntervalFinal);

    }

}