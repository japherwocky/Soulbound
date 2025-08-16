package info.tehnut.soulbound.api;

import info.tehnut.soulbound.SoulboundPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * API for interacting with the Soulbound plugin.
 */
public class SoulboundAPI {

    private static SoulboundPlugin plugin;

    /**
     * Initializes the API with the plugin instance.
     * This is called automatically by the plugin.
     *
     * @param plugin The SoulboundPlugin instance
     */
    public static void init(SoulboundPlugin plugin) {
        SoulboundAPI.plugin = plugin;
    }

    /**
     * Gets the Soulbound enchantment.
     *
     * @return The Soulbound enchantment
     */
    public static Enchantment getSoulboundEnchantment() {
        return plugin.getSoulboundEnchantment();
    }

    /**
     * Gets the NamespacedKey for the Soulbound enchantment.
     *
     * @return The NamespacedKey
     */
    public static NamespacedKey getSoulboundKey() {
        return plugin.getSoulboundKey();
    }

    /**
     * Checks if an ItemStack has the Soulbound enchantment.
     *
     * @param item The ItemStack to check
     * @return true if the item has the Soulbound enchantment, false otherwise
     */
    public static boolean hasSoulbound(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        return item.getItemMeta().hasEnchant(getSoulboundEnchantment());
    }

    /**
     * Adds the Soulbound enchantment to an ItemStack.
     *
     * @param item The ItemStack to enchant
     * @return true if the enchantment was added, false otherwise
     */
    public static boolean addSoulbound(ItemStack item) {
        if (item == null) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        if (meta.hasEnchant(getSoulboundEnchantment())) {
            return true; // Already has the enchantment
        }
        
        meta.addEnchant(getSoulboundEnchantment(), 1, true);
        item.setItemMeta(meta);
        return true;
    }

    /**
     * Removes the Soulbound enchantment from an ItemStack.
     *
     * @param item The ItemStack to remove the enchantment from
     * @return true if the enchantment was removed, false otherwise
     */
    public static boolean removeSoulbound(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasEnchant(getSoulboundEnchantment())) {
            return false; // Doesn't have the enchantment
        }
        
        meta.removeEnchant(getSoulboundEnchantment());
        item.setItemMeta(meta);
        return true;
    }
}

