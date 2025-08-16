package info.tehnut.soulbound.enchantment;

import info.tehnut.soulbound.SoulboundPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class SoulboundEnchantment extends Enchantment {

    public SoulboundEnchantment(NamespacedKey key) {
        super(key);
    }

    @Override
    public String getName() {
        return "soulbound";
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
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        if (SoulboundPlugin.getInstance().allowOnAllItems()) {
            return true;
        }
        
        // If not allowing on all items, only allow on items that can normally be enchanted
        return item.getType().isItem() && item.getType().getMaxDurability() > 0;
    }

    @Override
    public Component displayName(int level) {
        return Component.text("Soulbound").color(NamedTextColor.GRAY);
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public float getDamageIncrease(int level, EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public Set<EquipmentSlot> getActiveSlots() {
        return Set.of(EquipmentSlot.values());
    }

    @Override
    public String translationKey() {
        return "enchantment.soulbound.soulbound";
    }
}

