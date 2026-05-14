package com.github.ysbbbbbb.kaleidoscopecookery.block.misc;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.InteractionResult.PASS;

@SuppressWarnings({"deprecation"})
public class TrashCanBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
    /**
     * 吸取物品的范围
     */
    public static final VoxelShape SUCK_ZONE = Block.box(0, 15, 0, 16, 16, 16);
    public static final VoxelShape AABB = Shapes.or(
            Block.box(2, 0, 2, 14, 15, 14),
            Block.box(1, 12, 1, 15, 15, 15)
    );

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public TrashCanBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .mapColor(MapColor.COLOR_BLACK)
                .noOcclusion()
                .strength(1.5F, 6.0F));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
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
        if (level.isClientSide) {
            return createTickerHelper(blockEntityType, ModBlocks.TRASH_CAN_BE.get(),
                    (lvl, blockPos, blockState, trashCan) -> trashCan.clientTick(lvl));
        }
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TrashCanBlockEntity(pos, state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(state, direction, neighborState, levelAccessor, pos, neighborPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return PASS;
        }
        ItemStack itemInHand = player.getItemInHand(hand);
        if (level.getBlockEntity(pos) instanceof TrashCanBlockEntity trashCan) {
            if (itemInHand.isEmpty() && player.isSecondaryUseActive()) {
                trashCan.withdrawItem(player);
                return InteractionResult.SUCCESS;
            }

            if (!itemInHand.isEmpty()) {
                trashCan.putItem(itemInHand);
                return InteractionResult.SUCCESS;
            }
        }
        return PASS;
    }

    @Override
    public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        levelAccessor.getEntitiesOfClass(SitEntity.class, new AABB(pos)).forEach(Entity::discard);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance);
        // 如果是玩家
        if (entity instanceof Player player && player.getVehicle() == null && fallDistance > 1f) {
            List<SitEntity> entities = level.getEntitiesOfClass(SitEntity.class, new AABB(pos));
            if (!entities.isEmpty()) {
                return;
            }
            if (!level.isClientSide) {
                SitEntity entitySit = new SitEntity(level, pos, 0.875, SitEntity.TRASH_CAN);
                entitySit.setYRot(state.getValue(FACING).toYRot());
                level.addFreshEntity(entitySit);
                player.startRiding(entitySit, true);
            }

            // 清除周围生物对玩家的敌意
            level.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(32)).forEach(e -> {
                if (e.getTarget() == player) {
                    e.setTarget(null);
                }
            });

            // 播放进入动画
            if (level.getBlockEntity(pos) instanceof TrashCanBlockEntity trashCan) {
                trashCan.enterState.start((int) level.getGameTime());
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // 只有充能后才能吸取
        if (state.getValue(POWERED) && level.getBlockEntity(pos) instanceof TrashCanBlockEntity trashCan) {
            trashCan.entityInside(level, pos, entity);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean isMoving) {
        boolean isPowered = level.hasNeighborSignal(pos);
        if (isPowered != state.getValue(POWERED)) {
            level.setBlockAndUpdate(pos, state.setValue(POWERED, isPowered));
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, level.isWaterAt(context.getClickedPos()));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return AABB;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        BlockEntity parameter = params.getParameter(LootContextParams.BLOCK_ENTITY);
        if (parameter instanceof TrashCanBlockEntity trashCanBlock) {
            for (int i = 0; i < trashCanBlock.getStorage().getSlots(); i++) {
                ItemStack stack = trashCanBlock.getStorage().getStackInSlot(i);
                if (!stack.isEmpty()) {
                    drops.add(stack);
                }
            }
        }
        return drops;
    }
}
