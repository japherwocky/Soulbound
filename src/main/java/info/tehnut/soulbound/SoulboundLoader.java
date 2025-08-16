package info.tehnut.soulbound;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;

public class SoulboundLoader implements PluginLoader {
    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        // Add any external dependencies if needed
        // For now, we don't need any external dependencies
    }
}

