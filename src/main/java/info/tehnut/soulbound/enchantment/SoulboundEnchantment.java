package info.tehnut.soulbound.enchantment;

import info.tehnut.soulbound.SoulboundPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class SoulboundEnchantment extends Enchantment {

    public SoulboundEnchantment(NamespacedKey key) {
        super(key);
    }

    @Override
    public String getName() {
        return "Soulbound";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return other.equals(Enchantment.VANISHING_CURSE);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        if (SoulboundPlugin.getInstance().getConfig().getBoolean("allow-on-all-items", true)) {
            return true;
        }
        return getItemTarget().includes(item);
    }
}

