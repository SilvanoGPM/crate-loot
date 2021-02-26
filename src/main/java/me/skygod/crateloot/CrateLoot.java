package me.skygod.crateloot;

import me.skygod.crateloot.command.CratesCommand;
import me.skygod.crateloot.command.SortAnimationCommand;
import me.skygod.crateloot.config.CrateConfig;
import me.skygod.crateloot.listener.CrateLootListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrateLoot extends JavaPlugin {

    @Override
    public void onEnable() {
        new CrateLootListener(this);
        new CratesCommand(this);
        new SortAnimationCommand(this);
    }

    @Override
    public void onDisable() {
        CrateConfig.getInstance().saveCrates();
    }

}
