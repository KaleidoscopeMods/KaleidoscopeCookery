package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ITeapot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"deprecation", "unchecked"})
public class TeapotBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 2);
    public static final int COMMON = 0;
    public static final int BASED = 1;
    public static final int CHAINED = 2;

    private static final VoxelShape AABB = Shapes.or(
            Block.box(0, 0, 2, 16, 15, 14)
    );

    public TeapotBlock() {
        super(Properties.of().noOcclusion().sound(SoundType.LANTERN));

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(VARIANT, COMMON));
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlocks.TEAPOT_BE.get(),
                (lvl, blockPos, blockState, teapot) -> teapot.tick(lvl));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeapotBlockEntity(pos, state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }

        // 上方无法支撑，取消锁链
        // 下方无法支撑，添加基座
        int variant = state.getValue(VARIANT);
        if (direction == Direction.DOWN && variant != CHAINED) {
            if (!neighborState.isFaceSturdy(levelAccessor, neighborPos, Direction.UP)) {
                return state.setValue(VARIANT, BASED);
            }
        }
        if (direction == Direction.UP && variant != BASED) {
            if (canSupportCenter(levelAccessor, neighborPos, Direction.DOWN)) {
                return state.setValue(VARIANT, CHAINED);
            }
        }

        return super.updateShape(state, direction, neighborState, levelAccessor, pos, neighborPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ITeapot teapot)) {
            return InteractionResult.PASS;
        }
        ItemStack mainHandItem = player.getMainHandItem();
        // 加入汤底
        if (teapot.addFluid(level, player, mainHandItem)) {
            return InteractionResult.SUCCESS;
        }
        // 加入原料
        if (!mainHandItem.isEmpty() && teapot.addIngredient(level, player, mainHandItem)) {
            return InteractionResult.SUCCESS;
        }
        // 取出原料
        if (mainHandItem.isEmpty() && player.isSecondaryUseActive() && teapot.removeIngredient(level, player)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        FluidState fluidState = level.getFluidState(context.getClickedPos());
        Direction clickFace = context.getClickedFace();
        BlockState blockState = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);

        // 如果点击的是上方，那么依据是否是可支持方块添加锁链
        BlockPos abovePos = context.getClickedPos().above();
        if (clickFace == Direction.DOWN && canSupportCenter(level, abovePos, Direction.DOWN)) {
            return blockState.setValue(VARIANT, CHAINED);
        }

        // 如果下方是不完整方块，则添加基座
        BlockPos belowPos = context.getClickedPos().below();
        BlockState belowState = level.getBlockState(belowPos);
        if (!belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
            return blockState.setValue(VARIANT, BASED);
        }
        return blockState;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, VARIANT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return AABB;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParamsBuilder) {
        BlockEntity parameter = lootParamsBuilder.getParameter(LootContextParams.BLOCK_ENTITY);
        if (parameter instanceof TeapotBlockEntity teapotBlockEntity) {
            return teapotBlockEntity.getDrops();
        }
        return super.getDrops(state, lootParamsBuilder);
    }
}
