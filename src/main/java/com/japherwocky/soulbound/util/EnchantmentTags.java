package com.japherwocky.soulbound.util;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;

import java.util.HashSet;
import java.util.Set;

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
    
    /**
     * Gets the tag entry for the Soulbound enchantment.
     * 
     * @return The tag entry for the Soulbound enchantment
     */
    public static TagEntry<Enchantment> getSoulboundTagEntry() {
        return TagEntry.valueEntry(TypedKey.create(RegistryKey.ENCHANTMENT, SOULBOUND_KEY));
    }
    
    /**
     * Gets the set of enchantment tag keys that the Soulbound enchantment should be registered with.
     * 
     * @return The set of enchantment tag keys
     */
    public static Set<TagKey<Enchantment>> getSoulboundEnchantmentTags() {
        Set<TagKey<Enchantment>> tags = new HashSet<>();
        
        // Add to in_enchanting_table tag to make it available in the enchanting table
        tags.add(EnchantmentTagKeys.IN_ENCHANTING_TABLE);
        
        // Add to discoverable tag to make it available in loot
        // Create our own tag key since EnchantmentTagKeys.DISCOVERABLE is not available in this version
        tags.add(TagKey.create(RegistryKey.ENCHANTMENT, Key.key("minecraft:discoverable")));
        
        return tags;
    }
}
