package info.tehnut.soulbound.listeners;

import info.tehnut.soulbound.SoulboundPlugin;
import info.tehnut.soulbound.api.SoulboundAPI;
import info.tehnut.soulbound.persistence.SoulboundStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlayerDeathListener implements Listener {

    private final SoulboundPlugin plugin;
    private final Random random = new Random();

    public PlayerDeathListener(SoulboundPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        List<ItemStack> drops = event.getDrops();
        List<SoulboundStorage.SoulboundItem> soulboundItems = new ArrayList<>();
        
        // Process inventory items
        Iterator<ItemStack> iterator = drops.iterator();
        while (iterator.hasNext()) {
            ItemStack item = iterator.next();
            if (item != null && SoulboundAPI.hasSoulbound(item)) {
                plugin.debug("Found soulbound item: " + item.getType());
                
                // Check if the enchantment should be removed
                boolean removeEnchant = random.nextDouble() < plugin.getRemovalChance();
                if (removeEnchant) {
                    plugin.debug("Removing Soulbound enchantment from item");
                    ItemMeta meta = item.getItemMeta();
                    meta.removeEnchant(plugin.getSoulboundEnchantment());
                    item.setItemMeta(meta);
                }
                
                // Store the item for restoration on respawn
                soulboundItems.add(new SoulboundStorage.SoulboundItem(item.clone(), player.getInventory().first(item)));
                
                // Remove the item from drops
                iterator.remove();
            }
        }
        
        // Store soulbound items for this player
        if (!soulboundItems.isEmpty()) {
            plugin.debug("Storing " + soulboundItems.size() + " soulbound items for player " + player.getName());
            plugin.getStorage().storeSoulboundItems(player.getUniqueId(), soulboundItems);
        }
    }
}

