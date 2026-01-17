package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.food;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FoodBiteThreeByThreeBlockEntity extends BaseBlockEntity {
    public FoodBiteThreeByThreeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.FOOD_BITE_THREE_BY_THREE_BE.get(), pos, state);
    }
}
