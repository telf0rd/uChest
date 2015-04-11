package com.ullarah.uchest;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.ullarah.uchest.Init.getMsgPrefix;
import static com.ullarah.uchest.Init.getPlugin;

class Events implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void customChestAccessDrag(final InventoryDragEvent event) {

        Inventory chestInventory = event.getInventory();

        if (chestInventory.getName().matches("§2Donation Chest")) {

            event.setCancelled(true);
        }

    }

    @SuppressWarnings("unused")
    @EventHandler
    public void randomChestSelfAccess(final InventoryClickEvent event) {

        Inventory chestInventory = event.getInventory();

        if (chestInventory.getName().matches("§2Donation Chest")) if (event.getRawSlot() == -999)
            event.getView().close();

        if (chestInventory.getName().matches("§2Experience Chest")) if (event.getRawSlot() == -999)
            event.getView().close();

        if (chestInventory.getName().matches("§2Random Chest")) if (event.getRawSlot() == -999) event.getView().close();
        else if (event.getCurrentItem().getAmount() >= 1 && event.getRawSlot() >= 54) event.setCancelled(true);

    }

    @SuppressWarnings("unused")
    @EventHandler
    public void randomChestSelfAccess(final InventoryCloseEvent event) {

        Inventory chestInventory = event.getInventory();

        if (chestInventory.getName().matches("§2Experience Chest")) {

            ItemStack[] expItems = chestInventory.getContents();
            giveExp((Player) event.getPlayer(), expItems);
            chestInventory.clear();

        }

    }

    void giveExp(Player player, ItemStack[] items) {

        Integer playerXP = player.getTotalExperience();
        Integer playerNewXP = 0;

        for (ItemStack item : items) {

            if (item != null) {

                Integer itemAmount = item.getAmount();
                String itemMaterial = item.getType().name();

                Integer itemXP = getPlugin().getConfig().getConfigurationSection("materials").getInt(itemMaterial);

                playerNewXP += (itemXP * itemAmount);

            }

        }

        Integer playerTotalXP = playerXP + playerNewXP;

        player.setTotalExperience(playerTotalXP);
        player.setLevel(0);
        player.setExp(0);

        for (; playerTotalXP > player.getExpToLevel(); ) {
            playerTotalXP -= player.getExpToLevel();
            player.setLevel(player.getLevel() + 1);
        }

        float xp = (float) playerTotalXP / (float) player.getExpToLevel();
        player.setExp(xp);

        player.sendMessage(playerNewXP > 0 ? getMsgPrefix() + "You gained " + ChatColor.GREEN + playerNewXP +
                "xp" + ChatColor.RESET + "!" : getMsgPrefix() + "You gained no xp.");


    }

}