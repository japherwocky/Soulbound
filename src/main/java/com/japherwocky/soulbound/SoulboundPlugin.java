package com.japherwocky.soulbound;

import com.japherwocky.soulbound.api.SoulboundAPI;
import com.japherwocky.soulbound.enchantment.SoulboundEnchantment;
import com.japherwocky.soulbound.listeners.PlayerDeathListener;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@SuppressWarnings("UnstableApiUsage")
public class SoulboundPlugin extends JavaPlugin {

    private static SoulboundPlugin instance;
    private Enchantment soulboundEnchantment;
    private NamespacedKey enchantmentKey;

    @Override
    public void onLoad() {
        instance = this;
        enchantmentKey = new NamespacedKey(this, "soulbound");
    }

    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();
        
        // Get the enchantment from the registry
        Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        soulboundEnchantment = registry.get(SoulboundEnchantment.KEY);
        
        if (soulboundEnchantment == null) {
            getLogger().severe("Failed to get Soulbound enchantment from registry! The enchantment may not work correctly.");
        } else {
            getLogger().info("Successfully obtained Soulbound enchantment from registry!");
        }
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        
        // Initialize API
        SoulboundAPI.init(this);
        
        getLogger().info("Soulbound has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Soulbound has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("soulbound")) {
            if (args.length == 0) {
                sender.sendMessage("§6Soulbound §7v" + getDescription().getVersion());
                sender.sendMessage("§7An enchantment that will retain items upon death");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("soulbound.command")) {
                reloadConfig();
                sender.sendMessage("§6Soulbound §7configuration reloaded!");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("info") && sender.hasPermission("soulbound.command")) {
                sender.sendMessage("§6Soulbound §7Configuration:");
                sender.sendMessage("§7- Removal Chance: §f" + getConfig().getDouble("soulbound-removal-chance", 0.0));
                sender.sendMessage("§7- Allow on All Items: §f" + getConfig().getBoolean("allow-on-all-items", true));
                sender.sendMessage("§7- Debug Mode: §f" + getConfig().getBoolean("debug", false));
                return true;
            }
        }
        return false;
    }

    public Enchantment getSoulboundEnchantment() {
        return soulboundEnchantment;
    }

    public static SoulboundPlugin getInstance() {
        return instance;
    }

    public double getRemovalChance() {
        return getConfig().getDouble("soulbound-removal-chance", 0.0);
    }

    public boolean allowOnAllItems() {
        return getConfig().getBoolean("allow-on-all-items", true);
    }

    public boolean isDebugEnabled() {
        return getConfig().getBoolean("debug", false);
    }

    public void debug(String message) {
        if (isDebugEnabled()) {
            getLogger().info("[DEBUG] " + message);
        }
    }
}
