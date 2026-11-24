package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Collections;

public class MillstoneInputHandler implements IItemHandler {
    private final MillstoneBlockEntity millstone;

    public MillstoneInputHandler(MillstoneBlockEntity millstone) {
        this.millstone = millstone;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.millstone.getInput();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        Level level = this.millstone.getLevel();
        if (level == null) {
            return stack;
        }

        if (simulate) {
            SimpleInput container = new SimpleInput(Collections.singletonList(stack));
            if (this.millstone.isOutputEmpty()
                && this.millstone.getInput().isEmpty()
                && this.millstone.matchRecipe(container, level).isPresent()
            ) {
                int count = Math.min(stack.getCount(), MillstoneBlockEntity.MAX_INPUT_COUNT);
                return stack.copyWithCount(stack.getCount() - count);
            }
            return stack;
        }

        // 不能直接修改传入的 stack，需要复制后，返回修改后的 stack
        ItemStack copy = stack.copy();
        if (this.millstone.onPutItem(level, copy)) {
            return copy.isEmpty() ? ItemStack.EMPTY : copy;
        }
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return MillstoneBlockEntity.MAX_INPUT_COUNT;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}
