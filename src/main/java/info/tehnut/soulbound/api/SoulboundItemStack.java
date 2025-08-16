package info.tehnut.soulbound.api;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an ItemStack with Soulbound enchantment that is stored between death and respawn.
 */
public class SoulboundItemStack {

    private final ItemStack itemStack;
    private final String inventoryType;
    private final int slot;

    /**
     * Creates a new SoulboundItemStack.
     *
     * @param itemStack The ItemStack to store
     * @param inventoryType The inventory type (main, armor, offhand)
     */
    public SoulboundItemStack(ItemStack itemStack, String inventoryType) {
        this(itemStack, inventoryType, -1);
    }

    /**
     * Creates a new SoulboundItemStack.
     *
     * @param itemStack The ItemStack to store
     * @param inventoryType The inventory type (main, armor, offhand)
     * @param slot The slot index for armor items (0-3)
     */
    public SoulboundItemStack(ItemStack itemStack, String inventoryType, int slot) {
        this.itemStack = itemStack;
        this.inventoryType = inventoryType;
        this.slot = slot;
    }

    /**
     * Gets the stored ItemStack.
     *
     * @return The ItemStack
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Gets the inventory type.
     *
     * @return The inventory type (main, armor, offhand)
     */
    public String getInventoryType() {
        return inventoryType;
    }

    /**
     * Gets the slot index for armor items.
     *
     * @return The slot index (0-3) or -1 if not applicable
     */
    public int getSlot() {
        return slot;
    }
}

