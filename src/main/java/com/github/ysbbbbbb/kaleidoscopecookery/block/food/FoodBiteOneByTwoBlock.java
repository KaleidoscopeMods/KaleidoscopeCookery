package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteAnimateTicks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class FoodBiteOneByTwoBlock extends FoodBiteBlock {
    public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 1);
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public FoodBiteOneByTwoBlock(FoodProperties foodProperties, int maxBites,
                                 @Nullable FoodBiteAnimateTicks.AnimateTick animateTick) {
        super(foodProperties, maxBites, animateTick);
        this.registerDefaultState(this.stateDefinition.any().setValue(bites, 0).setValue(FACING, Direction.SOUTH).setValue(POSITION, RIGHT));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        int position = state.getValue(POSITION);
        Direction facing = state.getValue(FACING);

        // 如果当前方块在左，更新的方向来自于右侧，或者当前方块在右，更新的方向来自于左侧
        if ((position == LEFT && direction == facing.getCounterClockWise())
            || (position == RIGHT && direction == facing.getClockWise())) {
            // 一侧方块不是同类型或朝向、位置不对，移除当前方块
            if (!neighborState.is(this) || neighborState.getValue(FACING) != facing || neighborState.getValue(POSITION) == position) {
                return Blocks.AIR.defaultBlockState();
            }
            // 如果一侧 bite 次数和当前不一致，修改当前方块的 bite 次数
            int neighborBites = neighborState.getValue(bites);
            if (neighborBites != state.getValue(bites)) {
                return state.setValue(bites, neighborBites);
            }
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative() && state.getValue(POSITION) == LEFT) {
            BlockPos right = pos.relative(state.getValue(FACING).getCounterClockWise());
            BlockState rightState = level.getBlockState(right);
            if (rightState.is(state.getBlock()) && rightState.getValue(POSITION) == RIGHT) {
                BlockState airBlockState = rightState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(right, airBlockState, Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, right, Block.getId(rightState));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos rightPos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection().getOpposite();
        Level level = context.getLevel();

        // 检查左侧是否可以放置
        BlockPos leftPos = rightPos.relative(facing.getClockWise());
        if (level.getBlockState(leftPos).canBeReplaced(context)) {
            return super.getStateForPlacement(context);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        Direction facing = pState.getValue(FACING);
        BlockPos leftPos = pPos.relative(facing.getClockWise());
        BlockState leftState = pState.setValue(POSITION, LEFT);
        pLevel.setBlock(leftPos, leftState, Block.UPDATE_ALL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POSITION);
    }

    @Override
    protected void createBitesBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(bites, FACING, POSITION);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder pParams) {
        // 左侧不掉落
        if (state.getValue(POSITION) == LEFT) {
            return Collections.emptyList();
        }
        return super.getDrops(state, pParams);
    }
}
