package me.skygod.crateloot.command;

import me.skygod.crateloot.CrateLoot;
import me.skygod.crateloot.gui.SortAnimation;
import me.skygod.crateloot.utils.CustomItems;
import me.skygod.crateloot.utils.EntityUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SortAnimationCommand implements CommandExecutor {

    public static final String COMMAND_NAME = "sort-animation";

    private final CrateLoot crateLoot;
    private final List<ItemStack> items;

    public SortAnimationCommand(CrateLoot crateLoot) {
        this.crateLoot = crateLoot;
        this.crateLoot.getCommand(COMMAND_NAME).setExecutor(this);

        this.items = Arrays.asList(
                new ItemStack(Material.DIAMOND, 16),
                new ItemStack(Material.EMERALD, 16),
                new ItemStack(Material.COAL, 16),
                new ItemStack(Material.IRON_INGOT, 16),
                new ItemStack(Material.GOLD_INGOT, 16),
                new ItemStack(Material.WOODEN_AXE, 1),
                new ItemStack(Material.GOLDEN_BOOTS, 1),
                new ItemStack(Material.STONE_PICKAXE, 1),
                new ItemStack(Material.PLAYER_HEAD, 1),
                CustomItems.customItem(Material.ENDER_CHEST, ChatColor.GREEN + "Vip Crate"),
                CustomItems.customItem(Material.ENDER_CHEST, ChatColor.MAGIC + "Super Vip Crate")
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(COMMAND_NAME)) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players!");
            return true;
        }

        final Player player = (Player) sender;

        final SortAnimation sortAnimation = new SortAnimation(crateLoot);
        sortAnimation.setItems(items);

        sortAnimation.start(player, sortedItem -> {
            EntityUtils.spawnFireworks(player.getLocation(), Color.RED, 5, 1);
            player.sendMessage(ChatColor.GOLD + "Sorted!");
        });

        return true;
    }

}
