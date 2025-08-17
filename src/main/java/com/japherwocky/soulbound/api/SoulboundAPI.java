package com.japherwocky.soulbound.api;

import com.japherwocky.soulbound.SoulboundPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Public API for interacting with the Soulbound enchantment.
 */
public class SoulboundAPI {

    private static SoulboundPlugin plugin;

    /**
     * Initialize the API with the plugin instance.
     * This is called automatically by the plugin.
     *
     * @param plugin The Soulbound plugin instance
     */
    public static void init(SoulboundPlugin plugin) {
        SoulboundAPI.plugin = plugin;
    }

    /**
     * Checks if an item has the Soulbound enchantment.
     *
     * @param item The item to check
     * @return true if the item has the Soulbound enchantment, false otherwise
     */
    public static boolean hasSoulbound(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        return meta.hasEnchant(plugin.getSoulboundEnchantment());
    }

    /**
     * Adds the Soulbound enchantment to an item.
     *
     * @param item The item to add the enchantment to
     * @return The modified item with the Soulbound enchantment
     */
    public static ItemStack addSoulbound(ItemStack item) {
        if (item == null) {
            return null;
        }
        
        ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : plugin.getServer().getItemFactory().getItemMeta(item.getType());
        if (meta == null) {
            return item;
        }
        
        meta.addEnchant(plugin.getSoulboundEnchantment(), 1, true);
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
        if (item == null || !item.hasItemMeta()) {
            return item;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta.hasEnchant(plugin.getSoulboundEnchantment())) {
            meta.removeEnchant(plugin.getSoulboundEnchantment());
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
        return plugin.getSoulboundEnchantment();
    }
    
    /**
     * Creates an enchanted book with the Soulbound enchantment.
     *
     * @return An enchanted book with the Soulbound enchantment
     */
    public static ItemStack createSoulboundBook() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        
        if (meta != null) {
            Enchantment enchantment = plugin.getSoulboundEnchantment();
            if (plugin.isDebugEnabled()) {
                plugin.debug("Creating Soulbound book with enchantment: " + enchantment);
                plugin.debug("Enchantment key: " + enchantment.getKey());
                plugin.debug("Enchantment is discoverable: " + enchantment.isDiscoverable());
                plugin.debug("Enchantment is tradeable: " + enchantment.isTradeable());
            }
            
            meta.addStoredEnchant(enchantment, 1, true);
            book.setItemMeta(meta);
            
            if (plugin.isDebugEnabled()) {
                plugin.debug("Created Soulbound book: " + book);
                plugin.debug("Book meta: " + book.getItemMeta());
            }
        } else if (plugin.isDebugEnabled()) {
            plugin.debug("Failed to create Soulbound book: meta is null");
        }
        
        return book;
    }
    
    /**
     * Creates an enchanted book with the Soulbound enchantment and custom name/lore.
     *
     * @param name The custom name for the book (can be null for default)
     * @param lore The custom lore for the book (can be null for default)
     * @return An enchanted book with the Soulbound enchantment and custom name/lore
     */
    public static ItemStack createSoulboundBook(String name, List<String> lore) {
        ItemStack book = createSoulboundBook();
        ItemMeta meta = book.getItemMeta();
        
        if (meta != null) {
            if (name != null) {
                meta.setDisplayName(name);
            }
            
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(lore);
            }
            
            book.setItemMeta(meta);
        }
        
        return book;
    }
}
