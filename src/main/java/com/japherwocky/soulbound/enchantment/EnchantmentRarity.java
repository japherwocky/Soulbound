package com.japherwocky.soulbound.enchantment;

/**
 * A custom implementation of EnchantmentRarity to support the Soulbound enchantment.
 * This is needed because the Paper API's EnchantmentRarity is deprecated and will be removed.
 */
@Deprecated(forRemoval = true)
public enum EnchantmentRarity {
    COMMON(10),
    UNCOMMON(5),
    RARE(2),
    VERY_RARE(1);

    private final int weight;

    EnchantmentRarity(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}

