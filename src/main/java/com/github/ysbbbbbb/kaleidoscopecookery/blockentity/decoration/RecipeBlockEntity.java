package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class RecipeBlockEntity extends BaseBlockEntity {
    private static final String SHOW_ITEMS = "ShowItems";
    private final SimpleContainer items = new SimpleContainer(1);

    public RecipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.RECIPE_BLOCK_BE, pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(SHOW_ITEMS, ContainerHelper.saveAllItems(new CompoundTag(), this.items.items));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(SHOW_ITEMS)) {
            CompoundTag compound = tag.getCompound(SHOW_ITEMS);
            ContainerHelper.loadAllItems(compound, this.items.items);
        }
    }

    public List<ItemStack> getItems() {
        return items.items;
    }
}
