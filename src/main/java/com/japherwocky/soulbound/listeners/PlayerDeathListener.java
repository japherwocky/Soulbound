package com.japherwocky.soulbound.listeners;

import com.japherwocky.soulbound.SoulboundPlugin;
import com.japherwocky.soulbound.api.SoulboundAPI;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerDeathListener implements Listener {

    private final SoulboundPlugin plugin;
    private final Random random = new Random();
    private final Enchantment soulboundEnchantment;

    public PlayerDeathListener(SoulboundPlugin plugin) {
        this.plugin = plugin;
        this.soulboundEnchantment = plugin.getSoulboundEnchantment();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (soulboundEnchantment == null) {
            plugin.getLogger().warning("Soulbound enchantment is null! Cannot process death event.");
            return;
        }
        
        Player player = event.getEntity();
        List<ItemStack> itemsToRemoveFromDrops = new ArrayList<>();
        
        // Process inventory items
        player.getInventory().forEach(itemStack -> {
            if (itemStack != null && itemStack.containsEnchantment(soulboundEnchantment)) {
                plugin.debug("Found soulbound item: " + itemStack.getType());
                
                // Check if the enchantment should be removed
                boolean removeEnchant = random.nextDouble() < plugin.getRemovalChance();
                if (removeEnchant) {
                    plugin.debug("Removing Soulbound enchantment from item");
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.removeEnchant(soulboundEnchantment);
                    itemStack.setItemMeta(meta);
                }
                
                // Add the item to the keep list
                event.getItemsToKeep().add(itemStack);
                itemsToRemoveFromDrops.add(itemStack);
            }
        });
        
        // Remove the kept items from drops
        event.getDrops().removeAll(itemsToRemoveFromDrops);
        
        if (!itemsToRemoveFromDrops.isEmpty()) {
            plugin.debug("Keeping " + itemsToRemoveFromDrops.size() + " soulbound items for player " + player.getName());
        }
    }
}
