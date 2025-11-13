package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IShawarmaSpit;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ShawarmaSpitBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShawarmaSpitBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
    public static final MapCodec<ShawarmaSpitBlock> CODEC = simpleCodec(p -> new ShawarmaSpitBlock());
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final VoxelShape UPPER_AABB = Shapes.or(
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(6, 0, 6, 10, 14, 10)
    );
    public static final VoxelShape LOWER_AABB = Shapes.or(
            Block.box(0, 0, 0, 16, 7, 16),
            Block.box(6, 7, 6, 10, 16, 10)
    );

    public ShawarmaSpitBlock() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .noOcclusion()
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .lightLevel(state -> state.getValue(POWERED) ? 8 : 0)
                .sound(SoundType.METAL));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(WATERLOGGED, false)
                .setValue(POWERED, false));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Nullable
    @SuppressWarnings("all")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isShiftKeyDown()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof IShawarmaSpit shawarmaSpit) {
            ItemStack heldItem = player.getItemInHand(hand);
            if (shawarmaSpit.onPutCookingItem(level, heldItem)) {
                return ItemInteractionResult.SUCCESS;
            } else if (shawarmaSpit.onTakeCookedItem(level, player)) {
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlocks.SHAWARMA_SPIT_BE, (levelIn, blockPos, blockState, spit) -> {
            if (blockState.getValue(POWERED)) {
                spit.tick();
            }
        });
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? LOWER_AABB : UPPER_AABB;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        DoubleBlockHalf half = state.getValue(HALF);
        boolean isLowerHalf = half == DoubleBlockHalf.LOWER && direction == Direction.UP;
        boolean isUpperHalf = half == DoubleBlockHalf.UPPER && direction == Direction.DOWN;
        if (direction.getAxis() == Direction.Axis.Y && (isLowerHalf || isUpperHalf)) {
            if (neighborState.is(this) && neighborState.getValue(HALF) != half) {
                return state.setValue(FACING, neighborState.getValue(FACING))
                        .setValue(POWERED, neighborState.getValue(POWERED));
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, levelAccessor, currentPos, neighborPos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        Direction direction = state.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN;
        boolean powered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.relative(direction));
        if (!state.is(block) && powered != state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                BlockPos below = pos.below();
                BlockState belowState = level.getBlockState(below);
                if (belowState.is(state.getBlock()) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    dropCookItems(level, below);
                    BlockState airBlockState = belowState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    level.setBlock(below, airBlockState, Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                    level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, below, Block.getId(belowState));
                }
            } else {
                dropCookItems(level, pos);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    private void dropCookItems(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ShawarmaSpitBlockEntity shawarmaSpit) {
            if (!shawarmaSpit.cookingItem.isEmpty()) {
                popResource(level, pos, shawarmaSpit.cookingItem.copy());
                shawarmaSpit.cookingItem = ItemStack.EMPTY;
            } else if (!shawarmaSpit.cookedItem.isEmpty()) {
                popResource(level, pos, shawarmaSpit.cookedItem.copy());
                shawarmaSpit.cookedItem = ItemStack.EMPTY;
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        FluidState fluidState = context.getLevel().getFluidState(pos);
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            boolean isPowered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection())
                    .setValue(POWERED, isPowered)
                    .setValue(HALF, DoubleBlockHalf.LOWER)
                    .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        FluidState fluidState = level.getFluidState(pos);
        BlockState blockState = state.setValue(HALF, DoubleBlockHalf.UPPER)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        level.setBlockAndUpdate(pos.above(), blockState);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, WATERLOGGED, POWERED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShawarmaSpitBlockEntity(pos, state);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParamsBuilder) {
        List<ItemStack> drops;
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            drops = super.getDrops(state, lootParamsBuilder);
        } else {
            drops = Lists.newArrayList();
        }
        BlockEntity parameter = lootParamsBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (parameter instanceof ShawarmaSpitBlockEntity shawarmaSpit) {
            if (!shawarmaSpit.cookingItem.isEmpty()) {
                drops.add(shawarmaSpit.cookingItem.copy());
            } else if (!shawarmaSpit.cookedItem.isEmpty()) {
                drops.add(shawarmaSpit.cookedItem.copy());
            }
        }
        return drops;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.shawarma_spit").withStyle(ChatFormatting.GRAY));
    }
}
