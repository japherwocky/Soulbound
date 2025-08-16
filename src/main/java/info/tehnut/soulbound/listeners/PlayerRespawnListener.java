package info.tehnut.soulbound.listeners;

import info.tehnut.soulbound.SoulboundPlugin;
import info.tehnut.soulbound.persistence.SoulboundStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.UUID;

public class PlayerRespawnListener implements Listener {

    private final SoulboundPlugin plugin;

    public PlayerRespawnListener(SoulboundPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Check if player has stored soulbound items
        List<SoulboundStorage.SoulboundItem> soulboundItems = plugin.getStorage().getSoulboundItems(playerId);
        if (soulboundItems == null || soulboundItems.isEmpty()) {
            return;
        }
        
        plugin.debug("Restoring " + soulboundItems.size() + " soulbound items for player " + player.getName());
        
        // Restore items to player inventory
        PlayerInventory inventory = player.getInventory();
        for (SoulboundStorage.SoulboundItem soulboundItem : soulboundItems) {
            ItemStack item = soulboundItem.getItemStack();
            int originalSlot = soulboundItem.getSlot();
            
            // Try to restore to original slot if possible
            if (originalSlot >= 0 && originalSlot < inventory.getSize() && inventory.getItem(originalSlot) == null) {
                inventory.setItem(originalSlot, item);
                plugin.debug("Restored item to original slot " + originalSlot);
            } else {
                // Otherwise add to first available slot
                inventory.addItem(item);
                plugin.debug("Restored item to first available slot");
            }
        }
        
        // Clear stored items for this player
        plugin.getStorage().clearSoulboundItems(playerId);
    }
}

