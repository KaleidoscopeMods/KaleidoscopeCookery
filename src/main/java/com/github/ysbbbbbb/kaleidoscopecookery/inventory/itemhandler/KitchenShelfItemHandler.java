package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class KitchenShelfItemHandler extends ItemStackHandler {
    public static final int CAPACITY = 256;
    private final KitchenShelfBlockEntity blockEntity;

    public KitchenShelfItemHandler(KitchenShelfBlockEntity blockEntity) {
        super(1);
        this.blockEntity = blockEntity;
    }

    @Override
    public int getSlotLimit(int slot) {
        return CAPACITY;
    }

    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return CAPACITY;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        ItemStack stack = stacks.getFirst();

        CompoundTag root = new CompoundTag();
        Tag itemTag = stack.copyWithCount(1).saveOptional(provider);

        root.put("item", itemTag);
        root.putInt("count", stack.getCount());

        return root;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        int count = nbt.getInt("count");

        CompoundTag itemTag = nbt.getCompound("item");
        ItemStack stack = ItemStack.parseOptional(provider, itemTag);

        this.stacks.set(0, stack.copyWithCount(count));
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.blockEntity.refresh();
    }
}
