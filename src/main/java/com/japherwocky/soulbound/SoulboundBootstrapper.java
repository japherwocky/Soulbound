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
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class SoulboundBootstrapper implements PluginBootstrap {
    
    private final Logger logger = LoggerFactory.getLogger("Soulbound");
    private final SoulboundEnchantment soulboundEnchantment = SoulboundEnchantment.create();
    
    @Override
    public void bootstrap(BootstrapContext context) {
        // Bootstrap logic - runs before the server starts
        logger.info("Bootstrapping Soulbound plugin");
        
        // STEP 1: Register enchantment tags FIRST
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ENCHANTMENT).newHandler((event) -> {
            soulboundEnchantment.getEnchantTagKeys().forEach(enchantmentTagKey -> {
                logger.info("Registering enchantment tag {} for Soulbound", enchantmentTagKey.key());
                event.registrar().addToTag(enchantmentTagKey, Set.of(soulboundEnchantment.getTagEntry()));
            });
        }));
        
        // STEP 2: Register the item tag for supported items
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ITEM).newHandler((event) -> {
            logger.info("Registering item tag {}", soulboundEnchantment.getTagForSupportedItems().key());
            event.registrar().addToTag(
                    ItemTypeTagKeys.create(soulboundEnchantment.getTagForSupportedItems().key()),
                    soulboundEnchantment.getSupportedItems()
            );
        }));
        
        // STEP 3: Register the enchantment LAST
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            logger.info("Registering Soulbound enchantment");
            
            event.registry().register(TypedKey.create(RegistryKey.ENCHANTMENT, soulboundEnchantment.getKey()), builder -> {
                builder.description(soulboundEnchantment.getDescription());
                builder.anvilCost(soulboundEnchantment.getAnvilCost());
                builder.maxLevel(soulboundEnchantment.getMaxLevel());
                builder.weight(soulboundEnchantment.getWeight());
                builder.minimumCost(soulboundEnchantment.getMinimumCost());
                builder.maximumCost(soulboundEnchantment.getMaximumCost());
                builder.activeSlots(soulboundEnchantment.getActiveSlots());
                builder.supportedItems(event.getOrCreateTag(soulboundEnchantment.getTagForSupportedItems()));
            });
        }));
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        // Create and return the plugin instance
        return new SoulboundPlugin();
    }
}

