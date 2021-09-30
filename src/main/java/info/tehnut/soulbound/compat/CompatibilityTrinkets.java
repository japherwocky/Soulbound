package info.tehnut.soulbound.compat;

import dev.emi.trinkets.api.TrinketsApi;
import info.tehnut.soulbound.api.SlottedItem;
import info.tehnut.soulbound.api.SoulboundContainer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CompatibilityTrinkets implements SoulboundContainer {

    static {
        SoulboundContainer.CONTAINERS.put(new Identifier("trinkets", "trinkets"), new CompatibilityTrinkets());
    }

    @Override
    public List<ItemStack> getContainerStacks(PlayerEntity player) {
        
        // Inventory inventory = TrinketsApi.getTrinketsInventory(player);
        List<ItemStack> stacks = new ArrayList<>();
        // for (int i = 0; i < inventory.size(); i++)
        //    stacks.add(inventory.getStack(i));
        stacks.add( new ItemStack(Blocks.DIAMOND_ORE, 13) );
        return stacks;

    }

    @Override
    public void replaceItem(PlayerEntity player, SlottedItem item) {
        // TrinketsApi.getTrinketsInventory(player).setStack(item.getSlot(), item.getStack());
    }

    @Override
    public void removeStoredItem(PlayerEntity player, int slot) {
        // TrinketsApi.getTrinketsInventory(player).setStack(slot, ItemStack.EMPTY);
    }
}
