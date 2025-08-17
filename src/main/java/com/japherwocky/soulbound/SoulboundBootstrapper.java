package com.japherwocky.soulbound;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistrySet;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.plugin.java.JavaPlugin;

public class SoulboundBootstrapper implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        // Register our custom enchantment using the registry API
        // The actual registration will happen when the server loads the enchantment registry
        // We'll use the SoulboundPlugin class to handle the registration
        System.out.println("[Soulbound] Bootstrap initialized, enchantment will be registered when server loads");
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        // Create and return the plugin instance
        return new SoulboundPlugin();
    }
}
