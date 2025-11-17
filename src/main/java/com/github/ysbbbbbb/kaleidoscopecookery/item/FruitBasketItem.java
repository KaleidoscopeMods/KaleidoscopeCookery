package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.util.forge.ItemStackHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FruitBasketItem extends BlockItem {
    private static final String TAG = "BlockEntityTag";
    private static final int MAX_SLOTS = 8;

    public FruitBasketItem() {
        super(ModBlocks.FRUIT_BASKET, new Properties().stacksTo(1));
    }

    public static ItemStackHandler getItems(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(TAG)) {
            CompoundTag compound = tag.getCompound(TAG);
            if (compound.contains(FruitBasketBlockEntity.ITEMS)) {
                ItemStackHandler handler = new ItemStackHandler(MAX_SLOTS);
                handler.deserializeNBT(compound.getCompound(FruitBasketBlockEntity.ITEMS));
                return handler;
            }
        }
        return new ItemStackHandler(MAX_SLOTS);
    }

    public static void saveItems(ItemStack stack, ItemStackHandler items) {
        CompoundTag beTag = stack.getOrCreateTagElement(TAG);
        beTag.put(FruitBasketBlockEntity.ITEMS, items.serializeNBT());
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(TAG)) {
            CompoundTag compound = tag.getCompound(TAG);
            if (compound.contains(FruitBasketBlockEntity.ITEMS)) {
                CompoundTag itemsTag = compound.getCompound(FruitBasketBlockEntity.ITEMS);
                NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(itemsTag, items);
                return Optional.of(new ItemContainerTooltip(items));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}
