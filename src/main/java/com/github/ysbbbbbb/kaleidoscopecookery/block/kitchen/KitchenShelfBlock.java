package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

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
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(SNEAK_PLACED, false)
                .setValue(PART, Part.SINGLE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite())
                .setValue(SNEAK_PLACED, context.getPlayer() != null && context.getPlayer().isShiftKeyDown());
        return updatePart(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, SNEAK_PLACED, PART);
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

        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
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
        if (level.getBlockEntity(pos) instanceof KitchenShelfBlockEntity shelf) {
            shelf.dispense(state);
        }
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
            shelf.showCountHint(hit.getDirection());
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
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && !level.isClientSide
            && level.getBlockEntity(pos) instanceof KitchenShelfBlockEntity shelf) {
            ItemStack stored = shelf.getStoredStack();
            if (!stored.isEmpty()) {
                popResource(level, pos, stored.copy());
                shelf.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .ifPresent(handler -> handler.extractItem(0, Integer.MAX_VALUE, false));
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
