package me.skygod.crateloot.gui;

import me.skygod.crateloot.CrateLoot;
import me.skygod.crateloot.utils.CustomItems;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SortAnimation {

    public static final int VISIBLE_SLOTS = 5;
    public static ItemStack NOT_DEFINED = CustomItems.customItem(Material.POPPY, "§4Item not defined");
    public static int ANIMATION_DURATION = 4 * 1000;

    private final CrateLoot crateLoot;
    private final BasicGui gui;
    private List<ItemStack> items;

    public SortAnimation(CrateLoot crateLoot) {
        this(crateLoot, new ArrayList<>());
    }

    public SortAnimation(CrateLoot crateLoot, List<ItemStack> items) {
        this.crateLoot = crateLoot;
        this.gui = new BasicGui(crateLoot, 27, "Sorting...");

        setItems(items);
        insertGlasses();
    }

    public void start(HumanEntity entity, Consumer<ItemStack> whenEnd) {
        gui.openGui(entity);

        new BukkitRunnable() {
            int times;
            final long started = System.currentTimeMillis();

            @Override
            public void run() {
                if (System.currentTimeMillis() > (started + ANIMATION_DURATION)) {
                    whenEnd.accept(gui.getGui().getItem(13));
                    this.cancel();
                    return;
                }

                insertItemsInGui(times++);

                if (times > items.size()) {
                    times = 0;
                }

            }

        }.runTaskTimer(crateLoot, 0, 5);
    }

    private void insertItemsInGui(int dislocation) {
        int next = 0;
        for (int i = 0; i < VISIBLE_SLOTS; i++) {
            int dislocated = i + dislocation >= items.size() ? next++ : i + dislocation;
            gui.putItem(i + 11, items.get(dislocated));
        }
    }

    private void setupItems() {
        Collections.shuffle(items);

        if (items.size() < VISIBLE_SLOTS) {
            int times = items.size() == 0 ? VISIBLE_SLOTS : VISIBLE_SLOTS - items.size();
            for (int i = 0; i < times; i++) {
                items.add(NOT_DEFINED);
            }
        }
    }

    private void insertGlasses() {
        final ItemStack redGlass = CustomItems.customItem(Material.RED_STAINED_GLASS_PANE, "§4Sorting...");
        final ItemStack greenGlass = CustomItems.customItem(Material.GREEN_STAINED_GLASS_PANE, "§2Sorting...");

        for (int i = 0; i < 9; i++) {
            gui.putItem(i, redGlass);
            gui.putItem(i + 18, redGlass);
        }

        gui.putItem(9, redGlass);
        gui.putItem(10, redGlass);
        gui.putItem(16, redGlass);
        gui.putItem(17, redGlass);

        gui.putItem(4, greenGlass);
        gui.putItem(22, greenGlass);
    }

    public void setItems(List<ItemStack> items) {
        this.items = new ArrayList<>(items);
        setupItems();
    }

}
