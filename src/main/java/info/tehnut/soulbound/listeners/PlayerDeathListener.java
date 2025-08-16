package info.tehnut.soulbound.listeners;

import info.tehnut.soulbound.SoulboundPlugin;
import info.tehnut.soulbound.api.SoulboundItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class PlayerDeathListener implements Listener {

    private final SoulboundPlugin plugin;

    public PlayerDeathListener(SoulboundPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        List<SoulboundItemStack> soulboundItems = new ArrayList<>();
        
        // If keep inventory is true, we don't need to do anything
        if (event.getKeepInventory()) {
            return;
        }
        
        // Check for soulbound items in drops
        Iterator<ItemStack> iterator = event.getDrops().iterator();
        while (iterator.hasNext()) {
            ItemStack item = iterator.next();
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasEnchant(plugin.getSoulboundEnchantment())) {
                // This is a soulbound item, remove it from drops
                iterator.remove();
                
                // Store the item for restoration
                soulboundItems.add(new SoulboundItemStack(item.clone(), "main"));
                
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info("Saved soulbound item: " + item.getType() + " for player: " + player.getName());
                }
            }
        }
        
        // Check armor slots
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (int i = 0; i < armor.length; i++) {
            ItemStack item = armor[i];
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasEnchant(plugin.getSoulboundEnchantment())) {
                // Store the item for restoration
                soulboundItems.add(new SoulboundItemStack(item.clone(), "armor", i));
                
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info("Saved soulbound armor: " + item.getType() + " for player: " + player.getName());
                }
            }
        }
        
        // Check offhand
        ItemStack offhand = player.getInventory().getItemInOffHand();
        if (offhand != null && offhand.hasItemMeta() && offhand.getItemMeta().hasEnchant(plugin.getSoulboundEnchantment())) {
            // Store the item for restoration
            soulboundItems.add(new SoulboundItemStack(offhand.clone(), "offhand"));
            
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info("Saved soulbound offhand: " + offhand.getType() + " for player: " + player.getName());
            }
        }
        
        // Store the soulbound items for this player
        if (!soulboundItems.isEmpty()) {
            try {
                plugin.getStorage().storeSoulboundItems(player.getUniqueId(), soulboundItems);
                
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info("Stored " + soulboundItems.size() + " soulbound items for player: " + player.getName());
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to store soulbound items for player: " + player.getName(), e);
            }
        }
    }
}

