package info.tehnut.soulbound;

import info.tehnut.soulbound.enchantment.SoulboundEnchantment;
import info.tehnut.soulbound.listeners.PlayerDeathListener;
import info.tehnut.soulbound.listeners.PlayerRespawnListener;
import info.tehnut.soulbound.persistence.SoulboundStorage;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public class SoulboundPlugin extends JavaPlugin {

    private static SoulboundPlugin instance;
    private SoulboundEnchantment soulboundEnchantment;
    private SoulboundStorage storage;
    private NamespacedKey soulboundKey;

    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Create the enchantment key
        soulboundKey = new NamespacedKey(this, "soulbound");
        
        // Register the enchantment
        registerSoulboundEnchantment();
        
        // Initialize storage
        storage = new SoulboundStorage(this);
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        
        getLogger().info("Soulbound plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Save any pending data
        if (storage != null) {
            storage.saveAll();
        }
        
        // Unregister the enchantment if possible
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");
            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);
            byKey.remove(soulboundKey);
            
            Field nameField = Enchantment.class.getDeclaredField("byName");
            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);
            byName.remove(soulboundEnchantment.getName());
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Failed to unregister Soulbound enchantment", e);
        }
        
        getLogger().info("Soulbound plugin disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("soulbound")) {
            if (args.length == 0) {
                sender.sendMessage("§6Soulbound §7v" + getDescription().getVersion());
                sender.sendMessage("§7Usage: /soulbound [reload|info]");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                sender.sendMessage("§6Soulbound §7configuration reloaded!");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage("§6Soulbound §7v" + getDescription().getVersion());
                sender.sendMessage("§7Removal chance: §f" + getConfig().getDouble("soulbound-removal-chance"));
                sender.sendMessage("§7Allow on all items: §f" + getConfig().getBoolean("allow-on-all-items"));
                return true;
            }
            
            sender.sendMessage("§cUnknown command. Use /soulbound for help.");
            return true;
        }
        
        return false;
    }

    private void registerSoulboundEnchantment() {
        try {
            // Check if the enchantment is already registered
            if (Enchantment.getByKey(soulboundKey) != null) {
                getLogger().info("Soulbound enchantment already registered.");
                soulboundEnchantment = (SoulboundEnchantment) Enchantment.getByKey(soulboundKey);
                return;
            }
            
            // Create the enchantment
            soulboundEnchantment = new SoulboundEnchantment(soulboundKey);
            
            // Use reflection to bypass the "acceptingNew" restriction
            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);
            
            // Register the enchantment
            Enchantment.registerEnchantment(soulboundEnchantment);
            
            getLogger().info("Soulbound enchantment registered successfully!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to register Soulbound enchantment", e);
        }
    }

    public static SoulboundPlugin getInstance() {
        return instance;
    }

    public SoulboundEnchantment getSoulboundEnchantment() {
        return soulboundEnchantment;
    }

    public SoulboundStorage getStorage() {
        return storage;
    }

    public NamespacedKey getSoulboundKey() {
        return soulboundKey;
    }
}

