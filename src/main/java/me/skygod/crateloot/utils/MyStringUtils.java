package me.skygod.crateloot.utils;

import org.bukkit.ChatColor;

public class MyStringUtils {

    public final static char CHAR = '&';

    public static String formatColor(String string) {
        return string == null
                ? ""
                : ChatColor.translateAlternateColorCodes(CHAR, string);
    }

    public static String formatColorToSave(String string) {
        return string == null
                ? ""
                : string.replaceAll("§+", Character.toString(CHAR));
    }

    public static String formatToKey(String string) {
        return ChatColor.stripColor(formatColor(string))
                .toLowerCase()
                .replace(" ", "_");
    }

}
