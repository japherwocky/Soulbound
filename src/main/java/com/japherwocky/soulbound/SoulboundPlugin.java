package com.japherwocky.soulbound;

import com.japherwocky.soulbound.api.SoulboundAPI;
import com.japherwocky.soulbound.enchantment.SoulboundEnchantment;
import com.japherwocky.soulbound.listeners.PlayerDeathListener;
import com.japherwocky.soulbound.listeners.PlayerRespawnListener;
import com.japherwocky.soulbound.persistence.SoulboundStorage;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class SoulboundPlugin extends JavaPlugin {

    private static SoulboundPlugin instance;
    private SoulboundEnchantment soulboundEnchantment;
    private SoulboundStorage storage;
    private NamespacedKey enchantmentKey;

    @Override
    public void onLoad() {
        instance = this;
        enchantmentKey = new NamespacedKey(this, "soulbound");
        
        // Register serialization classes
        ConfigurationSerialization.registerClass(SoulboundStorage.SoulboundItem.class);
    }

    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();
        
        // Initialize storage
        storage = new SoulboundStorage(this);
        
        // Register enchantment
        registerEnchantment();
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        
        // Initialize API
        SoulboundAPI.init(this);
        
        getLogger().info("Soulbound has been enabled!");
    }

    @Override
    public void onDisable() {
        // Save any pending data
        if (storage != null) {
            storage.saveAll();
        }
        
        // Unregister enchantment
        try {
            unregisterEnchantment();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to unregister Soulbound enchantment", e);
        }
        
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
                storage.reload();
                sender.sendMessage("§6Soulbound §7configuration reloaded!");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("info") && sender.hasPermission("soulbound.command")) {
                sender.sendMessage("§6Soulbound §7Configuration:");
                sender.sendMessage("§7- Removal Chance: §f" + getConfig().getDouble("soulbound-removal-chance"));
                sender.sendMessage("§7- Allow on All Items: §f" + getConfig().getBoolean("allow-on-all-items"));
                sender.sendMessage("§7- Debug Mode: §f" + getConfig().getBoolean("debug"));
                return true;
            }
        }
        return false;
    }

    private void registerEnchantment() {
        try {
            // Create enchantment instance
            soulboundEnchantment = new SoulboundEnchantment(enchantmentKey);
            
            // Use reflection to bypass the "acceptingNew" restriction
            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);
            
            // Register the enchantment
            Enchantment.registerEnchantment(soulboundEnchantment);
            
            getLogger().info("Successfully registered Soulbound enchantment!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to register Soulbound enchantment", e);
        }
    }

    private void unregisterEnchantment() throws Exception {
        // Use reflection to unregister the enchantment
        Field byKeyField = Enchantment.class.getDeclaredField("byKey");
        byKeyField.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byKeyField.get(null);
        byKey.remove(enchantmentKey);
        
        Field byNameField = Enchantment.class.getDeclaredField("byName");
        byNameField.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
        byName.remove(soulboundEnchantment.getName());
    }

    public SoulboundEnchantment getSoulboundEnchantment() {
        return soulboundEnchantment;
    }

    public SoulboundStorage getStorage() {
        return storage;
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

