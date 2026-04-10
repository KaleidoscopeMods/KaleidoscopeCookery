package com.github.ysbbbbbb.kaleidoscopecookery.api.block;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teafluid.ITeaFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ITeacup {
    boolean tryPourTeaOn(Level level, BlockPos pos, BlockState state, ITeaFluid teaType, boolean simulate);

    boolean tryIncreaseCount(Level level, BlockPos pos, BlockState state, ItemStack stack, boolean simulate);
}
