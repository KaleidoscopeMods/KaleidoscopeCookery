package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ISteamer;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SteamerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SteamerBlock extends FallingBlock implements EntityBlock, SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HALF = BooleanProperty.create("half");
    public static final BooleanProperty HAS_LID = BooleanProperty.create("has_lid");
    public static final BooleanProperty HAS_BASE = BooleanProperty.create("has_base");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape HALF_AABB = Block.box(1, 0, 1, 15, 8, 15);
    private static final VoxelShape FULL_AABB = Block.box(1, 0, 1, 15, 16, 15);

    public SteamerBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .instabreak()
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.BAMBOO));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, true)
                .setValue(HAS_LID, false)
                .setValue(HAS_BASE, false)
                .setValue(WATERLOGGED, false));
    }

    @Nullable
    @SuppressWarnings("all")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlocks.STEAMER_BE.get(),
                (levelIn, blockPos, blockState, steamer) -> steamer.tick(levelIn));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        // 如果蒸笼正下方是热源，则触发成就
        if (placer instanceof ServerPlayer player && level.getBlockEntity(pos) instanceof ISteamer steamer && steamer.hasHeatSource(level)) {
            ModTrigger.EVENT.trigger(player, ModEventTriggerType.USE_STEAMER);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState below = level.getBlockState(pos.below());
        if (isFree(below) && pos.getY() >= level.getMinBuildHeight()) {
            CompoundTag blockEntityTag = null;
            if (level.getBlockEntity(pos) instanceof SteamerBlockEntity steamer) {
                blockEntityTag = steamer.saveWithoutMetadata();
            }
            FallingBlockEntity fall = FallingBlockEntity.fall(level, pos, state.setValue(HAS_BASE, false));
            fall.blockData = blockEntityTag;
            this.falling(fall);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        levelAccessor.scheduleTick(pos, this, this.getDelayAfterPlace());
        // 如果下方是不完整方块，则添加基座
        if (direction == Direction.DOWN) {
            // 如果下方完全是空气，那么反而不需要添加基座了，让它自然掉落
            if (isFree(neighborState)) {
                state = state.setValue(HAS_BASE, false);
            } else {
                state = state.setValue(HAS_BASE, shouldHasBase(levelAccessor, pos));
            }
        }
        // 如果是上方方块是蒸笼，那么把盖子去掉
        if (direction == Direction.UP && neighborState.is(this)) {
            state = state.setValue(HAS_LID, false);
        }
        return state;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        // 空手 Shift 右击盖盖子、去掉盖子
        // 需要检查上方是否有方块，如果有方块则不能盖盖子
        Boolean hasLid = state.getValue(HAS_LID);
        if (itemInHand.isEmpty() && player.isSecondaryUseActive() && (hasLid || !level.getBlockState(pos.above()).is(this))) {
            level.setBlock(pos, state.setValue(HAS_LID, !hasLid), Block.UPDATE_ALL);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // 手持蒸笼，右击可以摞上去
        if (itemInHand.is(this.asItem())) {
            if (state.getValue(HALF) && itemInHand.getItem() instanceof SteamerItem steamerItem) {
                return steamerItem.place(new BlockPlaceContext(player, hand, itemInHand, hit));
            } else {
                return InteractionResult.PASS;
            }
        }

        // 其他情况交给 BlockEntity 处理
        if (level.getBlockEntity(pos) instanceof ISteamer steamer) {
            // 先尝试放入物品
            if (steamer.placeFood(level, player, itemInHand)) {
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            // 再尝试取出物品
            if (steamer.takeFood(level, player)) {
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clickedPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(clickedPos);
        FluidState fluidState = context.getLevel().getFluidState(clickedPos);
        BlockState resultState;
        if (blockState.is(this)) {
            resultState = blockState.setValue(HALF, false);
        } else {
            resultState = this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        }

        // 如果下方完全是空气，那么反而不需要添加基座了，让它自然掉落
        if (isFree(context.getLevel().getBlockState(clickedPos.below()))) {
            return resultState.setValue(HAS_BASE, false);
        }
        // 如果下方是不完整方块，则添加基座
        return resultState.setValue(HAS_BASE, shouldHasBase(context.getLevel(), clickedPos));
    }

    private boolean shouldHasBase(LevelAccessor level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        // 如果还是蒸笼，那么不添加基座
        if (belowState.is(this)) {
            return false;
        }
        // 如果是热源，强制添加基座
        if (belowState.hasProperty(BlockStateProperties.LIT)) {
            return true;
        }
        if (belowState.is(TagMod.HEAT_SOURCE_BLOCKS_WITHOUT_LIT)) {
            return true;
        }
        // 如果下方是完整方块或者是不可替换方块，则添加基座
        return !belowState.isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        // 落地时如果下方是不完整方块，则添加基座
        if (shouldHasBase(level, pos)) {
            level.setBlock(pos, state.setValue(HAS_BASE, true), Block.UPDATE_ALL);
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        ItemStack itemInHand = context.getItemInHand();
        // 如果是半块，那么可以摞上去
        return state.getValue(HALF) && itemInHand.is(this.asItem());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, HAS_LID, HAS_BASE, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return state.getValue(HALF) ? HALF_AABB : FULL_AABB;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SteamerBlockEntity(pos, state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParamsBuilder) {
        BlockEntity parameter = lootParamsBuilder.getParameter(LootContextParams.BLOCK_ENTITY);
        if (parameter instanceof SteamerBlockEntity steamer) {
            return steamer.dropAsItem();
        }
        return super.getDrops(state, lootParamsBuilder);
    }
}
