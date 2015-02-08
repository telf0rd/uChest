package com.ullarah.uchest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Init extends JavaPlugin {

    private static final Inventory chestDonationInventory = Bukkit.createInventory(getChestDonationHolder(), 54,
            ChatColor.DARK_GREEN + "Donation Chest");
    private static final Inventory chestRandomInventory = Bukkit.createInventory(getChestDonationHolder(), 54,
            ChatColor.DARK_GREEN + "Random Chest");
    private static Plugin plugin;
    private static String msgPrefix = null;
    private static String msgPermDeny = null;
    private static String msgNoConsole = null;

    private static Boolean maintenanceCheck;
    private static String maintenanceMessage;
    private static InventoryHolder chestDonationHolder = new InventoryHolder() {
        @Override
        public Inventory getInventory() {
            return getChestDonationInventory();
        }
    };
    private static InventoryHolder chestRandomHolder = new InventoryHolder() {
        @Override
        public Inventory getInventory() {
            return getChestRandomInventory();
        }
    };

    public static Plugin getPlugin() {
        return plugin;
    }

    private static void setPlugin(Plugin plugin) {
        Init.plugin = plugin;
    }

    public static String getMsgPrefix() {
        return msgPrefix;
    }

    private static void setMsgPrefix(String msgPrefix) {
        Init.msgPrefix = msgPrefix;
    }

    public static String getMsgPermDeny() {
        return msgPermDeny;
    }

    private static void setMsgPermDeny(String msgPermDeny) {
        Init.msgPermDeny = msgPermDeny;
    }

    public static String getMsgNoConsole() {
        return msgNoConsole;
    }

    private static void setMsgNoConsole(String msgNoConsole) {
        Init.msgNoConsole = msgNoConsole;
    }

    public static Boolean getMaintenanceCheck() {
        return maintenanceCheck;
    }

    public static void setMaintenanceCheck(Boolean maintenanceCheck) {
        Init.maintenanceCheck = maintenanceCheck;
    }

    public static String getMaintenanceMessage() {
        return maintenanceMessage;
    }

    private static void setMaintenanceMessage(String maintenanceMessage) {
        Init.maintenanceMessage = maintenanceMessage;
    }

    public static InventoryHolder getChestDonationHolder() {
        return chestDonationHolder;
    }

    private static void setChestDonationHolder(InventoryHolder chestDonationHolder) {
        Init.chestDonationHolder = chestDonationHolder;
    }

    public static InventoryHolder getChestRandomHolder() {
        return chestRandomHolder;
    }

    private static void setChestRandomHolder(InventoryHolder chestRandomHolder) {
        Init.chestRandomHolder = chestRandomHolder;
    }

    public static Inventory getChestDonationInventory() {
        return chestDonationInventory;
    }

    public static Inventory getChestRandomInventory() {
        return chestRandomInventory;
    }

    @SuppressWarnings("serial")
    public void onEnable() {

        setMsgPrefix(ChatColor.GOLD + "[uChest] " + ChatColor.WHITE);

        setPlugin(this);

        PluginManager pluginManager = getServer().getPluginManager();

        getConfig().options().copyDefaults(true);
        saveConfig();

        getCommand("chest").setExecutor(new Commands());
        getCommand("dchest").setExecutor(new Commands());
        getCommand("rchest").setExecutor(new Commands());

        pluginManager.registerEvents(new Events(), getPlugin());

        setMsgPermDeny(getMsgPrefix() + ChatColor.RED + "No permission.");
        setMsgNoConsole(getMsgPrefix() + ChatColor.RED + "No console usage.");

        setMaintenanceCheck(Init.getPlugin().getConfig().getBoolean("maintenance"));
        setMaintenanceMessage(getMsgPrefix() + ChatColor.RED + "The Chest System is unavailable.");

        Task.runCleanChest();
        Task.runAnnouncements();
        Task.runRandomTimer();

        setChestDonationHolder(chestDonationHolder);
        setChestRandomHolder(chestRandomHolder);

    }

    public void onDisable() {
    }

}
