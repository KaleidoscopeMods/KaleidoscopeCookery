package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.OilPotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class OilPotHandler extends ItemStackHandler {
    private final OilPotBlockEntity oilPot;

    public OilPotHandler(OilPotBlockEntity oilPot) {
        super(1);
        this.oilPot = oilPot;
        int count = oilPot.getOilCount();
        if (count > 0) {
            this.setStackInSlot(0, new ItemStack(ModItems.OIL.get(), count));
        }
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.is(ModItems.OIL.get());
    }

    @Override
    public int getSlotLimit(int slot) {
        return OilPotBlockEntity.MAX_OIL_COUNT;
    }

    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return OilPotBlockEntity.MAX_OIL_COUNT;
    }

    @Override
    protected void onContentsChanged(int slot) {
        ItemStack stackInSlot = getStackInSlot(slot);
        oilPot.setOilCount(stackInSlot.getCount());
    }
}
