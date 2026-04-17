package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ITeapot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.util.FluidUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.ItemInteractionResult.*;

@SuppressWarnings({"deprecation", "unchecked"})
public class TeapotBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
    public static final MapCodec<TeapotBlock> CODEC = simpleCodec(p -> new TeapotBlock());
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 2);

    public static final int COMMON = 0;
    public static final int BASED = 1;
    public static final int CHAINED = 2;

    public static final VoxelShape AABB = Shapes.or(
            Block.box(3, 0, 3, 13, 6, 13),
            Block.box(5, 6, 5, 11, 8, 11)
    );

    public TeapotBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.LANTERN)
                .mapColor(MapColor.COLOR_ORANGE)
                .noOcclusion()
                .instabreak());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(VARIANT, COMMON));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
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

        // 下方无法支撑，添加基座
        int variant = state.getValue(VARIANT);
        if (direction == Direction.DOWN && variant != CHAINED) {
            if (!neighborState.isFaceSturdy(levelAccessor, neighborPos, Direction.UP)) {
                return state.setValue(VARIANT, BASED);
            } else {
                return state.setValue(VARIANT, COMMON);
            }
        }

        // 上方无法支撑，取消锁链
        if (direction == Direction.UP && variant != BASED) {
            if (canSupportCenter(levelAccessor, neighborPos, Direction.DOWN)) {
                return state.setValue(VARIANT, CHAINED);
            } else {
                return state.setValue(VARIANT, COMMON);
            }
        }

        return super.updateShape(state, direction, neighborState, levelAccessor, pos, neighborPos);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ITeapot teapot)) {
            return PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        ItemStack mainHandItem = player.getMainHandItem();
        var capability = mainHandItem.getCapability(Capabilities.FluidHandler.ITEM);

        // 加入茶水
        if (capability != null) {
            // 如果手持物有流体，那么灌入
            if (FluidUtils.hasFluid(mainHandItem)) {
                boolean result = teapot.addTeaFluid(level, player, mainHandItem);
                return result ? SUCCESS : CONSUME;
            }

            // 否则取出
            boolean result = teapot.removeTeaFluid(level, player, mainHandItem);
            return result ? SUCCESS : CONSUME;
        }

        // 加入原料
        if (!mainHandItem.isEmpty()) {
            return teapot.addIngredient(level, player, mainHandItem) ? SUCCESS : CONSUME;
        }

        // 取出原料
        if (mainHandItem.isEmpty() && player.isSecondaryUseActive()) {
            return teapot.removeIngredient(level, player) ? SUCCESS : CONSUME;
        }

        // 拿起茶壶
        if (mainHandItem.isEmpty() && !player.isSecondaryUseActive()) {
            return teapot.takeTeapot(level, player) ? SUCCESS : CONSUME;
        }
        return PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
