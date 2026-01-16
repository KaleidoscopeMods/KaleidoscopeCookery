package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.NinePart;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.food.FoodBiteThreeByThreeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteAnimateTicks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * 特殊的方块类食物，占地 3x3
 */
public class FoodBiteThreeByThreeBlock extends FoodBiteBlock implements EntityBlock {
    public static final EnumProperty<NinePart> PART = EnumProperty.create("part", NinePart.class);

    // 是一个 40x40x2 的形状
    private static final VoxelShape LEFT_UP = Block.box(4, 0, 4, 16, 2, 16);
    private static final VoxelShape UP = Block.box(0, 0, 4, 16, 2, 16);
    private static final VoxelShape RIGHT_UP = Block.box(0, 0, 4, 12, 2, 16);
    private static final VoxelShape LEFT_CENTER = Block.box(4, 0, 0, 16, 2, 16);
    private static final VoxelShape CENTER = Block.box(0, 0, 0, 16, 2, 16);
    private static final VoxelShape RIGHT_CENTER = Block.box(0, 0, 0, 12, 2, 16);
    private static final VoxelShape LEFT_DOWN = Block.box(4, 0, 0, 16, 2, 12);
    private static final VoxelShape DOWN = Block.box(0, 0, 0, 16, 2, 12);
    private static final VoxelShape RIGHT_DOWN = Block.box(0, 0, 0, 12, 2, 12);

    public FoodBiteThreeByThreeBlock(FoodProperties foodProperties, int maxBites,
                                     @Nullable FoodBiteAnimateTicks.AnimateTick animateTick) {
        super(foodProperties, maxBites, animateTick);
        this.registerDefaultState(this.stateDefinition.any().setValue(bites, 0).setValue(FACING, Direction.SOUTH).setValue(PART, NinePart.CENTER));
    }

    private static void handleRemove(Level world, BlockPos pos, BlockState state, @Nullable Player player) {
        if (world.isClientSide) {
            return;
        }
        NinePart part = state.getValue(PART);
        BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
        BlockEntity te = world.getBlockEntity(centerPos);
        if (!(te instanceof FoodBiteThreeByThreeBlockEntity)) {
            return;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos offsetPos = centerPos.offset(i, 0, j);
                if (i == 0 && j == 0) {
                    world.destroyBlock(offsetPos, true, player);
                } else {
                    world.setBlock(offsetPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        NinePart part = state.getValue(PART);
        BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
        BlockState centerState = level.getBlockState(centerPos);
        if (!centerState.is(this)) {
            return InteractionResult.PASS;
        }

        // 将使用逻辑全部交给中心部分处理
        ItemStack itemInHand = player.getItemInHand(hand);
        int bites = centerState.getValue(this.bites);
        if (bites >= getMaxBites()) {
            handleRemove(level, centerPos, centerState, player);
            return InteractionResult.SUCCESS;
        }
        if (level.isClientSide) {
            if (eat(level, centerPos, centerState, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
            if (itemInHand.isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }
        return eat(level, centerPos, centerState, player);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        handleRemove(world, pos, state, player);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        handleRemove(world, pos, state, null);
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos centerPos = context.getClickedPos();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos searchPos = centerPos.offset(i, 0, j);
                if (!context.getLevel().getBlockState(searchPos).canBeReplaced(context)) {
                    return null;
                }
            }
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.isClientSide) {
            return;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos searchPos = pos.offset(i, 0, j);
                NinePart part = NinePart.getPartByPos(i, j);
                if (part != null && !part.isCenter()) {
                    worldIn.setBlock(searchPos, state.setValue(PART, part), Block.UPDATE_ALL);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, PART);
    }

    @Override
    protected void createBitesBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(bites, FACING, PART);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART).isCenter()) {
            return new FoodBiteThreeByThreeBlockEntity(pos, state);
        }
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        NinePart value = pState.getValue(PART);
        return switch (value) {
            case LEFT_UP -> LEFT_UP;
            case UP -> UP;
            case RIGHT_UP -> RIGHT_UP;
            case LEFT_CENTER -> LEFT_CENTER;
            case CENTER -> CENTER;
            case RIGHT_CENTER -> RIGHT_CENTER;
            case LEFT_DOWN -> LEFT_DOWN;
            case DOWN -> DOWN;
            case RIGHT_DOWN -> RIGHT_DOWN;
        };
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder pParams) {
        // 只有中心部分掉落物品
        if (state.getValue(PART) != NinePart.CENTER) {
            return Collections.emptyList();
        }
        return super.getDrops(state, pParams);
    }
}
