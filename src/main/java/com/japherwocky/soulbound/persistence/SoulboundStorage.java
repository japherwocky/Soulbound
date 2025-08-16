package com.japherwocky.soulbound.persistence;

import com.japherwocky.soulbound.SoulboundPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class SoulboundStorage {

    private final SoulboundPlugin plugin;
    private final File storageFile;
    private FileConfiguration storage;
    private final Map<UUID, List<SoulboundItem>> playerItems = new HashMap<>();

    public SoulboundStorage(SoulboundPlugin plugin) {
        this.plugin = plugin;
        this.storageFile = new File(plugin.getDataFolder(), "storage.yml");
        
        // Create storage file if it doesn't exist
        if (!storageFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                storageFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create storage file", e);
            }
        }
        
        // Load storage
        reload();
    }

    public void reload() {
        storage = YamlConfiguration.loadConfiguration(storageFile);
        playerItems.clear();
        
        // Load stored items
        ConfigurationSection playersSection = storage.getConfigurationSection("players");
        if (playersSection != null) {
            for (String playerIdStr : playersSection.getKeys(false)) {
                try {
                    UUID playerId = UUID.fromString(playerIdStr);
                    List<SoulboundItem> items = new ArrayList<>();
                    
                    @SuppressWarnings("unchecked")
                    List<SoulboundItem> storedItems = (List<SoulboundItem>) playersSection.getList(playerIdStr);
                    if (storedItems != null) {
                        items.addAll(storedItems);
                    }
                    
                    playerItems.put(playerId, items);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in storage: " + playerIdStr);
                }
            }
        }
    }

    public void save() {
        // Clear existing data
        storage.set("players", null);
        
        // Save player items
        for (Map.Entry<UUID, List<SoulboundItem>> entry : playerItems.entrySet()) {
            storage.set("players." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save to file
        try {
            storage.save(storageFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save storage file", e);
        }
    }

    public void storeSoulboundItems(UUID playerId, List<SoulboundItem> items) {
        playerItems.put(playerId, items);
        save();
    }

    public List<SoulboundItem> getSoulboundItems(UUID playerId) {
        return playerItems.getOrDefault(playerId, Collections.emptyList());
    }

    public void clearSoulboundItems(UUID playerId) {
        playerItems.remove(playerId);
        save();
    }

    public void saveAll() {
        save();
    }

    /**
     * Represents a soulbound item with its original inventory slot.
     */
    public static class SoulboundItem implements ConfigurationSerializable {
        private final ItemStack itemStack;
        private final int slot;

        public SoulboundItem(ItemStack itemStack, int slot) {
            this.itemStack = itemStack;
            this.slot = slot;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public int getSlot() {
            return slot;
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new HashMap<>();
            map.put("item", itemStack);
            map.put("slot", slot);
            return map;
        }

        public static SoulboundItem deserialize(Map<String, Object> map) {
            ItemStack item = (ItemStack) map.get("item");
            int slot = (int) map.get("slot");
            return new SoulboundItem(item, slot);
        }
    }
}

