package com.japherwocky.soulbound;

import com.japherwocky.soulbound.api.SoulboundAPI;
import com.japherwocky.soulbound.enchantment.SoulboundEnchantment;
import com.japherwocky.soulbound.listeners.PlayerDeathListener;
import com.japherwocky.soulbound.listeners.PlayerRespawnListener;
import com.japherwocky.soulbound.persistence.SoulboundStorage;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Level;

@SuppressWarnings("UnstableApiUsage")
public class SoulboundPlugin extends JavaPlugin {

    private static SoulboundPlugin instance;
    private Enchantment soulboundEnchantment;
    private final Random random = new Random();
    private double removalChance = 0.0;
    private boolean debug = false;
    private SoulboundStorage storage;

    @Override
    public void onLoad() {
        instance = this;
        
        // Register SoulboundItem for serialization
        org.bukkit.configuration.serialization.ConfigurationSerialization.registerClass(SoulboundStorage.SoulboundItem.class);
    }

    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();
        
        // Load configuration
        reloadConfig();
        
        // Initialize storage
        storage = new SoulboundStorage(this);
        
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
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        
        // Initialize API
        SoulboundAPI.init(this);
        
        getLogger().info("Soulbound has been enabled!");
    }

    @Override
    public void onDisable() {
        // Save any pending storage changes
        if (storage != null) {
            storage.saveAll();
        }
        
        getLogger().info("Soulbound has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("soulbound")) {
            if (args.length == 0) {
                sender.sendMessage("§6Soulbound §7v" + getDescription().getVersion() + " by " + getDescription().getAuthors().get(0));
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
    
    public void reloadConfig() {
        super.reloadConfig();
        this.removalChance = getConfig().getDouble("soulbound-removal-chance", 0.0);
        this.debug = getConfig().getBoolean("debug", false);
    }
    
    public boolean shouldRemoveEnchantment() {
        return random.nextDouble() < removalChance;
    }
    
    public void debug(String message) {
        if (debug) {
            getLogger().info("[DEBUG] " + message);
        }
    }
    
    public SoulboundStorage getStorage() {
        return storage;
    }
}
