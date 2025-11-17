package com.github.ysbbbbbb.kaleidoscopecookery.util.forge;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RangedWrapper implements IItemHandler {

    private final IItemHandler compose;
    private final int minSlot;
    private final int maxSlot;

    public RangedWrapper(IItemHandler compose, int minSlot, int maxSlotExclusive)
    {
        Preconditions.checkArgument(maxSlotExclusive > minSlot, "Max slot must be greater than min slot");
        this.compose = compose;
        this.minSlot = minSlot;
        this.maxSlot = maxSlotExclusive;
    }

    @Override
    public int getSlots()
    {
        return maxSlot - minSlot;
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot)
    {
        if (checkSlot(slot))
        {
            return compose.getStackInSlot(slot + minSlot);
        }

        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (checkSlot(slot))
        {
            return compose.insertItem(slot + minSlot, stack, simulate);
        }

        return stack;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (checkSlot(slot))
        {
            return compose.extractItem(slot + minSlot, amount, simulate);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack)
    {
        if (checkSlot(slot))
        {
            compose.setStackInSlot(slot + minSlot, stack);
        }
    }

    @Override
    public int getSlotLimit(int slot)
    {
        if (checkSlot(slot))
        {
            return compose.getSlotLimit(slot + minSlot);
        }

        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        if (checkSlot(slot))
        {
            return compose.isItemValid(slot + minSlot, stack);
        }

        return false;
    }

    private boolean checkSlot(int localSlot)
    {
        return localSlot + minSlot < maxSlot;
    }

}
