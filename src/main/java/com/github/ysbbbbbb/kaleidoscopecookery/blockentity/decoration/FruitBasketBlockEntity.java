package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.util.forge.ItemStackHandler;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FruitBasketBlockEntity extends BaseBlockEntity {
    public static final String ITEMS = "BasketItems";
    private final SimpleContainer items = new SimpleContainer(8);

    public FruitBasketBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.FRUIT_BASKET_BE, pPos, pBlockState);
    }

    @SuppressWarnings("all")
    public void putOn(ItemStack stack) {
        if (!stack.getItem().canFitInsideContainerItems()) {
            return;
        }
        try (Transaction tx = Transaction.openOuter()) {
            InventoryStorage storage = InventoryStorage.of(this.items, null);
            long inserted = storage.insert(ItemVariant.of(stack), stack.getCount(), tx);
            if (inserted > 0) {
                tx.commit();
                stack.shrink((int) inserted);
                if (this.level != null) {
                    this.level.playSound(null, this.worldPosition, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
                }
                this.refresh();
            }
        }
    }

    @SuppressWarnings("all")
    public void takeOut(Player player) {
        for (int i = 0; i < items.getContainerSize(); i++) {
            ItemStack stack = items.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            try (Transaction tx = Transaction.openOuter()) {
                InventoryStorage storage = InventoryStorage.of(this.items, null);
                ItemVariant itemVariant = ItemVariant.of(stack);
                long extracted = storage.extract(itemVariant, stack.getCount(), tx);
                if (extracted > 0) {
                    tx.commit();
                    player.getInventory().placeItemBackInInventory(itemVariant.toStack((int) extracted));
                    if (this.level != null) {
                        this.level.playSound(null, this.worldPosition, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS);
                    }
                    this.refresh();
                }
                return;
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(ITEMS, ContainerHelper.saveAllItems(new CompoundTag(), this.items.items));
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains(ITEMS)) {
            CompoundTag compound = tag.getCompound(ITEMS);
            ContainerHelper.loadAllItems(compound, this.items.items);
        }
    }

    public ItemStackHandler getItems() {
        return new ItemStackHandler(items.items);
    }
}
