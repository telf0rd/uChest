package com.ullarah.uchest;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

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

        if (chestInventory.getName().matches("§2Random Chest")) if (event.getRawSlot() == -999) event.getView().close();
        else if (event.getCurrentItem().getAmount() >= 1 && event.getRawSlot() >= 54) event.setCancelled(true);


    }

}