package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnamelBasinBlock extends Block implements SimpleWaterloggedBlock {
    public static final int MAX_OIL_COUNT = 12;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty HAS_LID = BooleanProperty.create("has_lid");
    public static final IntegerProperty OIL_COUNT = IntegerProperty.create("oil_count", 0, MAX_OIL_COUNT);

    private static final VoxelShape AABB_NO_LID = Block.box(3, 0, 3, 13, 5, 13);
    private static final VoxelShape AABB = Shapes.or(AABB_NO_LID,
            Block.box(2.5, 5, 2.5, 13.5, 6, 13.5),
            Block.box(7, 6, 7, 9, 7, 9));

    public EnamelBasinBlock() {
        super(Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BELL)
                .strength(1.0F, 1.5F)
                .sound(SoundType.LANTERN));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(HAS_LID, true)
                .setValue(OIL_COUNT, MAX_OIL_COUNT));
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(state, direction, neighborState, levelAccessor, pos, neighborPos);
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
        ItemStack mainHandItem = player.getMainHandItem();
        // 先判断棍子敲
        if (mainHandItem.is(Items.STICK)) {
            float pitch = 0.6F + (float) Math.random() * 0.2F;
            level.playSound(player, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS, 2, pitch);
            return ItemInteractionResult.SUCCESS;
        }
        // 再判断开盖
        boolean hasLid = state.getValue(HAS_LID);
        if (hasLid) {
            level.playSound(player, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS, 0.8f, 0.8f);
            level.setBlockAndUpdate(pos, state.setValue(HAS_LID, false));
            return ItemInteractionResult.SUCCESS;
        }
        // 没有盖子，并且是空手，那么盖上盖子
        if (mainHandItem.isEmpty()) {
            level.playSound(player, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS, 0.8f, 0.4f);
            level.setBlockAndUpdate(pos, state.setValue(HAS_LID, true));
            return ItemInteractionResult.SUCCESS;
        }
        // 手持油脂时，消耗油脂添加进去
        if (mainHandItem.is(ModItems.OIL)) {
            int value = state.getValue(OIL_COUNT);
            // 如果油已经满了，不能再放油
            if (value >= MAX_OIL_COUNT) {
                return ItemInteractionResult.FAIL;
            }
            // 尝试直接放满
            int needCount = MAX_OIL_COUNT - value;
            int consumeCount = Math.min(needCount, mainHandItem.getCount());
            level.playSound(player, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS, 0.8f, 0.8f);
            mainHandItem.shrink(consumeCount);
            level.setBlockAndUpdate(pos, state.setValue(OIL_COUNT, value + consumeCount));
            return ItemInteractionResult.SUCCESS;
        }
        // 当用铲子右击时
        if (mainHandItem.is(ModItems.KITCHEN_SHOVEL)) {
            return onShovelClick(state, level, pos, player, mainHandItem);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @NotNull
    private ItemInteractionResult onShovelClick(BlockState state, Level level, BlockPos pos, Player player, ItemStack mainHandItem) {
        int value = state.getValue(OIL_COUNT);
        boolean shovelHasOil = KitchenShovelItem.hasOil(mainHandItem);

        // 如果铲子有油，能还回去
        if (shovelHasOil) {
            // 如果油已经满了，不能再放油
            if (value >= MAX_OIL_COUNT) {
                return ItemInteractionResult.FAIL;
            }
            level.playSound(player, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS, 0.8f, 0.8f);
            KitchenShovelItem.setHasOil(mainHandItem, false);
            level.setBlockAndUpdate(pos, state.setValue(OIL_COUNT, value + 1));
            return ItemInteractionResult.SUCCESS;
        }

        // 没有油时，取出或者破坏
        if (value == 0) {
            level.destroyBlock(pos, true, player);
            return ItemInteractionResult.SUCCESS;
        }

        // 取油
        level.playSound(player, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS, 0.8f, 1.2F);
        KitchenShovelItem.setHasOil(mainHandItem, true);
        level.setBlockAndUpdate(pos, state.setValue(OIL_COUNT, value - 1));
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, HAS_LID, OIL_COUNT);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return state.getValue(HAS_LID) ? AABB : AABB_NO_LID;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(OIL_COUNT);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.enamel_basin").withStyle(ChatFormatting.GRAY));
    }
}
