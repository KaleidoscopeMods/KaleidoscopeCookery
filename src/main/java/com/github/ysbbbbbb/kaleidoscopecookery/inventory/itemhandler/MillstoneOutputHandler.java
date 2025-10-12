//package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;
//
//import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.items.IItemHandler;
//import org.jetbrains.annotations.NotNull;
//
//public class MillstoneOutputHandler implements IItemHandler {
//    private final MillstoneBlockEntity millstone;
//
//    public MillstoneOutputHandler(MillstoneBlockEntity millstone) {
//        this.millstone = millstone;
//    }
//
//    @Override
//    public int getSlots() {
//        return 1;
//    }
//
//    @Override
//    @NotNull
//    public ItemStack getStackInSlot(int slot) {
//        return millstone.getOutput();
//    }
//
//    @Override
//    @NotNull
//    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
//        return stack;
//    }
//
//    @Override
//    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
//        if (amount == 0) {
//            return ItemStack.EMPTY;
//        }
//        if (!millstone.getCarrier().isEmpty()) {
//            return ItemStack.EMPTY;
//        }
//        ItemStack output = millstone.getOutput();
//        if (output.isEmpty()) {
//            return ItemStack.EMPTY;
//        }
//        if (simulate) {
//            return output.copyWithCount(amount);
//        }
//        ItemStack split = output.split(amount);
//        this.millstone.refresh();
//        return split;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 64;
//    }
//
//    @Override
//    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
//        return false;
//    }
//}
