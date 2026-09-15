package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TeaBannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeaBannerBlock extends FaceAttachedHorizontalDirectionalBlock implements EntityBlock {
    private static final VoxelShape FLOOR_SHAPE = box(4, 0, 4, 12, 16, 12);

    private static final VoxelShape NORTH_SHAPE = box(0, 7, 4, 16, 15, 12);
    private static final VoxelShape SOUTH_SHAPE = box(0, 7, 4, 16, 15, 12);
    private static final VoxelShape WEST_SHAPE = box(4, 7, 0, 12, 15, 16);
    private static final VoxelShape EAST_SHAPE = box(4, 7, 0, 12, 15, 16);

    public TeaBannerBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .strength(1.0F)
                .noCollission()
                .noOcclusion()
                .sound(SoundType.WOOD));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.FLOOR));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldStack = player.getItemInHand(hand);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof TeaBannerBlockEntity teaBanner)) {
            return InteractionResult.PASS;
        }

        if (heldStack.getItem() instanceof DyeItem dyeItem) {
            if (teaBanner.getColor() == dyeItem.getDyeColor()) {
                return InteractionResult.PASS;
            }
            if (!level.isClientSide) {
                teaBanner.setColor(dyeItem.getDyeColor());
                if (!player.getAbilities().instabuild) {
                    heldStack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (heldStack.getItem() instanceof BannerPatternItem && TeaBannerBlockEntity.isSupportedPattern(heldStack)) {
            if (ItemStack.isSameItemSameTags(heldStack, teaBanner.getPatternItem())) {
                return InteractionResult.PASS;
            }
            if (!level.isClientSide) {
                ItemStack previousPattern = teaBanner.setPatternItem(heldStack.copyWithCount(1));
                if (!player.getAbilities().instabuild) {
                    heldStack.shrink(1);
                }
                if (!previousPattern.isEmpty()) {
                    popResource(level, pos, previousPattern);
                }
                level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (heldStack.is(Items.SHEARS) && teaBanner.hasPattern()) {
            if (!level.isClientSide) {
                popResource(level, pos, teaBanner.removePatternItem());
                heldStack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));
                level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null || state.getValue(FACE) == AttachFace.CEILING) {
            return null;
        }
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(FACE) == AttachFace.FLOOR) {
            return FLOOR_SHAPE;
        }
        Direction shapeFacing = state.getValue(FACING).getClockWise();
        return switch (shapeFacing) {
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TeaBannerBlockEntity teaBanner) {
            return teaBanner.createItemStack();
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParamsBuilder) {
        List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
        BlockEntity blockEntity = lootParamsBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof TeaBannerBlockEntity teaBanner) {
            drops.add(teaBanner.createItemStack());
        }
        return drops;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeaBannerBlockEntity(pos, state);
    }
}
