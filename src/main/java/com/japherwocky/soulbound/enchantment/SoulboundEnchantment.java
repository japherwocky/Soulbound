package com.japherwocky.soulbound.enchantment;

import com.japherwocky.soulbound.SoulboundPlugin;
import io.papermc.paper.enchantments.EnchantmentRarity;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class SoulboundEnchantment extends Enchantment {

    public static final Key KEY = Key.key("soulbound:soulbound");
    
    private final NamespacedKey key;
    private final int anvilCost;
    private final int weight;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
    
    public SoulboundEnchantment(NamespacedKey key) {
        this.key = key;
        this.anvilCost = 30;
        this.weight = 1; // Rare enchantment
        this.minimumCost = EnchantmentRegistryEntry.EnchantmentCost.of(20, 0);
        this.maximumCost = EnchantmentRegistryEntry.EnchantmentCost.of(50, 0);
        
        // Add default supported item tags
        List<String> defaultTags = List.of(
            "#minecraft:enchantable/armor",
            "#minecraft:enchantable/weapon",
            "#minecraft:enchantable/mining",
            "#minecraft:enchantable/fishing_rod",
            "#minecraft:enchantable/trident",
            "#minecraft:enchantable/wearable"
        );
        
        for (String itemTag : defaultTags) {
            if (itemTag.startsWith("#")) {
                String tagName = itemTag.substring(1);
                try {
                    Key tagKey = Key.key(tagName);
                    TagKey<ItemType> itemTypeTagKey = ItemTypeTagKeys.create(tagKey);
                    TagEntry<ItemType> tagEntry = TagEntry.tagEntry(itemTypeTagKey);
                    supportedItemTags.add(tagEntry);
                } catch (IllegalArgumentException e) {
                    SoulboundPlugin.getInstance().getLogger().warning("Invalid item tag: " + itemTag);
                }
            }
        }
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        if (SoulboundPlugin.getInstance().allowOnAllItems()) {
            return true;
        }
        
        // If not allowing on all items, only allow on items that can normally be enchanted
        return item.getType().isItem() && item.getType().getMaxDurability() > 0;
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.text("Soulbound").color(NamedTextColor.GRAY);
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public @NotNull Component description() {
        return Component.text("Keeps items in your inventory when you die").color(NamedTextColor.GRAY);
    }

    @Override
    public @NotNull RegistryKeySet<ItemType> getSupportedItems() {
        // Convert our tag entries to a RegistryKeySet
        return RegistrySet.keySet(RegistryKey.ITEM);
    }

    @Override
    public @Nullable RegistryKeySet<ItemType> getPrimaryItems() {
        return null; // Use supported items
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int getAnvilCost() {
        return anvilCost;
    }

    @Override
    public @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups() {
        return Set.of(EquipmentSlotGroup.ANY);
    }

    @Override
    public @NotNull RegistryKeySet<Enchantment> getExclusiveWith() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT);
    }
    
    public @NotNull Component translationName() {
        return Component.translatable("enchantment.soulbound.soulbound");
    }
    
    public EnchantmentRegistryEntry.@NotNull EnchantmentCost getMinimumCost() {
        return minimumCost;
    }
    
    public EnchantmentRegistryEntry.@NotNull EnchantmentCost getMaximumCost() {
        return maximumCost;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public @NotNull String translationKey() {
        return "enchantment.soulbound.soulbound";
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public float getDamageIncrease(int level, EntityType entityType) {
        return 0.0f; // Soulbound doesn't increase damage
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public float getDamageIncrease(int level, EntityCategory entityCategory) {
        return 0.0f; // Soulbound doesn't increase damage
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public @NotNull EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.BREAKABLE;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public @NotNull String getName() {
        return "Soulbound";
    }
    
    @Override
    public @NotNull String getTranslationKey() {
        return "enchantment.soulbound.soulbound";
    }
    
    public static TagKey<ItemType> getTagForSupportedItems() {
        return TagKey.create(RegistryKey.ITEM, Key.key("soulbound:soulbound_enchantable"));
    }
}
