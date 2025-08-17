package com.japherwocky.soulbound.util;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemType;

/**
 * Utility class for enchantment tag-related functionality.
 * This class is separate from the Enchantment class to avoid early class loading issues.
 */
@SuppressWarnings("UnstableApiUsage")
public class EnchantmentTags {
    
    /**
     * The key for the Soulbound enchantment.
     */
    public static final Key SOULBOUND_KEY = Key.key("soulbound:soulbound");
    
    /**
     * Gets the tag key for items that can be enchanted with Soulbound.
     * 
     * @return The tag key for supported items
     */
    public static TagKey<ItemType> getSoulboundSupportedItemsTag() {
        return TagKey.create(RegistryKey.ITEM, Key.key("soulbound:soulbound_enchantable"));
    }
}

