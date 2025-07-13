package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TableBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.util.BlockDrop;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;
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
        ItemStackHandler tableItems = table.getItems();
        Pair<Integer, ItemStack> lastStack = ItemUtils.getLastStack(tableItems);
        Integer tableIndex = lastStack.getLeft();
        ItemStack tableItem = lastStack.getRight();

        boolean handEmpty = itemInHand.isEmpty();

        // 玩家手为空，桌子有物品：取出桌子物品
        if (handEmpty && !tableItem.isEmpty()) {
            level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, player.getSoundSource(), 1.0F, 1.0F);
            BlockDrop.popResource(level, pos, 0.75, tableItem.copy());
            tableItems.setStackInSlot(tableIndex, ItemStack.EMPTY);
            table.setChanged();
            return InteractionResult.SUCCESS;
        }

        // 玩家手有物品，并且可以放入物品时
        if (!handEmpty && tableIndex < (tableItems.getSlots() - 1)) {
            if (tableItem.isEmpty()) {
                tableItems.setStackInSlot(tableIndex, itemInHand.copy());
            } else {
                tableItems.setStackInSlot(tableIndex + 1, itemInHand.copy());
            }
            table.setChanged();
            player.setItemInHand(hand, ItemStack.EMPTY);
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
                tableBlockEntity.setChanged();
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
            tableBlockEntity.setChanged();
            level.setBlockAndUpdate(pos, state.setValue(HAS_CARPET, true));
            itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
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
            ItemStackHandler items = tableBlockEntity.getItems();
            for (int i = 0; i < items.getSlots(); i++) {
                drops.add(items.getStackInSlot(i).copy());
            }
        }
        return drops;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, POSITION, HAS_CARPET, WATERLOGGED);
    }

    /**
     * 根据指定方向和位置，计算桌子的连接状态和朝向
     */
    private BlockState getConnectedState(LevelAccessor levelAccessor, BlockPos pos, BlockState baseState) {
        BlockState northState = levelAccessor.getBlockState(pos.north());
        BlockState southState = levelAccessor.getBlockState(pos.south());
        if (northState.is(this) && southState.is(this)) {
            return baseState.setValue(POSITION, MIDDLE).setValue(AXIS, Direction.Axis.Z);
        }
        if (!northState.is(this) && southState.is(this)) {
            return baseState.setValue(POSITION, LEFT).setValue(AXIS, Direction.Axis.Z);
        }
        if (northState.is(this) && !southState.is(this)) {
            return baseState.setValue(POSITION, RIGHT).setValue(AXIS, Direction.Axis.Z);
        }

        BlockState westState = levelAccessor.getBlockState(pos.west());
        BlockState eastState = levelAccessor.getBlockState(pos.east());
        if (eastState.is(this) && westState.is(this)) {
            return baseState.setValue(POSITION, MIDDLE).setValue(AXIS, Direction.Axis.X);
        }
        if (eastState.is(this) && !westState.is(this)) {
            return baseState.setValue(POSITION, LEFT).setValue(AXIS, Direction.Axis.X);
        }
        if (!eastState.is(this) && westState.is(this)) {
            return baseState.setValue(POSITION, RIGHT).setValue(AXIS, Direction.Axis.X);
        }

        return baseState.setValue(POSITION, SINGLE);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        if (direction.getAxis() == Direction.Axis.X) {
            BlockState northState = levelAccessor.getBlockState(pos.north());
            BlockState southState = levelAccessor.getBlockState(pos.south());
            if (northState.is(this) || southState.is(this)) {
                return state;
            }
        }
        if (direction.getAxis() == Direction.Axis.Z) {
            BlockState westState = levelAccessor.getBlockState(pos.west());
            BlockState eastState = levelAccessor.getBlockState(pos.east());
            if (westState.is(this) || eastState.is(this)) {
                return state;
            }
        }
        return getConnectedState(levelAccessor, pos, state);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        boolean hasWater = level.getFluidState(clickedPos).getType() == Fluids.WATER;
        BlockState base = this.defaultBlockState().setValue(WATERLOGGED, hasWater);
        return getConnectedState(level, clickedPos, base);
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
}
