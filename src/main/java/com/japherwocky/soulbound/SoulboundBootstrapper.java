package com.japherwocky.soulbound;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.registry.RegistryEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.plugin.java.JavaPlugin;

public class SoulboundBootstrapper implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        // Register our custom enchantment using the registry API
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.compose().newHandler(event -> {
            // Create a key for our enchantment
            TypedKey<org.bukkit.enchantments.Enchantment> enchantmentKey = 
                TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("soulbound", "soulbound"));
            
            // Register the enchantment
            event.registry().register(
                enchantmentKey,
                builder -> builder
                    .description(Component.text("Keeps items in your inventory when you die"))
                    .supportedItems(RegistryKey.ITEM.allValues()) // Support all items
                    .anvilCost(30)
                    .maxLevel(1)
                    .weight(1) // Rare enchantment
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(20, 1))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(50, 1))
                    .activeSlots(EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.HAND)
                    .discoverable(true)
                    .tradeable(true)
                    .treasure(false) // Set to false to ensure it appears in creative inventory
            );
            
            System.out.println("[Soulbound] Registered Soulbound enchantment with key: " + enchantmentKey);
        }));
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        // Create and return the plugin instance
        return new SoulboundPlugin();
    }
}
