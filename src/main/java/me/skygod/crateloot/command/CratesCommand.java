package me.skygod.crateloot.command;

import me.skygod.crateloot.CrateLoot;
import me.skygod.crateloot.config.CrateConfig;
import me.skygod.crateloot.gui.BasicGui;
import me.skygod.crateloot.gui.ConfirmationGui;
import me.skygod.crateloot.gui.NewCrateGui;
import me.skygod.crateloot.utils.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class CratesCommand implements CommandExecutor {

    public final static String COMMAND_NAME = "crates";

    private final CrateLoot crateLoot;
    private final CrateConfig config;

    private final BasicGui firstGui;
    private final BasicGui allCratesGui;
    private final BasicGui removeCratesGui;
    private final ConfirmationGui confirmationRemoveGui;

    public CratesCommand(CrateLoot crateLoot) {
        this.crateLoot = crateLoot;
        this.crateLoot.getCommand(COMMAND_NAME).setExecutor(this);

        config = CrateConfig.getInstance();

        firstGui = new BasicGui(this.crateLoot, 27, "Config crates");
        allCratesGui = new BasicGui(this.crateLoot, 54, "All crates");
        removeCratesGui = new BasicGui(this.crateLoot, 54, "Remove crate");
        confirmationRemoveGui = new ConfirmationGui(crateLoot);

        setupFirstGui();
        setupAllCrates();
    }

    private void setupAllCrates() {
        loadCratesInGui(allCratesGui, item -> entity -> {
            entity.getInventory().addItem(item);
        });
    }

    private void setupRemoveCrate() {
        loadCratesInGui(removeCratesGui, item -> entity -> {
            setupRemoveConfirmation(item);
            confirmationRemoveGui.openGui(entity);
        });
    }

    private void setupRemoveConfirmation(ItemStack item) {
        confirmationRemoveGui.setWhenAccept(entity -> {
            String message = "Crate removed!";

            final boolean removed = config.remove(item);
            if (!removed) {
                message = "This crate not exists!";
            }

            entity.sendMessage(ChatColor.DARK_RED + message);
            entity.closeInventory();
        });

        confirmationRemoveGui.setWhenDecline(HumanEntity::closeInventory);
    }

    private void loadCratesInGui(BasicGui gui, Function<ItemStack, Consumer<HumanEntity>> foreach) {
        gui.clear();

        final ArrayList<ItemStack> allCrates = new ArrayList<>(config.getCrates().keySet());

        for (int i = 0; i < allCrates.size(); i++) {
            final ItemStack item = allCrates.get(i);
            gui.putItem(i, item, foreach.apply(item));
        }

    }

    private void setupFirstGui() {
        final ItemStack viewAllCrates = CustomItems.customItem(Material.CHEST, "§6View all crates");
        final ItemStack newCrate = CustomItems.customItem(Material.GRAY_STAINED_GLASS, "§8New crate");
        final ItemStack removeCrate = CustomItems.customItem(Material.BARRIER, "§cRemove crate");

        firstGui.putItem(10, viewAllCrates, entity -> {
            setupAllCrates();
            allCratesGui.openGui(entity);
        });

        firstGui.putItem(13, newCrate, entity -> {
            final NewCrateGui newCrateGui = new NewCrateGui(crateLoot);
            newCrateGui.openGui(entity);
        });

        firstGui.putItem(16, removeCrate, entity -> {
            setupRemoveCrate();
            removeCratesGui.openGui(entity);
        });

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
        firstGui.openGui(player);

        return true;
    }

}
