package com.github.ysbbbbbb.kaleidoscopecookery.block.misc;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.RecipeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecipeBlock extends FaceAttachedHorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape CEILING_AABB_X = Block.box(1.5D, 15.75D, 3.0D, 14.5D, 16.0D, 13.0D);
    private static final VoxelShape CEILING_AABB_Z = Block.box(3.0D, 15.75D, 1.5D, 13.0D, 16.0D, 14.5D);
    private static final VoxelShape FLOOR_AABB_X = Block.box(1.5D, 0.0D, 3.0D, 14.5D, 0.25D, 13.0D);
    private static final VoxelShape FLOOR_AABB_Z = Block.box(3.0D, 0.0D, 1.5D, 13.0D, 0.25D, 14.5D);

    private static final VoxelShape NORTH_AABB = Block.box(3.0D, 1.5D, 15.75D, 13.0D, 14.5D, 16.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(3.0D, 1.5D, 0.0D, 13.0D, 14.5D, 0.25D);
    private static final VoxelShape WEST_AABB = Block.box(15.75D, 1.5D, 3.0D, 16.0D, 14.5D, 13.0D);
    private static final VoxelShape EAST_AABB = Block.box(0.0D, 1.5D, 3.0D, 0.25D, 14.5D, 13.0D);

    public RecipeBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_YELLOW)
                .instabreak()
                .noOcclusion()
                .sound(ModSoundType.RECIPE_BLOCK));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.WALL)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // 空手右击取下来
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        ItemStack mainHandItem = player.getMainHandItem();
        if (!mainHandItem.isEmpty()) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RecipeBlockEntity recipeBlockEntity) {
            ItemStack itemStack = recipeBlockEntity.getItems().getStackInSlot(0);
            if (itemStack.isEmpty()) {
                return InteractionResult.PASS;
            }
            player.setItemInHand(hand, itemStack.copy());
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, pos, ModSoundType.RECIPE_BLOCK.getBreakSound(), player.getSoundSource(), 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(state, direction, neighborState, levelAccessor, pos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = state.getValue(FACING);
        return switch (state.getValue(FACE)) {
            case FLOOR -> facing.getAxis() == Direction.Axis.X ? FLOOR_AABB_X : FLOOR_AABB_Z;
            case WALL -> switch (facing) {
                case EAST -> EAST_AABB;
                case WEST -> WEST_AABB;
                case SOUTH -> SOUTH_AABB;
                case NORTH, UP, DOWN -> NORTH_AABB;
            };
            default -> facing.getAxis() == Direction.Axis.X ? CEILING_AABB_X : CEILING_AABB_Z;
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement != null) {
            return stateForPlacement.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity livingEntity, ItemStack stack) {
        if (!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof RecipeBlockEntity recipeBlockEntity) {
                recipeBlockEntity.getItems().setStackInSlot(0, stack.copyWithCount(1));
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RecipeBlockEntity recipeBlockEntity) {
            ItemStack itemStack = recipeBlockEntity.getItems().getStackInSlot(0);
            if (!itemStack.isEmpty()) {
                return itemStack.copy();
            }
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, FACE, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParamsBuilder) {
        List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
        BlockEntity parameter = lootParamsBuilder.getParameter(LootContextParams.BLOCK_ENTITY);
        if (parameter instanceof RecipeBlockEntity recipeBlock) {
            drops.add(recipeBlock.getItems().getStackInSlot(0).copyWithCount(1));
        }
        return drops;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RecipeBlockEntity(pPos, pState);
    }
}
