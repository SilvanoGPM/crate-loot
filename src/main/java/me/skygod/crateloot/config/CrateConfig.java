package me.skygod.crateloot.config;

import me.skygod.crateloot.utils.CustomItems;
import me.skygod.crateloot.utils.MyStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CrateConfig {

    private static CrateConfig instance;

    public final static File CRATES_FILE = new File("./plugins/CrateLoot/crates.yml");
    public final static File CONFIG_FILE = new File("./plugins/CrateLoot/config.yml");

    private Map<ItemStack, List<ItemStack>> crates;
    private final YamlConfiguration cratesConfig;
    private final YamlConfiguration geralConfig;

    private CrateConfig() {
        this.cratesConfig = YamlConfiguration.loadConfiguration(CRATES_FILE);
        this.geralConfig = YamlConfiguration.loadConfiguration(CONFIG_FILE);

        if (!CRATES_FILE.exists()) {
            createNewCrates();
        }

        if (!CONFIG_FILE.exists()) {
            createNewConfig();
        }

        loadCrates();
    }

    public static synchronized CrateConfig getInstance() {
        if (instance == null) {
            instance = new CrateConfig();
        }

        return instance;
    }

    public String getInventoryFullMessage() {
        final String inventoryFullMessage = YamlConfiguration
                .loadConfiguration(CONFIG_FILE)
                .getString("inventory-full", "&4Default inventory full message not found");

        return MyStringUtils.formatColor(inventoryFullMessage);
    }

    public String getSortingMessage() {
        final String sortingMessage = YamlConfiguration
                .loadConfiguration(CONFIG_FILE)
                .getString("sorting", "&4Default sorting message not found");

        return MyStringUtils.formatColor(sortingMessage);
    }

    public String getTimeoutMessage() {
        final String timeoutMessage = YamlConfiguration
                .loadConfiguration(CONFIG_FILE)
                .getString("timeout-message", "&4Default timeout message not found");

        return MyStringUtils.formatColor(timeoutMessage);
    }

    public int getTimeout() {
        return YamlConfiguration
                .loadConfiguration(CONFIG_FILE)
                .getInt("timeout", 4);
    }

    public void loadCrates() {
        final ConfigurationSection section = cratesConfig.getConfigurationSection("crates");

        if (section == null) return;

        final Map<ItemStack, List<ItemStack>> allItems = new HashMap<>();

        for (String key : section.getKeys(false)) {
            String name = MyStringUtils.formatColor(section.getString(key + ".name"));
            Material material = Material.valueOf(section.getString(key + ".material"));
            List<ItemStack> content = (List<ItemStack>) section.getList(key + ".content");

            allItems.put(CustomItems.customItem(material, name), content);
        }

        this.crates = allItems;
    }

    public void saveCrates() {
        final ConfigurationSection section = cratesConfig.getConfigurationSection("crates");

        crates.forEach((crate, content) -> {
            final String key = MyStringUtils.formatToKey(crate.getItemMeta().getDisplayName());
            final String name = MyStringUtils.formatColorToSave(crate.getItemMeta().getDisplayName());
            final String material = crate.getType().name();

            section.set(key, crate);
            section.set(key + ".name", name);
            section.set(key + ".material", material);
            section.set(key + ".content", content);

            Bukkit.getLogger().info(key + " as saved");
        });

        try {
            cratesConfig.save(CRATES_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getLogger().info("All crates saved!");
    }

    public boolean remove(ItemStack item) {
        if (crates.containsKey(item)) {
            crates.remove(item);

            final ConfigurationSection section = cratesConfig.getConfigurationSection("crates");
            final String key = MyStringUtils.formatToKey(item.getItemMeta().getDisplayName());

            section.set(key, null);

            Bukkit.getLogger().info(key + " removed");
            return true;
        }

        return false;
    }

    private void createNewCrates() {
        final List<ItemStack> content = new ArrayList<>();
        content.add(new ItemStack(Material.DIAMOND, 32));
        content.add(new ItemStack(Material.EMERALD, 32));
        content.add(new ItemStack(Material.COAL, 64));
        content.add(new ItemStack(Material.IRON_INGOT, 64));
        content.add(new ItemStack(Material.GOLD_INGOT, 48));

        final ConfigurationSection section = cratesConfig.createSection("crates");
        final String defaultKey = "default";

        section.set(defaultKey, new HashMap<>());
        section.set(defaultKey + ".name", "&7Default Crate");
        section.set(defaultKey + ".material", Material.TRAPPED_CHEST.toString());
        section.set(defaultKey + ".content", content);

        try {
            cratesConfig.save(CRATES_FILE);
            Bukkit.getLogger().info("Default crates crated!");
        } catch (IOException e) {
            Bukkit.getLogger().warning("Default crates error to crate!");
            e.printStackTrace();
        }
    }

    private void createNewConfig() {
        geralConfig.set("inventory-full", "&4Seu inventário esta lotado!");
        geralConfig.set("sorting", "&2Sorteando item...");
        geralConfig.set("timeout", 4);
        geralConfig.set("timeout-message", "&4%d segundos para abrir outra caixa");

        try {
            geralConfig.save(CONFIG_FILE);
            Bukkit.getLogger().warning("Config crates created!");
        } catch (IOException e) {
            Bukkit.getLogger().warning("Config crates error to crate!");
            e.printStackTrace();
        }
    }

    public Map<ItemStack, List<ItemStack>> getCrates() {
        return crates;
    }

}
