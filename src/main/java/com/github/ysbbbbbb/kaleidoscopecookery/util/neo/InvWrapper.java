package com.github.ysbbbbbb.kaleidoscopecookery.util.neo;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

//From Neoforge
public record InvWrapper(Container inv) implements IItemHandler {

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            InvWrapper that = (InvWrapper) o;
            return this.inv().equals(that.inv());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.inv().hashCode();
    }

    public int getSlots() {
        return this.inv().getContainerSize();
    }

    public ItemStack getStackInSlot(int slot) {
        return this.inv().getItem(slot);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stackInSlot = this.inv().getItem(slot);
            if (!stackInSlot.isEmpty()) {
                if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), this.getSlotLimit(slot))) {
                    return stack;
                } else if (!ItemStack.isSameItemSameComponents(stack, stackInSlot)) {
                    return stack;
                } else if (!this.inv().canPlaceItem(slot, stack)) {
                    return stack;
                } else {
                    int m = Math.min(stack.getMaxStackSize(), this.getSlotLimit(slot)) - stackInSlot.getCount();
                    if (stack.getCount() <= m) {
                        if (!simulate) {
                            ItemStack copy = stack.copy();
                            copy.grow(stackInSlot.getCount());
                            this.inv().setItem(slot, copy);
                            this.inv().setChanged();
                        }

                        return ItemStack.EMPTY;
                    } else {
                        stack = stack.copy();
                        if (!simulate) {
                            ItemStack copy = stack.split(m);
                            copy.grow(stackInSlot.getCount());
                            this.inv().setItem(slot, copy);
                            this.inv().setChanged();
                            return stack;
                        } else {
                            stack.shrink(m);
                            return stack;
                        }
                    }
                }
            } else if (!this.inv().canPlaceItem(slot, stack)) {
                return stack;
            } else {
                int m = Math.min(stack.getMaxStackSize(), this.getSlotLimit(slot));
                if (m < stack.getCount()) {
                    stack = stack.copy();
                    if (!simulate) {
                        this.inv().setItem(slot, stack.split(m));
                        this.inv().setChanged();
                        return stack;
                    } else {
                        stack.shrink(m);
                        return stack;
                    }
                } else {
                    if (!simulate) {
                        this.inv().setItem(slot, stack);
                        this.inv().setChanged();
                    }

                    return ItemStack.EMPTY;
                }
            }
        }
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stackInSlot = this.inv().getItem(slot);
            if (stackInSlot.isEmpty()) {
                return ItemStack.EMPTY;
            } else if (simulate) {
                if (stackInSlot.getCount() < amount) {
                    return stackInSlot.copy();
                } else {
                    ItemStack copy = stackInSlot.copy();
                    copy.setCount(amount);
                    return copy;
                }
            } else {
                int m = Math.min(stackInSlot.getCount(), amount);
                ItemStack decrStackSize = this.inv().removeItem(slot, m);
                this.inv().setChanged();
                return decrStackSize;
            }
        }
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        this.inv().setItem(slot, stack);
    }

    public int getSlotLimit(int slot) {
        return this.inv().getMaxStackSize();
    }

    public boolean isItemValid(int slot, ItemStack stack) {
        return this.inv().canPlaceItem(slot, stack);
    }
}
