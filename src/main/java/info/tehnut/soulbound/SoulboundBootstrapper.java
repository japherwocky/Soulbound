package info.tehnut.soulbound;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;

public class SoulboundBootstrapper implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        // Bootstrap logic - runs before the server starts
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        // Create and return the plugin instance
        return new SoulboundPlugin();
    }
}

