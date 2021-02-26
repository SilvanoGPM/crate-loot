package me.skygod.crateloot.listener;

import me.skygod.crateloot.CrateLoot;
import me.skygod.crateloot.classes.CooldownManager;
import me.skygod.crateloot.config.CrateConfig;
import me.skygod.crateloot.gui.BasicGui;
import me.skygod.crateloot.gui.SortAnimation;
import me.skygod.crateloot.utils.EntityUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CrateLootListener implements Listener {

    private final CrateLoot crateLoot;
    private final CrateConfig config;
    private final BasicGui crateGui;

    private final Map<UUID, ItemStack> offlinePlayers;
    private final CooldownManager cooldownManager;

    public CrateLootListener(CrateLoot crateLoot) {
        this.crateLoot = crateLoot;
        this.crateLoot.getServer().getPluginManager().registerEvents(this, this.crateLoot);

        this.crateGui = new BasicGui(this.crateLoot, 27, "Crate content");

        this.config = CrateConfig.getInstance();

        this.offlinePlayers = new HashMap<>();
        this.cooldownManager = new CooldownManager();
    }

    private void putItemsInGui(List<ItemStack> items) {
        for (int i = 0; i < crateGui.getSize(); i++) {
            final ItemStack item = i < items.size()
                    ? items.get(i)
                    : new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);

            crateGui.putItem(i, item);
        }
    }

    private void playerGetItemLogic(Player player, ItemStack sortedItem) {
        final PlayerInventory inventory = player.getInventory();

        if (player.isOnline()) {
            EntityUtils.spawnFireworks(player.getLocation(), Color.PURPLE, 2, 1);
            if (inventory.firstEmpty() != -1) {
                inventory.addItem(sortedItem);
            } else {
                player.getWorld().dropItem(player.getLocation(), sortedItem);
            }

            String receivedMessage = "Você ganhou: ";

            if (sortedItem.hasItemMeta() && !sortedItem.getItemMeta().getDisplayName().isEmpty()) {
                receivedMessage += ChatColor.stripColor(sortedItem.getItemMeta().getDisplayName());
            } else {
                receivedMessage += StringUtils
                        .capitalize(sortedItem.getType()
                                .name().toLowerCase())
                        .replace("_", " ");
            }

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent
                    .fromLegacyText(ChatColor.GOLD + receivedMessage));

            return;
        }

        offlinePlayers.put(player.getUniqueId(), sortedItem);
    }

    @EventHandler()
    public void onCrateInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final PlayerInventory inventory = player.getInventory();

        boolean isCrate = config.getCrates().keySet()
                .stream()
                .anyMatch(crate -> crate.isSimilar(inventory.getItemInMainHand()));

        if (isCrate) {
            final Action action = event.getAction();
            final ItemStack crate = inventory.getItemInMainHand();

            config.getCrates().entrySet().stream()
                    .filter(entry -> entry.getKey().isSimilar(crate))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .ifPresent(crateItems -> {

                        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
                            event.setCancelled(true);
                            return;
                        }

                        if (action == Action.RIGHT_CLICK_AIR) {

                            final boolean hasSpace = inventory.firstEmpty() != -1 || crate.getAmount() == 1;
                            String message = config.getSortingMessage();

                            if (hasSpace) {
                                final long timeLeft = System.currentTimeMillis() - cooldownManager
                                        .getCooldown(player.getUniqueId());

                                if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= config.getTimeout()) {
                                    crate.setAmount(crate.getAmount() - 1);
                                    player.updateInventory();

                                    final SortAnimation sortAnimation = new SortAnimation(crateLoot, crateItems);
                                    sortAnimation.start(player, sortedItem -> playerGetItemLogic(player, sortedItem));

                                    cooldownManager.setCooldown(player.getUniqueId(), System.currentTimeMillis());
                                } else {

                                    long seconds = config.getTimeout() - TimeUnit.MILLISECONDS
                                            .toSeconds(timeLeft);
                                    message = String.format(config.getTimeoutMessage(), seconds);
                                }

                            } else {
                                message = config.getInventoryFullMessage();
                            }

                            player.spigot()
                                    .sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                        }

                        if (action == Action.LEFT_CLICK_AIR) {
                            putItemsInGui(crateItems);
                            crateGui.openGui(player);
                        }

                    });

        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final UUID uniqueId = event.getPlayer().getUniqueId();

        if (offlinePlayers.containsKey(uniqueId)) {
            playerGetItemLogic(Bukkit.getPlayer(uniqueId), offlinePlayers.get(uniqueId));
            offlinePlayers.remove(uniqueId);
        }

    }

}
