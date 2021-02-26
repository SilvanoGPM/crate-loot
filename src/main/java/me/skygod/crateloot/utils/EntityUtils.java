package me.skygod.crateloot.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class EntityUtils {

    public static void spawnFireworks(Location location, Color color, int amount, int power) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.setPower(power);
        fireworkMeta.addEffect(FireworkEffect.builder().withColor(color).flicker(true).build());

        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();

        for (int i = 0; i < amount; i++) {
            Firework firework2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            firework2.setFireworkMeta(fireworkMeta);
        }

    }

}
