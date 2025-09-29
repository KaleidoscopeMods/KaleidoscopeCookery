package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.Optional;

public class FruitBasketItem extends BlockItem {
    private static final int MAX_SLOTS = 8;

    public FruitBasketItem() {
        super(ModBlocks.FRUIT_BASKET.get(), new Properties().stacksTo(1));
    }

    public static ItemStackHandler getItems(ItemStack stack) {
        ItemContainer container = stack.get(ModDataComponents.FRUIT_BASKET_ITEMS);
        if (container != null) {
            return container.items();
        }
        return new ItemStackHandler(MAX_SLOTS);
    }

    public static void saveItems(ItemStack stack, ItemStackHandler items) {
        stack.set(ModDataComponents.FRUIT_BASKET_ITEMS, new ItemContainer(items));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        ItemContainer container = stack.get(ModDataComponents.FRUIT_BASKET_ITEMS);
        if (container != null) {
            return Optional.of(new ItemContainerTooltip(container.items()));
        }
        return Optional.empty();
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    public record ItemContainer(ItemStackHandler items) {
        public static final Codec<ItemContainer> CODEC = ItemStack.OPTIONAL_CODEC.listOf().xmap(
                list -> {
                    ItemStackHandler handler = new ItemStackHandler(8);
                    for (int i = 0; i < Math.min(list.size(), handler.getSlots()); i++) {
                        handler.setStackInSlot(i, list.get(i));
                    }
                    return new ItemContainer(handler);
                },
                container -> {
                    ItemStackHandler handler = container.items();
                    List<ItemStack> output = Lists.newArrayList();
                    for (int i = 0; i < handler.getSlots(); i++) {
                        output.add(handler.getStackInSlot(i));
                    }
                    return output;
                }
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemContainer> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public ItemContainer decode(RegistryFriendlyByteBuf buffer) {
                CompoundTag compoundTag = buffer.readNbt();
                ItemStackHandler handler = new ItemStackHandler(8);
                if (compoundTag != null) {
                    handler.deserializeNBT(buffer.registryAccess(), compoundTag);
                }
                return new ItemContainer(handler);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, ItemContainer value) {
                CompoundTag compoundTag = value.items().serializeNBT(buffer.registryAccess());
                buffer.writeNbt(compoundTag);
            }
        };
    }
}
