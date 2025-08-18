package com.japherwocky.soulbound.enchantment;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class SoulboundEnchantment {

    public static final Key KEY = Key.key("soulbound:soulbound");
    
    private final int anvilCost;
    private final int weight;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
    private final Set<TagKey<Enchantment>> enchantTagKeys = new HashSet<>();
    
    public SoulboundEnchantment(
            int anvilCost,
            int weight,
            EnchantmentRegistryEntry.EnchantmentCost minimumCost,
            EnchantmentRegistryEntry.EnchantmentCost maximumCost,
            Collection<TagKey<Enchantment>> enchantTagKeys,
            Collection<TagEntry<ItemType>> supportedItemTags
    ) {
        this.anvilCost = anvilCost;
        this.weight = weight;
        this.minimumCost = minimumCost;
        this.maximumCost = maximumCost;
        this.supportedItemTags.addAll(supportedItemTags);
        this.enchantTagKeys.addAll(enchantTagKeys);
    }
    
    @NotNull
    public Key getKey() {
        return KEY;
    }
    
    @NotNull
    public Component getDescription() {
        return Component.translatable("enchantment.soulbound.soulbound", "Keeps items in your inventory when you die");
    }
    
    public int getAnvilCost() {
        return anvilCost;
    }
    
    public int getMaxLevel() {
        return 1;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public EnchantmentRegistryEntry.@NotNull EnchantmentCost getMinimumCost() {
        return minimumCost;
    }
    
    public EnchantmentRegistryEntry.@NotNull EnchantmentCost getMaximumCost() {
        return maximumCost;
    }
    
    @NotNull
    public Iterable<EquipmentSlotGroup> getActiveSlots() {
        return Set.of(EquipmentSlotGroup.ANY);
    }
    
    @NotNull
    public Set<TagEntry<ItemType>> getSupportedItems() {
        return Collections.unmodifiableSet(supportedItemTags);
    }
    
    @NotNull
    public Set<TagKey<Enchantment>> getEnchantTagKeys() {
        return Collections.unmodifiableSet(enchantTagKeys);
    }
    
    @NotNull
    public TagKey<ItemType> getTagForSupportedItems() {
        return TagKey.create(RegistryKey.ITEM, Key.key(getKey().asString() + "_enchantable"));
    }
    
    @NotNull
    public TagEntry<Enchantment> getTagEntry() {
        return TagEntry.valueEntry(TypedKey.create(RegistryKey.ENCHANTMENT, getKey()));
    }
    
    public static SoulboundEnchantment create() {
        // Create a default configuration for the Soulbound enchantment
        return new SoulboundEnchantment(
                30, // anvilCost
                1,  // weight (rare)
                EnchantmentRegistryEntry.EnchantmentCost.of(20, 0), // minimumCost
                EnchantmentRegistryEntry.EnchantmentCost.of(50, 0), // maximumCost
                getDefaultEnchantmentTags(),
                getDefaultSupportedItems()
        );
    }
    
    private static Set<TagKey<Enchantment>> getDefaultEnchantmentTags() {
        Set<TagKey<Enchantment>> tags = new HashSet<>();
        
        // CRITICAL: Add to discoverable tag to make it available in creative mode
        tags.add(TagKey.create(RegistryKey.ENCHANTMENT, Key.key("minecraft:discoverable")));
        
        // Add to in_enchanting_table tag to make it available in the enchanting table
        tags.add(TagKey.create(RegistryKey.ENCHANTMENT, Key.key("minecraft:in_enchanting_table")));
        
        // Add to tradeable tag to make it available from villager trades
        tags.add(TagKey.create(RegistryKey.ENCHANTMENT, Key.key("minecraft:tradeable")));
        
        // Add to non_treasure tag to make it available in the enchanting table
        tags.add(TagKey.create(RegistryKey.ENCHANTMENT, Key.key("minecraft:non_treasure")));
        
        return tags;
    }
    
    private static Set<TagEntry<ItemType>> getDefaultSupportedItems() {
        Set<TagEntry<ItemType>> supportedItems = new HashSet<>();
        
        // Add standard enchantable items
        supportedItems.add(TagEntry.tagEntry(TagKey.create(RegistryKey.ITEM, Key.key("minecraft:enchantable/armor"))));
        supportedItems.add(TagEntry.tagEntry(TagKey.create(RegistryKey.ITEM, Key.key("minecraft:enchantable/weapon"))));
        supportedItems.add(TagEntry.tagEntry(TagKey.create(RegistryKey.ITEM, Key.key("minecraft:enchantable/mining"))));
        supportedItems.add(TagEntry.tagEntry(TagKey.create(RegistryKey.ITEM, Key.key("minecraft:enchantable/crossbow"))));
        supportedItems.add(TagEntry.tagEntry(TagKey.create(RegistryKey.ITEM, Key.key("minecraft:enchantable/bow"))));
        supportedItems.add(TagEntry.tagEntry(TagKey.create(RegistryKey.ITEM, Key.key("minecraft:enchantable/trident"))));
        
        return supportedItems;
    }
}

