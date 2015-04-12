package com.ullarah.uchest;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * @author desht
 *         <p/>
 *         Adapted from ExperienceUtils code originally in ScrollingMenuSign.
 *         <p/>
 *         Credit to nisovin (http://forums.bukkit.org/threads/experienceutils-make-giving-taking-exp-a-bit-more-intuitive.54450/#post-1067480)
 *         for an implementation that avoids the problems of getTotalExperience(), which doesn't work properly after a player has enchanted something.
 *         <p/>
 *         Credit to comphenix for further contributions:
 *         See http://forums.bukkit.org/threads/experiencemanager-was-experienceutils-make-giving-taking-exp-a-bit-more-intuitive.54450/page-3#post-1273622
 */

class ExperienceManager {

    private static int hardMaxLevel = 100000;

    private static int xpTotalToReachLevel[];

    private final WeakReference<Player> player;
    private final String playerName;

    static {
        initLookupTables(25);
    }

    public ExperienceManager(Player player) {
        Validate.notNull(player, "Player cannot be null");
        this.player = new WeakReference<>(player);
        this.playerName = player.getName();
    }

    public static int getHardMaxLevel() {
        return hardMaxLevel;
    }

    public static void setHardMaxLevel(int hardMaxLevel) {
        ExperienceManager.hardMaxLevel = hardMaxLevel;
    }

    private static void initLookupTables(int maxLevel) {
        xpTotalToReachLevel = new int[maxLevel];

        for (int i = 0; i < xpTotalToReachLevel.length; i++)
            xpTotalToReachLevel[i] =
                    i >= 30 ? (int) (3.5 * i * i - 151.5 * i + 2220) :
                            i >= 16 ? (int) (1.5 * i * i - 29.5 * i + 360) :
                                    17 * i;
    }

    private static int calculateLevelForExp(int exp) {
        int level = 0;
        int curExp = 7; // level 1
        int inc = 10;

        while (curExp <= exp) {
            curExp += inc;
            level++;
            inc += (level % 2 == 0) ? 3 : 4;
        }
        return level;
    }

    Player getPlayer() {
        Player p = player.get();
        if (p == null) throw new IllegalStateException("Player " + playerName + " is not online");
        return p;
    }

    public void changeExp(int amt) {
        changeExp((double) amt);
    }

    public void changeExp(double amt) {
        setExp(getCurrentFractionalXP(), amt);
    }

    public void setExp(int amt) {
        setExp(0, amt);
    }

    public void setExp(double amt) {
        setExp(0, amt);
    }

    private void setExp(double base, double amt) {
        int xp = (int) Math.max(base + amt, 0);

        Player player = getPlayer();
        int curLvl = player.getLevel();
        int newLvl = getLevelForExp(xp);

        if (curLvl != newLvl) player.setLevel(newLvl);
        if (xp > base) player.setTotalExperience(player.getTotalExperience() + xp - (int) base);

        double pct = (base - getXpForLevel(newLvl) + amt) / (double) (getXpNeededToLevelUp(newLvl));
        player.setExp((float) pct);
    }

    int getCurrentExp() {
        Player player = getPlayer();

        int lvl = player.getLevel();
        return getXpForLevel(lvl) + Math.round(getXpNeededToLevelUp(lvl) * player.getExp());
    }

    private double getCurrentFractionalXP() {
        Player player = getPlayer();

        int lvl = player.getLevel();
        return getXpForLevel(lvl) + (double) (getXpNeededToLevelUp(lvl) * player.getExp());
    }

    public boolean hasExp(int amt) {
        return getCurrentExp() >= amt;
    }

    public boolean hasExp(double amt) {
        return getCurrentFractionalXP() >= amt;
    }

    int getLevelForExp(int exp) {
        if (exp <= 0) return 0;
        if (exp > xpTotalToReachLevel[xpTotalToReachLevel.length - 1]) {
            int newMax = calculateLevelForExp(exp) * 2;
            Validate.isTrue(newMax <= hardMaxLevel, "Level for exp " + exp + " > hard max level " + hardMaxLevel);
            initLookupTables(newMax);
        }
        int pos = Arrays.binarySearch(xpTotalToReachLevel, exp);
        return pos < 0 ? -pos - 2 : pos;
    }

    int getXpNeededToLevelUp(int level) {
        Validate.isTrue(level >= 0, "Level may not be negative.");
        return level > 30 ? 62 + (level - 30) * 7 : level >= 16 ? 17 + (level - 15) * 3 : 17;
    }

    int getXpForLevel(int level) {
        Validate.isTrue(level >= 0 && level <= hardMaxLevel, "Invalid level " + level + "(must be in range 0.." + hardMaxLevel + ")");
        if (level >= xpTotalToReachLevel.length) initLookupTables(level * 2);
        return xpTotalToReachLevel[level];
    }
}