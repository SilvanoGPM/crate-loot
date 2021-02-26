package me.skygod.crateloot.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CustomItems {

    public static ItemStack customItem(Material material, String name, String... lore) {
        final ItemStack itemStack = new ItemStack(material, 1);

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
