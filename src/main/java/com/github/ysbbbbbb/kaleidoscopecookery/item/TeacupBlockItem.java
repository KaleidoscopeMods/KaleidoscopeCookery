package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.block.food.TeacupBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class TeacupBlockItem extends BlockItem {
    public TeacupBlockItem(Block block) {
        this(block, new Properties()
                .stacksTo(16));
    }

    public TeacupBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);
        Block self = this.getBlock();

        if (player != null && tryIncreaseCount(self, state, level, pos, stack, player)) {
            return InteractionResult.SUCCESS;
        }
        return this.place(new BlockPlaceContext(context));
    }

    protected boolean tryIncreaseCount(Block self, BlockState state, Level level, BlockPos pos, ItemStack stack, Player player) {
        if (self instanceof TeacupBlock && state.getBlock() instanceof TeacupBlock block && block.tryIncreaseCount(level, pos, state, stack, false)) {
            SoundType soundType = state.getSoundType(level, pos, player);
            SoundEvent sound = this.getPlaceSound(state, level, pos, player);
            level.playSound(
                    player, pos, sound, SoundSource.BLOCKS,
                    (soundType.getVolume() + 1) / 2f,
                    soundType.getPitch() * 0.8f
            );
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return true;
        }
        return false;
    }
}
