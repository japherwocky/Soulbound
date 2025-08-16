package info.tehnut.soulbound.persistence;

import info.tehnut.soulbound.SoulboundPlugin;
import info.tehnut.soulbound.api.SoulboundItemStack;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class SoulboundStorage {

    private final SoulboundPlugin plugin;
    private final File storageFile;
    private final Map<UUID, List<SoulboundItemStack>> playerItems = new HashMap<>();
    private YamlConfiguration storage;

    public SoulboundStorage(SoulboundPlugin plugin) {
        this.plugin = plugin;
        this.storageFile = new File(plugin.getDataFolder(), "storage.yml");
        
        // Create the data folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        // Load the storage file
        loadStorage();
    }

    private void loadStorage() {
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create storage file", e);
            }
        }
        
        storage = YamlConfiguration.loadConfiguration(storageFile);
        
        // Load stored items
        if (storage.contains("players")) {
            for (String uuidString : storage.getConfigurationSection("players").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    List<SoulboundItemStack> items = new ArrayList<>();
                    
                    for (String key : storage.getConfigurationSection("players." + uuidString).getKeys(false)) {
                        String inventoryType = storage.getString("players." + uuidString + "." + key + ".type", "main");
                        int slot = storage.getInt("players." + uuidString + "." + key + ".slot", -1);
                        ItemStack itemStack = storage.getItemStack("players." + uuidString + "." + key + ".item");
                        
                        if (itemStack != null) {
                            items.add(new SoulboundItemStack(itemStack, inventoryType, slot));
                        }
                    }
                    
                    if (!items.isEmpty()) {
                        playerItems.put(uuid, items);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in storage file: " + uuidString);
                }
            }
        }
    }

    public void saveStorage() {
        // Clear the current storage
        storage.set("players", null);
        
        // Save all player items
        for (Map.Entry<UUID, List<SoulboundItemStack>> entry : playerItems.entrySet()) {
            UUID uuid = entry.getKey();
            List<SoulboundItemStack> items = entry.getValue();
            
            for (int i = 0; i < items.size(); i++) {
                SoulboundItemStack item = items.get(i);
                String path = "players." + uuid.toString() + "." + i;
                
                storage.set(path + ".type", item.getInventoryType());
                storage.set(path + ".slot", item.getSlot());
                storage.set(path + ".item", item.getItemStack());
            }
        }
        
        // Save to file
        try {
            storage.save(storageFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save storage file", e);
        }
    }

    public void storeSoulboundItems(UUID playerUuid, List<SoulboundItemStack> items) {
        playerItems.put(playerUuid, items);
        saveStorage();
    }

    public List<SoulboundItemStack> getSoulboundItems(UUID playerUuid) {
        return playerItems.get(playerUuid);
    }

    public void clearSoulboundItems(UUID playerUuid) {
        playerItems.remove(playerUuid);
        saveStorage();
    }

    public void saveAll() {
        saveStorage();
    }
}

