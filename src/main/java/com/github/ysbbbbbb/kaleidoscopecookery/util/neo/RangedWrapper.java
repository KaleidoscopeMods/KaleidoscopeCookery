package com.github.ysbbbbbb.kaleidoscopecookery.util.neo;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.ItemStack;

//From Neoforge
public class RangedWrapper implements IItemHandler {
    private final IItemHandler compose;
    private final int minSlot;
    private final int maxSlot;

    public RangedWrapper(IItemHandler compose, int minSlot, int maxSlotExclusive) {
        Preconditions.checkArgument(maxSlotExclusive > minSlot, "Max slot must be greater than min slot");
        this.compose = compose;
        this.minSlot = minSlot;
        this.maxSlot = maxSlotExclusive;
    }

    public int getSlots() {
        return this.maxSlot - this.minSlot;
    }

    public ItemStack getStackInSlot(int slot) {
        return this.checkSlot(slot) ? this.compose.getStackInSlot(slot + this.minSlot) : ItemStack.EMPTY;
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return this.checkSlot(slot) ? this.compose.insertItem(slot + this.minSlot, stack, simulate) : stack;
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.checkSlot(slot) ? this.compose.extractItem(slot + this.minSlot, amount, simulate) : ItemStack.EMPTY;
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        if (this.checkSlot(slot)) {
            this.compose.setStackInSlot(slot + this.minSlot, stack);
        }

    }

    public int getSlotLimit(int slot) {
        return this.checkSlot(slot) ? this.compose.getSlotLimit(slot + this.minSlot) : 0;
    }

    public boolean isItemValid(int slot, ItemStack stack) {
        return this.checkSlot(slot) && this.compose.isItemValid(slot + this.minSlot, stack);
    }

    private boolean checkSlot(int localSlot) {
        return localSlot + this.minSlot < this.maxSlot;
    }
}
