package com.japherwocky.soulbound;

import com.japherwocky.soulbound.enchantment.SoulboundEnchantment;
import com.japherwocky.soulbound.util.EnchantmentTags;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
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
            // Get the tag key from our utility class to avoid loading the Enchantment class
            TagKey<ItemType> supportedItemsTag = EnchantmentTags.getSoulboundSupportedItemsTag();
            logger.info("Registering item tag {}", supportedItemsTag.key());
            
            // Register the tag with standard enchantable items
            event.registrar().addToTag(
                    supportedItemsTag,
                    Set.of(
                        TagEntry.tagEntry(ItemTypeTagKeys.ENCHANTABLE_ARMOR),
                        TagEntry.tagEntry(ItemTypeTagKeys.ENCHANTABLE_WEAPON),
                        TagEntry.tagEntry(ItemTypeTagKeys.ENCHANTABLE_MINING),
                        TagEntry.tagEntry(ItemTypeTagKeys.ENCHANTABLE_CROSSBOW),
                        TagEntry.tagEntry(ItemTypeTagKeys.ENCHANTABLE_BOW),
                        TagEntry.tagEntry(ItemTypeTagKeys.ENCHANTABLE_TRIDENT)
                    )
            );
        }));
        
        // Register the enchantment
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            logger.info("Registering Soulbound enchantment");
            
            // Register the enchantment using the builder pattern
            // We don't need to create a SoulboundEnchantment instance here
            event.registry().register(TypedKey.create(RegistryKey.ENCHANTMENT, EnchantmentTags.SOULBOUND_KEY), builder -> {
                builder.description(Component.text("Keeps items in your inventory when you die").color(NamedTextColor.GRAY));
                builder.anvilCost(30);
                builder.maxLevel(1);
                builder.weight(1); // Rare enchantment
                builder.minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(20, 0));
                builder.maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(50, 0));
                builder.activeSlots(Set.of(EquipmentSlotGroup.ANY));
                builder.supportedItems(event.getOrCreateTag(EnchantmentTags.getSoulboundSupportedItemsTag()));
            });
        }));
        
        // Register enchantment tags
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ENCHANTMENT).newHandler((event) -> {
            // Get the enchantment tags from our utility class
            Set<TagKey<Enchantment>> enchantmentTags = EnchantmentTags.getSoulboundEnchantmentTags();
            
            // Get the tag entry for our enchantment
            TagEntry<Enchantment> soulboundTagEntry = EnchantmentTags.getSoulboundTagEntry();
            
            // Register our enchantment with each tag
            for (TagKey<Enchantment> tagKey : enchantmentTags) {
                logger.info("Registering enchantment tag {} for Soulbound", tagKey.key());
                event.registrar().addToTag(tagKey, Set.of(soulboundTagEntry));
            }
        }));
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        // Create and return the plugin instance
        return new SoulboundPlugin();
    }
}
