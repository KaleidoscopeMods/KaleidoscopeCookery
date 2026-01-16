package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.food;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class FoodBiteThreeByThreeBlockEntity extends BaseBlockEntity {
    public FoodBiteThreeByThreeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.FOOD_BITE_THREE_BY_THREE_BE.get(), pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.offset(-3, 0, -3), worldPosition.offset(3, 2, 3));
    }
}
