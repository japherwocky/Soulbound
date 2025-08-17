package com.japherwocky.soulbound;

import com.japherwocky.soulbound.enchantment.SoulboundEnchantment;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class SoulboundBootstrapper implements PluginBootstrap {
    
    private final Logger logger = LoggerFactory.getLogger("Soulbound");
    
    @Override
    public void bootstrap(BootstrapContext context) {
        // Bootstrap logic - runs before the server starts
        logger.info("Bootstrapping Soulbound plugin");
        
        // Register the item tag for supported items
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ITEM).newHandler((event) -> {
            logger.info("Registering item tag {}", SoulboundEnchantment.getTagForSupportedItems().key());
            
            // Create a new SoulboundEnchantment instance to get the supported items
            NamespacedKey key = new NamespacedKey("soulbound", "soulbound");
            SoulboundEnchantment enchantment = new SoulboundEnchantment(key);
            
            // Register the tag
            event.registrar().addToTag(
                    ItemTypeTagKeys.create(SoulboundEnchantment.getTagForSupportedItems().key()),
                    enchantment.getSupportedItems()
            );
        }));
        
        // Register the enchantment
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            logger.info("Registering Soulbound enchantment");
            
            // Create a new SoulboundEnchantment instance to get the configuration
            NamespacedKey key = new NamespacedKey("soulbound", "soulbound");
            SoulboundEnchantment enchantment = new SoulboundEnchantment(key);
            
            // Register the enchantment
            event.registry().register(TypedKey.create(RegistryKey.ENCHANTMENT, SoulboundEnchantment.KEY), builder -> {
                builder.description(enchantment.description());
                builder.anvilCost(enchantment.getAnvilCost());
                builder.maxLevel(enchantment.getMaxLevel());
                builder.weight(enchantment.getWeight());
                builder.minimumCost(enchantment.getMinimumCost());
                builder.maximumCost(enchantment.getMaximumCost());
                builder.activeSlots(enchantment.getActiveSlotGroups());
                builder.supportedItems(event.getOrCreateTag(SoulboundEnchantment.getTagForSupportedItems()));
            });
        }));
        
        // Register enchantment tags
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ENCHANTMENT).newHandler((event) -> {
            // Add the enchantment to the in_enchanting_table tag if desired
            // For now, we'll keep it as a treasure enchantment
        }));
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        // Create and return the plugin instance
        return new SoulboundPlugin();
    }
}
