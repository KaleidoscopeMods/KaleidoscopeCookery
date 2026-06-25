package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

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

        // 模拟模式：只判断能否接受，绝不改变石磨状态（否则会导致物品复制/丢失）
        if (simulate) {
            SimpleContainer container = new SimpleContainer(stack);
            boolean canAccept = this.millstone.isOutputEmpty()
                                && this.millstone.getInput().isEmpty()
                                && this.millstone.matchRecipe(container, level).isPresent();
            return canAccept ? ItemStack.EMPTY : stack;
        }

        // onPutItem 内部用 split 最多取走 MAX_INPUT_COUNT 个，
        // 返回 stack 中剩余的部分，避免多余物品被溜槽当作"已接收"而销毁
        if (this.millstone.onPutItem(level, stack)) {
            return stack.isEmpty() ? ItemStack.EMPTY : stack;
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
