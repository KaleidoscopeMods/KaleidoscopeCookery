package com.github.ysbbbbbb.kaleidoscopecookery.block.crop;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ChiliCropBlock extends BaseCropBlock {
    public ChiliCropBlock() {
        super(ModItems.RED_CHILI, ModItems.CHILI_SEED);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.is(ModItems.SICKLE.get())) {
            return InteractionResult.PASS;
        }
        if (state.getValue(AGE) >= this.getMaxAge()) {
            // 20% 几率掉落青椒
            if (level.getRandom().nextInt(5) == 0) {
                Block.popResource(level, pos, ModItems.GREEN_CHILI.get().getDefaultInstance());
            } else {
                Block.popResource(level, pos, this.result.get().getDefaultInstance());
            }
            super.onUseBreakCrop(level, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
