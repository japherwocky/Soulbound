package info.tehnut.soulbound.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import info.tehnut.soulbound.api.SlottedItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SoulboundPersistentState extends PersistentState {

    private final Map<UUID, List<SlottedItem>> persistedItems = Maps.newHashMap();

    public SoulboundPersistentState () {};

    public static SoulboundPersistentState fromNbt(NbtCompound nbt) {
        SoulboundPersistentState ourState = new SoulboundPersistentState();

        ourState.readNbt(nbt);
        return ourState;

    }
    public void storePlayer(PlayerEntity player, List<SlottedItem> items) {
        this.persistedItems.put(player.getGameProfile().getId(), items);
        markDirty();
    }

    public List<SlottedItem> restorePlayer(PlayerEntity player) {
        List<SlottedItem> items = this.persistedItems.getOrDefault(player.getGameProfile().getId(), Collections.emptyList());
        this.persistedItems.remove(player.getGameProfile().getId());
        markDirty();
        return items;
    }

    public void readNbt(NbtCompound tag) {
        tag.getList("playerTags", 10).forEach(playerTag -> {
            NbtCompound playerCompound = (NbtCompound) playerTag;
            UUID uuid = playerCompound.getUuid("uuid");
            NbtList items = playerCompound.getList("items", 10);
            List<SlottedItem> slotted = Lists.newArrayList();
            items.forEach(itemTag -> {
                NbtCompound itemCompound = (NbtCompound) itemTag;
                Identifier id = new Identifier(itemCompound.getString("id"));
                ItemStack stack = ItemStack.fromNbt(itemCompound.getCompound("stack"));
                int slot = itemCompound.getInt("slot");
                slotted.add(new SlottedItem(id, stack, slot));
            });
            this.persistedItems.put(uuid, slotted);
        });
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        NbtList playerTags = new NbtList();
        persistedItems.forEach((uuid, items) -> {
            NbtCompound playerTag = new NbtCompound();
            playerTag.putUuid("uuid", uuid);
            NbtList itemsList = new NbtList();
            items.forEach(slotted -> {
                NbtCompound itemTag = new NbtCompound();
                itemTag.putString("id", slotted.getContainerId().toString());
                itemTag.put("stack", slotted.getStack().writeNbt(new NbtCompound()));
                itemTag.putInt("slot", slotted.getSlot());
                itemsList.add(itemTag);
            });
            playerTag.put("items", itemsList);

            playerTags.add(playerTag);
        });
        tag.put("playerTags", playerTags);
        return tag;
    }
}
