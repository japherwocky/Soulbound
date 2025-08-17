package com.japherwocky.soulbound.api;

import com.japherwocky.soulbound.SoulboundPlugin;
import com.japherwocky.soulbound.enchantment.SoulboundEnchantment;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Public API for interacting with the Soulbound enchantment.
 */
@SuppressWarnings("UnstableApiUsage")
public class SoulboundAPI {

    private static SoulboundPlugin plugin;
    private static Enchantment soulboundEnchantment;

    /**
     * Initialize the API with the plugin instance.
     * This is called automatically by the plugin.
     *
     * @param plugin The Soulbound plugin instance
     */
    public static void init(SoulboundPlugin plugin) {
        SoulboundAPI.plugin = plugin;
        
        // Get the enchantment from the registry if not already available
        if (plugin.getSoulboundEnchantment() != null) {
            soulboundEnchantment = plugin.getSoulboundEnchantment();
        } else {
            Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
            soulboundEnchantment = registry.get(SoulboundEnchantment.KEY);
            
            if (soulboundEnchantment == null) {
                plugin.getLogger().severe("Failed to get Soulbound enchantment from registry in API initialization!");
            }
        }
    }

    /**
     * Checks if an item has the Soulbound enchantment.
     *
     * @param item The item to check
     * @return true if the item has the Soulbound enchantment, false otherwise
     */
    public static boolean hasSoulbound(ItemStack item) {
        if (item == null || !item.hasItemMeta() || soulboundEnchantment == null) {
            return false;
        }
        
        return item.containsEnchantment(soulboundEnchantment);
    }

    /**
     * Adds the Soulbound enchantment to an item.
     *
     * @param item The item to add the enchantment to
     * @return The modified item with the Soulbound enchantment
     */
    public static ItemStack addSoulbound(ItemStack item) {
        if (item == null || soulboundEnchantment == null) {
            return item;
        }
        
        ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : plugin.getServer().getItemFactory().getItemMeta(item.getType());
        if (meta == null) {
            return item;
        }
        
        meta.addEnchant(soulboundEnchantment, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Removes the Soulbound enchantment from an item.
     *
     * @param item The item to remove the enchantment from
     * @return The modified item without the Soulbound enchantment
     */
    public static ItemStack removeSoulbound(ItemStack item) {
        if (item == null || !item.hasItemMeta() || soulboundEnchantment == null) {
            return item;
        }
        
        if (item.containsEnchantment(soulboundEnchantment)) {
            ItemMeta meta = item.getItemMeta();
            meta.removeEnchant(soulboundEnchantment);
            item.setItemMeta(meta);
        }
        
        return item;
    }

    /**
     * Gets the Soulbound enchantment instance.
     *
     * @return The Soulbound enchantment
     */
    public static Enchantment getSoulboundEnchantment() {
        return soulboundEnchantment;
    }
}
