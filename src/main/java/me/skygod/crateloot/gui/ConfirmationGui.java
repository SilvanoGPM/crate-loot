package me.skygod.crateloot.gui;

import me.skygod.crateloot.CrateLoot;
import me.skygod.crateloot.utils.CustomItems;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;

import java.util.function.Consumer;

public class ConfirmationGui {

    public final CrateLoot crateLoot;
    public final BasicGui gui;

    private Consumer<HumanEntity> whenAccept;
    private Consumer<HumanEntity> whenDecline;

    public ConfirmationGui(CrateLoot crateLoot) {
        this.crateLoot = crateLoot;
        gui = new BasicGui(this.crateLoot, 27, "Confirm?");
    }

    private void setupGui() {
        gui.putItem(12, CustomItems.customItem(Material.GREEN_WOOL, "§2Confirm"), whenAccept);
        gui.putItem(14, CustomItems.customItem(Material.RED_WOOL, "§4Decline"), whenDecline);
    }

    public void openGui(HumanEntity entity) {
        setupGui();
        gui.openGui(entity);
    }

    public Consumer<HumanEntity> getWhenAccept() {
        return whenAccept;
    }

    public void setWhenAccept(Consumer<HumanEntity> whenAccept) {
        this.whenAccept = whenAccept;
    }

    public Consumer<HumanEntity> getWhenDecline() {
        return whenDecline;
    }

    public void setWhenDecline(Consumer<HumanEntity> whenDecline) {
        this.whenDecline = whenDecline;
    }
}
