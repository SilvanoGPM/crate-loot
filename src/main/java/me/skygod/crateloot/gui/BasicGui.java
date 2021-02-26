package me.skygod.crateloot.gui;

import me.skygod.crateloot.CrateLoot;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BasicGui implements IGui {

    private final CrateLoot crateLoot;
    private final int size;
    private final String title;
    private final Inventory gui;
    private final Map<ItemStack, Consumer<HumanEntity>> actions;

    public BasicGui(CrateLoot crateLoot, int size, String title) {
        this.crateLoot = crateLoot;
        this.crateLoot.getServer().getPluginManager().registerEvents(this, this.crateLoot);

        this.size = size;
        this.title = title;
        this.actions = new HashMap<>();
        this.gui = Bukkit.createInventory(null, this.size, this.title);
    }

    public void putItem(int index, ItemStack item, Consumer<HumanEntity> action) {
        putItem(index, item);
        actions.putIfAbsent(item, action);
    }

    public void putItem(int index, ItemStack item) {
        gui.setItem(index, item);
    }

    public void clear() {
        this.actions.clear();
        this.gui.clear();
    }

    public void openGui(HumanEntity humanEntity) {
        humanEntity.openInventory(gui);
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (event.getInventory() != gui) return;
        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();

        final boolean isAValidItem = clickedItem != null &&
                !clickedItem.getType().isAir() &&
                event.getRawSlot() <= size;

        if (isAValidItem) {
            final HumanEntity player = event.getWhoClicked();

            final Consumer<HumanEntity> action = actions.get(clickedItem);
            if (action != null) {
                action.accept(player);
            }

        }
    }

    @EventHandler
    public void onGuiDrag(InventoryDragEvent event) {
        if (event.getInventory() == gui) {
            event.setCancelled(true);
        }
    }

    public Map<ItemStack, Consumer<HumanEntity>> getActions() {
        return actions;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public Inventory getGui() {
        return gui;
    }
}
