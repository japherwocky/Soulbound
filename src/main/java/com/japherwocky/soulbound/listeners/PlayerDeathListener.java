package com.japherwocky.soulbound.listeners;

import com.japherwocky.soulbound.SoulboundPlugin;
import com.japherwocky.soulbound.enchantment.SoulboundEnchantment;
import com.japherwocky.soulbound.persistence.SoulboundStorage;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class PlayerDeathListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment soulbound;
    private final SoulboundPlugin plugin;

    public PlayerDeathListener(SoulboundPlugin plugin) {
        this.plugin = plugin;
        this.soulbound = registry.get(SoulboundEnchantment.KEY);
        
        if (soulbound == null) {
            plugin.getLogger().severe("Failed to get Soulbound enchantment from registry! The enchantment may not work correctly.");
        } else {
            plugin.getLogger().info("Successfully obtained Soulbound enchantment from registry!");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (soulbound == null) {
            plugin.getLogger().warning("Soulbound enchantment is null! Cannot process death event.");
            return;
        }
        
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        PlayerInventory inventory = player.getInventory();
        List<SoulboundStorage.SoulboundItem> soulboundItems = new ArrayList<>();
        
        // Process inventory items
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack itemStack = inventory.getItem(slot);
            if (itemStack != null && itemStack.containsEnchantment(soulbound)) {
                plugin.debug("Found soulbound item: " + itemStack.getType() + " in slot " + slot);
                
                // Store the item for respawn
                soulboundItems.add(new SoulboundStorage.SoulboundItem(itemStack.clone(), slot));
                
                // Add the item to the keep list
                event.getItemsToKeep().add(itemStack);
                event.getDrops().remove(itemStack);
                
                // Check if the enchantment should be removed
                if (plugin.shouldRemoveEnchantment()) {
                    plugin.debug("Removing Soulbound enchantment from item");
                    itemStack.removeEnchantment(soulbound);
                }
            }
        }
        
        // Store soulbound items for this player
        if (!soulboundItems.isEmpty()) {
            plugin.debug("Storing " + soulboundItems.size() + " soulbound items for player " + player.getName());
            plugin.getStorage().storeSoulboundItems(playerId, soulboundItems);
        }
    }
}
