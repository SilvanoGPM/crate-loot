package me.skygod.crateloot.gui;

import me.skygod.crateloot.CrateLoot;
import me.skygod.crateloot.config.CrateConfig;
import me.skygod.crateloot.utils.CustomItems;
import me.skygod.crateloot.utils.MyStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NewCrateGui implements IGui {

    public final static ItemStack NEXT_ITEM = CustomItems.customItem(
            Material.CYAN_WOOL,
            "§3Next",
            "§2Insert items for crate", "§2When everything stops inserted only close inventory"
    );

    public final static ItemStack INFO_ITEM = CustomItems.customItem(
            Material.GRAY_STAINED_GLASS_PANE,
            "§8Click in the pattern item for crate"
    );

    public final static int GUI_SIZE = 9;

    private final CrateLoot crateLoot;
    private final Inventory gui;
    private final Inventory crateContent;
    private ItemStack patternItem;

    public NewCrateGui(CrateLoot crateLoot) {
        this.crateLoot = crateLoot;
        this.crateLoot.getServer().getPluginManager().registerEvents(this, this.crateLoot);

        this.gui = Bukkit.createInventory(null, GUI_SIZE, "New crate");
        this.crateContent = Bukkit.createInventory(null, 27, "Crate content");

        setupGui();
    }

    private void setupGui() {
        gui.setItem(1, INFO_ITEM);
        gui.setItem(8, NEXT_ITEM);
    }

    public void openGui(HumanEntity entity) {
        entity.openInventory(gui);
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (event.getInventory() != gui) return;
        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getInventory();
        final ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem != null) {

            final boolean itemIsNotInTheGui = event.getRawSlot() > GUI_SIZE - 1;
            if (itemIsNotInTheGui) {
                inventory.setItem(0, clickedItem);
            }

            final ItemStack item = inventory.getItem(0);

            if (item != null && clickedItem.isSimilar(NEXT_ITEM)) {

                final String name = item.getItemMeta().getDisplayName();
                if (!name.isEmpty()) {
                    patternItem = CustomItems.customItem(item.getType(), MyStringUtils.formatColor(name));
                    player.openInventory(crateContent);
                } else {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "Rename the pattern item!");
                }

            }

        }

    }

    @EventHandler
    public void onGuiDrag(InventoryDragEvent event) {
        if (event.getInventory() == gui) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() != crateContent) return;

        final List<ItemStack> items = Arrays.stream(crateContent.getContents())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        final HumanEntity player = event.getPlayer();

        if (items.size() < 5) {
            player.sendMessage(ChatColor.RED + "A crate needs at least 5 items");
        } else {
            CrateConfig.getInstance().getCrates().put(this.patternItem, items);
            player.sendMessage(ChatColor.GREEN + "Crate created!");
        }

    }

}
