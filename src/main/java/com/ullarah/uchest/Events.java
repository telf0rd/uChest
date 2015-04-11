package com.ullarah.uchest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.ullarah.uchest.Init.*;

class Events implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void chestDrag(final InventoryDragEvent event) {

        Inventory chestInventory = event.getInventory();

        if (chestInventory.getName().matches("§2Donation Chest")) event.setCancelled(true);

    }

    @SuppressWarnings("unused")
    @EventHandler
    public void chestClick(final InventoryClickEvent event) {

        Inventory chestInventory = event.getInventory();
        Player chestPlayer = (Player) event.getWhoClicked();

        if (chestInventory.getName().matches("§2Donation Chest") && event.getRawSlot() == -999)
            event.getView().close();

        if (chestInventory.getName().matches("§2Experience Chest") && event.getRawSlot() == -999)
            event.getView().close();

        if (chestInventory.getName().matches("§2Random Chest")) if (event.getRawSlot() == -999) event.getView().close();
        else if (event.getCurrentItem().getAmount() >= 1 && event.getRawSlot() >= 54) event.setCancelled(true);

        if (chestInventory.getName().matches("§6§lMixed Chests")) {
            switch (event.getRawSlot()) {

                case 0:
                    event.getView().close();
                    chestPlayer.openInventory(getChestDonationHolder().getInventory());
                    break;

                case 1:
                    event.getView().close();
                    chestPlayer.openInventory(getChestRandomHolder().getInventory());
                    break;

                case 2:
                    event.getView().close();
                    Inventory chestExpInventory = Bukkit.createInventory(
                            chestPlayer, 27, ChatColor.DARK_GREEN + "Experience Chest");
                    chestPlayer.openInventory(chestExpInventory);
                    break;

                case 3:
                    event.getView().close();
                    chestPlayer.sendMessage(getMsgPrefix() + "Holding chest coming soon!");
                    break;

                case 4:
                    event.getView().close();
                    chestPlayer.openInventory(getChestSwapHolder().getInventory());
                    break;

                default:
                    event.getView().close();
                    break;

            }
        }

    }

    @SuppressWarnings("unused")
    @EventHandler
    public void chestInteract(final InventoryInteractEvent event) {

        Inventory chestInventory = event.getInventory();

        if (chestInventory.getName().matches("§6§lMixed Chests")) {

            event.setCancelled(true);

        }

    }

    @SuppressWarnings("unused")
    @EventHandler
    public void chestClose(final InventoryCloseEvent event) {

        Inventory chestInventory = event.getInventory();
        Player chestPlayer = (Player) event.getPlayer();

        if (chestInventory.getName().matches("§2Experience Chest")) {
            ItemStack[] expItems = chestInventory.getContents();
            giveExp(chestPlayer, expItems);
            chestInventory.clear();
        }

        if (chestInventory.getName().matches("§2Swap Chest")) {
            ItemStack[] checkItems = chestInventory.getContents();

            for (ItemStack item : checkItems)
                if (item != null) {
                    if (chestSwapItemStack != null) for (ItemStack swapItem : chestSwapItemStack)
                        if (swapItem != null)
                            chestPlayer.getWorld().dropItemNaturally(chestPlayer.getEyeLocation(), swapItem);
                    chestSwapItemStack = checkItems;
                    break;
                }

            chestSwapBusy = false;
        }

    }

    @SuppressWarnings("unused")
    @EventHandler
    public void chestOpen(final InventoryOpenEvent event) {

        Inventory chestInventory = event.getInventory();

        if (chestInventory.getName().matches("§2Swap Chest")) {
            chestSwapBusy = true;
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