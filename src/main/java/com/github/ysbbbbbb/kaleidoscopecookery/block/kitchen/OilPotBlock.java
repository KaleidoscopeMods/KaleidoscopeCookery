package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.OilPotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OilPotBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
    public static final MapCodec<OilPotBlock> CODEC = simpleCodec(p -> new OilPotBlock());
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty HAS_OIL = BooleanProperty.create("has_oil");

    private static final VoxelShape AABB = Block.box(5, 0, 5, 11, 10, 11);

    public OilPotBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .instrument(NoteBlockInstrument.BELL)
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.LANTERN));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(HAS_OIL, false)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
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
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof OilPotBlockEntity be && stack.getItem() instanceof OilPotItem) {
            int oilCount = OilPotItem.getOilCount(stack);
            be.setOilCount(oilCount);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        BlockEntity te = level.getBlockEntity(pos);
        if (!(te instanceof OilPotBlockEntity oilPot)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        ItemStack mainHandItem = player.getMainHandItem();

        // 如果是空手，那么取出油
        if (mainHandItem.isEmpty()) {
            int currentOilCount = oilPot.getOilCount();
            if (currentOilCount <= 0) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            int needOilCount = Math.min(currentOilCount, 64);
            ItemStack oilStack = new ItemStack(ModItems.OIL.get(), needOilCount);
            player.setItemInHand(hand, oilStack);
            oilPot.setOilCount(currentOilCount - needOilCount);
            return ItemInteractionResult.SUCCESS;
        }

        // 如果是油，那么添加油
        if (mainHandItem.is(ModItems.OIL.get())) {
            int currentOilCount = oilPot.getOilCount();
            int needOilCount = OilPotBlockEntity.MAX_OIL_COUNT - currentOilCount;
            if (needOilCount <= 0) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            int addOilCount = Math.min(needOilCount, mainHandItem.getCount());
            oilPot.setOilCount(currentOilCount + addOilCount);
            if (!player.isCreative()) {
                mainHandItem.shrink(addOilCount);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING, HAS_OIL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return AABB;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof OilPotBlockEntity be) {
            double signal = (double) be.getOilCount() / (double) OilPotBlockEntity.MAX_OIL_COUNT;
            int baseSignal = be.getOilCount() > 0 ? 1 : 0;
            return Mth.floor(signal * 14.0) + baseSignal;
        }
        return 0;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        if (level.getBlockEntity(pos) instanceof OilPotBlockEntity be) {
            int oilCount = be.getOilCount();
            OilPotItem.setOilCount(stack, oilCount);
            return stack;
        }
        return stack;
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        List<ItemStack> stacks = super.getDrops(pState, pParams);
        BlockEntity blockEntity = pParams.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof OilPotBlockEntity oilPot)) {
            return stacks;
        }
        stacks.forEach(s -> {
            if (s.is(ModItems.OIL_POT.get())) {
                int oilCount = oilPot.getOilCount();
                OilPotItem.setOilCount(s, oilCount);
            }
        });
        return stacks;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new OilPotBlockEntity(pPos, pState);
    }
}
