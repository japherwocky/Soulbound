package com.japherwocky.soulbound.enchantment;

import com.japherwocky.soulbound.SoulboundPlugin;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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
        return true;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
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
        return RegistryKeySet.allOf(ItemType.class);
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
        return RegistryKeySet.empty(Enchantment.class);
    }

    @Override
    public @NotNull String translationKey() {
        return "enchantment.soulbound.soulbound";
    }
}
