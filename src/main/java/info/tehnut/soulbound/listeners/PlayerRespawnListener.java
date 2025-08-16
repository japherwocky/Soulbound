package info.tehnut.soulbound.listeners;

import info.tehnut.soulbound.SoulboundPlugin;
import info.tehnut.soulbound.api.SoulboundItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

public class PlayerRespawnListener implements Listener {

    private final SoulboundPlugin plugin;
    private final Random random = new Random();

    public PlayerRespawnListener(SoulboundPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        
        // Get stored soulbound items for this player
        List<SoulboundItemStack> soulboundItems = plugin.getStorage().getSoulboundItems(player.getUniqueId());
        
        if (soulboundItems == null || soulboundItems.isEmpty()) {
            return;
        }
        
        if (plugin.getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("Restoring " + soulboundItems.size() + " soulbound items for player: " + player.getName());
        }
        
        // Get the configured removal chance
        double removalChance = plugin.getConfig().getDouble("soulbound-removal-chance", 0.0);
        removalChance = Math.min(1.0, Math.max(0.0, removalChance)); // Clamp between 0.0 and 1.0
        
        // Restore the items
        for (SoulboundItemStack soulboundItem : soulboundItems) {
            ItemStack item = soulboundItem.getItemStack();
            
            // Check if the enchantment should be removed
            if (removalChance > 0.0 && random.nextDouble() <= removalChance) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.removeEnchant(plugin.getSoulboundEnchantment());
                    item.setItemMeta(meta);
                    
                    if (plugin.getConfig().getBoolean("debug", false)) {
                        plugin.getLogger().info("Removed Soulbound enchantment from item: " + item.getType());
                    }
                }
            }
            
            // Restore the item to the appropriate slot
            try {
                switch (soulboundItem.getInventoryType()) {
                    case "armor":
                        int slot = soulboundItem.getSlot();
                        if (slot >= 0 && slot < player.getInventory().getArmorContents().length) {
                            ItemStack[] armor = player.getInventory().getArmorContents();
                            armor[slot] = item;
                            player.getInventory().setArmorContents(armor);
                        } else {
                            player.getInventory().addItem(item);
                        }
                        break;
                    case "offhand":
                        player.getInventory().setItemInOffHand(item);
                        break;
                    case "main":
                    default:
                        player.getInventory().addItem(item);
                        break;
                }
                
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info("Restored soulbound item: " + item.getType() + " to player: " + player.getName());
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to restore soulbound item: " + item.getType(), e);
                // Fallback: add to main inventory
                player.getInventory().addItem(item);
            }
        }
        
        // Clear the stored items
        plugin.getStorage().clearSoulboundItems(player.getUniqueId());
    }
}

