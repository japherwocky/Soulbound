package com.japherwocky.soulbound.enchantment;

import com.japherwocky.soulbound.SoulboundPlugin;
import io.papermc.paper.enchantments.EnchantmentRarity;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
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

import java.util.Set;

public class SoulboundEnchantment extends Enchantment {

    private final NamespacedKey key;

    public SoulboundEnchantment(NamespacedKey key) {
        this.key = key;
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
        // Setting to false to ensure it appears in creative inventory
        return false;
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
        return SoulboundPlugin.getInstance().isTradeable();
    }

    @Override
    public boolean isDiscoverable() {
        return SoulboundPlugin.getInstance().isDiscoverable();
    }

    @Override
    public @NotNull Component description() {
        return Component.text("Keeps items in your inventory when you die").color(NamedTextColor.GRAY);
    }

    @Override
    public @NotNull RegistryKeySet<ItemType> getSupportedItems() {
        // Use the ITEM registry key
        // Create a set of all item types
        return RegistrySet.allOf(RegistryKey.ITEM);
    }

    @Override
    public @Nullable RegistryKeySet<ItemType> getPrimaryItems() {
        return null; // Use supported items
    }

    @Override
    public int getWeight() {
        return 1; // Rare enchantment
    }

    @Override
    public int getAnvilCost() {
        return 30;
    }

    @Override
    public @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups() {
        return Set.of(EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.HAND);
    }

    @Override
    public @NotNull RegistryKeySet<Enchantment> getExclusiveWith() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT);
    }
    
    public @NotNull Component translationName() {
        return Component.translatable("enchantment.soulbound.soulbound");
    }
    
    @Override
    public int getMinModifiedCost(int level) {
        return 20;
    }
    
    @Override
    public int getMaxModifiedCost(int level) {
        return 50;
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
}
