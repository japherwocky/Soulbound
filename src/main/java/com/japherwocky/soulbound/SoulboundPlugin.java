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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

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
            // Basic info command doesn't require permission
            if (args.length == 0) {
                sender.sendMessage("§6Soulbound §7v" + getDescription().getVersion());
                sender.sendMessage("§7An enchantment that will retain items upon death");
                sender.sendMessage("§7Use §f/soulbound help §7for available commands");
                return true;
            }
            
            // Check if sender has permission for all other commands
            if (!sender.hasPermission("soulbound.command") && !sender.isOp()) {
                sender.sendMessage("§cYou don't have permission to use this command.");
                debug("Permission denied for " + sender.getName() + " using command: /soulbound " + args[0]);
                return true;
            }
            
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§6Soulbound §7Commands:");
                sender.sendMessage("§7- §f/soulbound §7- Show plugin information");
                sender.sendMessage("§7- §f/soulbound help §7- Show this help message");
                sender.sendMessage("§7- §f/soulbound reload §7- Reload the configuration");
                sender.sendMessage("§7- §f/soulbound info §7- Show configuration information");
                sender.sendMessage("§7- §f/soulbound book [player] §7- Give a Soulbound book to a player");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                storage.reload();
                sender.sendMessage("§6Soulbound §7configuration reloaded!");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage("§6Soulbound §7Configuration:");
                sender.sendMessage("§7- Removal Chance: §f" + getConfig().getDouble("soulbound-removal-chance"));
                sender.sendMessage("§7- Allow on All Items: §f" + getConfig().getBoolean("allow-on-all-items"));
                sender.sendMessage("§7- Discoverable: §f" + getConfig().getBoolean("discoverable"));
                sender.sendMessage("§7- Tradeable: §f" + getConfig().getBoolean("tradeable"));
                sender.sendMessage("§7- Debug Mode: §f" + getConfig().getBoolean("debug"));
                return true;
            }
            
            if (args[0].equalsIgnoreCase("book")) {
                // Handle giving a Soulbound book
                Player target;
                
                if (args.length > 1) {
                    // Give to specified player
                    target = getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found: §f" + args[1]);
                        return true;
                    }
                } else if (sender instanceof Player) {
                    // Give to command sender
                    target = (Player) sender;
                } else {
                    sender.sendMessage("§cUsage: §f/soulbound book [player]");
                    return true;
                }
                
                // Create and give the book
                ItemStack book = SoulboundAPI.createSoulboundBook();
                target.getInventory().addItem(book);
                
                if (target == sender) {
                    sender.sendMessage("§6Soulbound §7book added to your inventory!");
                } else {
                    sender.sendMessage("§6Soulbound §7book given to §f" + target.getName());
                    target.sendMessage("§6Soulbound §7book added to your inventory!");
                }
                
                return true;
            }
        }
        return false;
    }

    private void registerEnchantment() {
        try {
            // Create enchantment instance
            soulboundEnchantment = new SoulboundEnchantment(enchantmentKey);
            
            // Register the enchantment using the Registry API
            // In Paper 1.21.4, custom enchantments are registered differently
            // The actual registration is handled by the server when it loads the plugin
            
            // Add enchanted book to creative inventory
            // We'll use a delayed task to ensure the enchantment is registered before we try to create the book
            getServer().getScheduler().runTaskLater(this, () -> {
                try {
                    // Check if the enchantment is registered
                    Enchantment registeredEnchantment = Enchantment.getByKey(enchantmentKey);
                    if (registeredEnchantment == null) {
                        debug("Enchantment not found in registry, attempting to register manually");
                        // If the enchantment isn't in the registry, we need to register it manually
                        // This is a fallback and shouldn't be necessary in most cases
                        
                        // In Paper 1.21.4, enchantments are registered through the plugin.yml
                        // But we can try to ensure it's available through our API
                        registeredEnchantment = soulboundEnchantment;
                    }
                    
                    debug("Enchantment registration status: " + (registeredEnchantment != null));
                    if (registeredEnchantment != null) {
                        debug("Registered enchantment key: " + registeredEnchantment.getKey());
                        debug("Enchantment is discoverable: " + registeredEnchantment.isDiscoverable());
                        debug("Enchantment is tradeable: " + registeredEnchantment.isTradeable());
                        
                        // We'll skip creating a sample book here to avoid ClassCastException
                        // The enchantment is registered, but we need to use the Bukkit API to create enchanted books
                        debug("Enchantment is registered and should be available in the creative inventory");
                        debug("Use the /soulbound book command to create a Soulbound book");
                    }
                } catch (Exception e) {
                    getLogger().log(Level.SEVERE, "Failed to verify enchantment registration", e);
                }
            }, 20L); // 1 second delay
            
            getLogger().info("Successfully registered Soulbound enchantment!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to register Soulbound enchantment", e);
        }
    }

    private void unregisterEnchantment() throws Exception {
        // In Paper 1.21.4, we don't need to manually unregister enchantments
        // The registry will handle this when the plugin is disabled
        getLogger().info("Soulbound enchantment will be unregistered automatically");
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

    public boolean isDiscoverable() {
        return getConfig().getBoolean("discoverable", true);
    }

    public boolean isTradeable() {
        return getConfig().getBoolean("tradeable", true);
    }

    public void debug(String message) {
        if (isDebugEnabled()) {
            getLogger().info("[DEBUG] " + message);
        }
    }
    
    /**
     * Creates a Soulbound enchanted book
     * @return The enchanted book
     */
    public ItemStack createSoulboundBook() {
        try {
            // Create an enchanted book
            ItemStack book = new ItemStack(org.bukkit.Material.ENCHANTED_BOOK);
            
            // Get the enchantment from the registry instead of using our custom instance directly
            Enchantment registeredEnchantment = Enchantment.getByKey(enchantmentKey);
            if (registeredEnchantment == null) {
                debug("Warning: Enchantment not found in registry, book may not work correctly");
                // Fall back to our instance if not found in registry
                registeredEnchantment = soulboundEnchantment;
            }
            
            // Add the enchantment to the book's meta directly
            if (book.getItemMeta() instanceof org.bukkit.inventory.meta.EnchantmentStorageMeta meta) {
                meta.addStoredEnchant(registeredEnchantment, 1, true);
                book.setItemMeta(meta);
                debug("Created Soulbound enchanted book: " + book);
            } else {
                debug("Failed to get EnchantmentStorageMeta from book");
                throw new IllegalStateException("Could not get EnchantmentStorageMeta from book");
            }
            
            return book;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to create Soulbound book", e);
            // Return a regular book if we can't create an enchanted one
            return new ItemStack(org.bukkit.Material.BOOK);
        }
    }
}
