package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TableBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.util.BlockDrop;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.github.ysbbbbbb.kaleidoscopecookery.util.TodoCheck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopecookery.util.CarpetColor.getCarpetByColor;
import static com.github.ysbbbbbb.kaleidoscopecookery.util.CarpetColor.getColorByCarpet;

public class TableBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 3);
    public static final BooleanProperty HAS_CARPET = BooleanProperty.create("has_carpet");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final int SINGLE = 0;
    public static final int LEFT = 1;
    public static final int MIDDLE = 2;
    public static final int RIGHT = 3;

    private static final VoxelShape FACE = Block.box(0, 13, 0, 16, 16, 16);

    public TableBlock() {
        super(Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .noOcclusion()
                .ignitedByLava());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AXIS, Direction.Axis.Z)
                .setValue(POSITION, SINGLE)
                .setValue(HAS_CARPET, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND) {
            if (itemInHand.is(ItemTags.WOOL_CARPETS)) {
                return useWithCarpets(state, level, pos, player, itemInHand);
            } else if (level.getBlockEntity(pos) instanceof TableBlockEntity table) {
                return useWithOther(level, pos, player, hand, table, itemInHand);
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @NotNull
    private InteractionResult useWithOther(Level level, BlockPos pos, Player player, InteractionHand hand, TableBlockEntity table, ItemStack itemInHand) {
        NonNullList<ItemStack> tableItems = table.getItems();
        Pair<Integer, ItemStack> lastStack = ItemUtils.getLastStack(tableItems);
        Integer tableIndex = lastStack.getLeft();
        ItemStack tableItem = lastStack.getRight();

        boolean handEmpty = itemInHand.isEmpty();

        // 玩家手为空，桌子有物品：取出桌子物品
        if (handEmpty && !tableItem.isEmpty()) {
            level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, player.getSoundSource(), 1.0F, 1.0F);
            BlockDrop.popResource(level, pos, 0.75, tableItem.copy());
            tableItems.set(tableIndex, ItemStack.EMPTY);
            table.refresh();
            return InteractionResult.SUCCESS;
        }

        // 玩家手有物品，并且可以放入物品时
        if (!handEmpty && tableIndex < (tableItems.size() - 1)) {
            ItemStack split = itemInHand.split(1);
            if (tableItem.isEmpty()) {
                tableItems.set(tableIndex, split);
            } else {
                tableItems.set(tableIndex + 1, split);
            }
            table.refresh();
            level.playSound(player, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, player.getSoundSource(), 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @NotNull
    private InteractionResult useWithCarpets(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemInHand) {
        @Nullable DyeColor dyeColor = getColorByCarpet(itemInHand.getItem());
        boolean hasCarpet = state.getValue(HAS_CARPET);
        if (dyeColor == null) {
            return InteractionResult.PASS;
        }

        // 第一种情况，桌子上没有地毯
        if (!hasCarpet) {
            level.setBlockAndUpdate(pos, state.setValue(HAS_CARPET, true));
            if (level.getBlockEntity(pos) instanceof TableBlockEntity tableBlockEntity) {
                level.playSound(null, pos, SoundType.WOOL.getPlaceSound(), player.getSoundSource(), 1.0F, 1.0F);
                tableBlockEntity.setColor(dyeColor);
                tableBlockEntity.refresh();
                itemInHand.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }

        // 第二种情况：有地毯，但是颜色不一致
        if (hasCarpet && level.getBlockEntity(pos) instanceof TableBlockEntity tableBlockEntity && tableBlockEntity.getColor() != dyeColor) {
            // 掉落原地毯
            DyeColor originalColor = tableBlockEntity.getColor();
            ItemStack carpetItem = getCarpetByColor(originalColor).getDefaultInstance();
            BlockDrop.popResource(level, pos, 0.75, carpetItem);
            level.playSound(null, pos, SoundType.WOOL.getPlaceSound(), player.getSoundSource(), 1.0F, 1.0F);

            tableBlockEntity.setColor(dyeColor);
            tableBlockEntity.refresh();
            level.setBlockAndUpdate(pos, state.setValue(HAS_CARPET, true));
            itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @TodoCheck
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative() && level.getBlockEntity(pos) instanceof TableBlockEntity tableBlockEntity) {
//            ItemStackHandler items = tableBlockEntity.getItems();
//            for (int i = 0; i < items.getSlots(); i++) {
//                ItemStack stack = items.getStackInSlot(i);
//                if (!stack.isEmpty()) {
//                    popResource(level, pos, stack);
//                    items.setStackInSlot(i, ItemStack.EMPTY);
//                }
//            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParamsBuilder) {
        List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
        BlockEntity parameter = lootParamsBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (parameter instanceof TableBlockEntity tableBlockEntity) {
            if (state.getValue(HAS_CARPET)) {
                Item carpet = getCarpetByColor(tableBlockEntity.getColor());
                drops.add(new ItemStack(carpet));
            }
            NonNullList<ItemStack> items = tableBlockEntity.getItems();
            for (ItemStack item : items) {
                drops.add(item.copy());
            }
        }
        return drops;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, POSITION, HAS_CARPET, WATERLOGGED);
    }

    private BlockState checkEastWestState(LevelAccessor levelAccessor, BlockPos pos, BlockState baseState) {
        // 如果自己本身已经成为 Z 方向的组合桌，那么不更新
        if (baseState.getValue(AXIS) == Direction.Axis.Z && baseState.getValue(POSITION) != SINGLE) {
            return baseState;
        }
        BlockState westState = levelAccessor.getBlockState(pos.west());
        BlockState eastState = levelAccessor.getBlockState(pos.east());
        if (checkIfShouldLink(eastState, Direction.Axis.Z) && checkIfShouldLink(westState, Direction.Axis.Z)) {
            return baseState.setValue(POSITION, MIDDLE).setValue(AXIS, Direction.Axis.X);
        }
        if (checkIfShouldLink(eastState, Direction.Axis.Z) && !checkIfShouldLink(westState, Direction.Axis.Z)) {
            return baseState.setValue(POSITION, LEFT).setValue(AXIS, Direction.Axis.X);
        }
        if (!checkIfShouldLink(eastState, Direction.Axis.Z) && checkIfShouldLink(westState, Direction.Axis.Z)) {
            return baseState.setValue(POSITION, RIGHT).setValue(AXIS, Direction.Axis.X);
        }
        return baseState.setValue(POSITION, SINGLE);
    }

    private BlockState checkNorthSouthState(LevelAccessor levelAccessor, BlockPos pos, BlockState baseState) {
        // 如果自己本身已经成为 X 方向的组合桌，那么不更新
        if (baseState.getValue(AXIS) == Direction.Axis.X && baseState.getValue(POSITION) != SINGLE) {
            return baseState;
        }
        BlockState northState = levelAccessor.getBlockState(pos.north());
        BlockState southState = levelAccessor.getBlockState(pos.south());
        if (checkIfShouldLink(southState, Direction.Axis.X) && checkIfShouldLink(northState, Direction.Axis.X)) {
            return baseState.setValue(POSITION, MIDDLE).setValue(AXIS, Direction.Axis.Z);
        }
        if (checkIfShouldLink(southState, Direction.Axis.X) && !checkIfShouldLink(northState, Direction.Axis.X)) {
            return baseState.setValue(POSITION, LEFT).setValue(AXIS, Direction.Axis.Z);
        }
        if (!checkIfShouldLink(southState, Direction.Axis.X) && checkIfShouldLink(northState, Direction.Axis.X)) {
            return baseState.setValue(POSITION, RIGHT).setValue(AXIS, Direction.Axis.Z);
        }
        return baseState.setValue(POSITION, SINGLE);
    }

    private boolean checkIfShouldLink(BlockState state, Direction.Axis axis) {
        if (!state.is(this)) {
            return false;
        }
        // 如果对方与修正方向不同，且对方并不是独立状态，则不可以接
        if (state.getValue(AXIS) == axis) {
            return state.getValue(POSITION) == SINGLE;
        }
        // 如果双方方向相同且毗邻，则无论如何都可以接
        return true;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        if (direction.getAxis() == Direction.Axis.X) {
            return checkEastWestState(levelAccessor, pos, state);
        }
        if (direction.getAxis() == Direction.Axis.Z) {
            return checkNorthSouthState(levelAccessor, pos, state);
        }
        return state;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction direction = context.getHorizontalDirection();
        boolean hasWater = level.getFluidState(clickedPos).getType() == Fluids.WATER;
        BlockState base = this.defaultBlockState().setValue(WATERLOGGED, hasWater);

        if (direction.getAxis() == Direction.Axis.X) {
            return checkNorthSouthState(level, clickedPos, base);
        } else if (direction.getAxis() == Direction.Axis.Z) {
            return checkEastWestState(level, clickedPos, base);
        }

        return base;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return FACE;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TableBlockEntity(pos, state);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}
