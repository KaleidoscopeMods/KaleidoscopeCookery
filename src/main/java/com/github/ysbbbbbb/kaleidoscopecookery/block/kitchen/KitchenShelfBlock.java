package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

@SuppressWarnings("deprecation")
public class KitchenShelfBlock extends BaseEntityBlock implements EntityBlock {
    public static final BooleanProperty SNEAK_PLACED = BooleanProperty.create("sneak_placed");
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

    /**
     * 学习原版投掷器，4 tick 延迟发射
     */
    private static final int DISPENSE_DELAY = 4;

    public KitchenShelfBlock() {
        super(Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .isRedstoneConductor(KitchenShelfBlock::never)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(SNEAK_PLACED, false)
                .setValue(PART, Part.SINGLE));
    }

    private static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite())
                .setValue(SNEAK_PLACED, context.getPlayer() != null && context.getPlayer().isShiftKeyDown());
        return updatePart(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, SNEAK_PLACED, PART);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return updatePart(state, level, pos);
    }

    private BlockState updatePart(BlockState state, LevelAccessor level, BlockPos pos) {
        if (state.getValue(SNEAK_PLACED)) {
            return state.setValue(PART, Part.SINGLE);
        }

        Direction facing = state.getValue(HORIZONTAL_FACING);
        Direction clockwise = facing.getClockWise();
        Direction counterClockwise = facing.getCounterClockWise();

        BlockPos leftPos = pos.relative(counterClockwise);
        BlockPos rightPos = pos.relative(clockwise);

        boolean left = isConnectable(level.getBlockState(leftPos));
        boolean right = isConnectable(level.getBlockState(rightPos));

        Part part;
        if (left && right) {
            part = Part.MIDDLE;
        } else if (left) {
            part = Part.RIGHT;
        } else if (right) {
            part = Part.LEFT;
        } else {
            part = Part.SINGLE;
        }

        return state.setValue(PART, part);
    }

    private boolean isConnectable(BlockState state) {
        return state.getBlock() == this && !state.getValue(SNEAK_PLACED);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(this)) {
            updateRedstone(level, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block neighborBlock, BlockPos fromPos, boolean isMoving) {
        updateRedstone(level, pos);
    }

    private void updateRedstone(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel && serverLevel.getBlockEntity(pos) instanceof KitchenShelfBlockEntity shelf) {
            if (shelf.updatePower(serverLevel.hasNeighborSignal(pos))) {
                serverLevel.scheduleTick(pos, this, DISPENSE_DELAY);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        List<BlockPos> group = getConnectedShelves(level, pos);

        BlockPos poweredLeader = group.stream()
                .filter(level::hasNeighborSignal)
                .min(BlockPos::compareTo)
                .orElse(null);

        if (pos.equals(poweredLeader)) {
            for (BlockPos shelfPos : group) {
                if (level.getBlockEntity(shelfPos) instanceof KitchenShelfBlockEntity shelf) {
                    shelf.dispense(level.getBlockState(shelfPos));
                }
            }
        }
    }

    public static List<BlockPos> getConnectedShelves(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof KitchenShelfBlock) || state.getValue(SNEAK_PLACED)) {
            return List.of(pos);
        }

        Direction facing = state.getValue(HORIZONTAL_FACING);
        Direction axis = facing.getClockWise();

        List<BlockPos> shelves = Lists.newArrayList();
        BlockPos start = pos;

        // 先向一端找到起点
        while (isSameShelf(level, start.relative(axis.getOpposite()), facing)) {
            start = start.relative(axis.getOpposite());
        }

        // 然后遍历寻找到所有的方块
        for (BlockPos current = start; isSameShelf(level, current, facing); current = current.relative(axis)) {
            shelves.add(current);
        }
        return shelves;
    }

    private static boolean isSameShelf(Level level, BlockPos pos, Direction facing) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof KitchenShelfBlock
               && !state.getValue(SNEAK_PLACED)
               && state.getValue(HORIZONTAL_FACING) == facing;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack itemInHand = player.getItemInHand(hand);
        if (blockEntity instanceof KitchenShelfBlockEntity shelf
            && !itemInHand.is(ModItems.KITCHEN_SHELF.get())
            && shelf.interact(player, player.isShiftKeyDown())) {
            level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KitchenShelfBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return null;
        }
        return (lvl, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof KitchenShelfBlockEntity shelf) {
                KitchenShelfBlockEntity.serverTick(serverLevel, pos, blockState, shelf);
            }
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        BlockEntity parameter = params.getParameter(LootContextParams.BLOCK_ENTITY);
        if (parameter instanceof KitchenShelfBlockEntity shelf) {
            ItemStack stored = shelf.getStoredStack();
            if (!stored.isEmpty()) {
                int remaining = stored.getCount();
                int maxStackSize = stored.getMaxStackSize();
                while (remaining > 0) {
                    int dropCount = Math.min(remaining, maxStackSize);
                    drops.add(stored.copyWithCount(dropCount));
                    remaining -= dropCount;
                }
            }
        }
        return drops;
    }
}
