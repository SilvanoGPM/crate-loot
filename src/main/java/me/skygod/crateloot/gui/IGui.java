package me.skygod.crateloot.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface IGui extends Listener {

    @EventHandler
    void onGuiDrag(InventoryDragEvent event);

    @EventHandler
    void onGuiClick(InventoryClickEvent event);

    void openGui(HumanEntity entity);

}
