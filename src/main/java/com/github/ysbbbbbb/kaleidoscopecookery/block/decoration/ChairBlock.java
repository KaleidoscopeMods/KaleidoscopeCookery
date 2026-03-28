package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.ChairBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.util.BlockDrop;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopecookery.util.CarpetColor.getCarpetByColor;
import static com.github.ysbbbbbb.kaleidoscopecookery.util.CarpetColor.getColorByCarpet;

public class ChairBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
    public static final BooleanProperty HAS_CARPET = BooleanProperty.create("has_carpet");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape BASE = Block.box(2, 0, 2, 14, 9, 14);
    private static final VoxelShape NORTH = Shapes.or(BASE, Block.box(2, 0, 12, 14, 19, 14));
    private static final VoxelShape SOUTH = Shapes.or(BASE, Block.box(2, 0, 2, 14, 19, 4));
    private static final VoxelShape WEST = Shapes.or(BASE, Block.box(12, 0, 2, 14, 19, 14));
    private static final VoxelShape EAST = Shapes.or(BASE, Block.box(2, 0, 2, 4, 19, 14));

    public ChairBlock() {
        super(Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .noOcclusion()
                .ignitedByLava());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.SOUTH)
                .setValue(HAS_CARPET, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(state, direction, neighborState, levelAccessor, pos, neighborPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND && itemInHand.is(ItemTags.WOOL_CARPETS)) {
            return useWithCarpets(state, level, pos, player, itemInHand);
        } else {
            return tryToSitOn(state, level, pos, player);
        }
    }

    @NotNull
    private InteractionResult tryToSitOn(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            List<SitEntity> entities = level.getEntitiesOfClass(SitEntity.class, new AABB(pos));
            if (entities.isEmpty()) {
                SitEntity entitySit = new SitEntity(level, pos, 0.5125);
                entitySit.setYRot(state.getValue(FACING).toYRot());
                level.addFreshEntity(entitySit);
                player.startRiding(entitySit, true);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        } else {
            // todo-check
            // 不知有无炸弹:(, 但这样能够将抱着的生物（使用 carryOn 模组）放置在方块上，而不是抱着生物坐在凳子上
            return InteractionResult.sidedSuccess(true);
        }
    }

    @NotNull
    private InteractionResult useWithCarpets(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemInHand) {
        @Nullable DyeColor dyeColor = getColorByCarpet(itemInHand.getItem());
        boolean hasCarpet = state.getValue(HAS_CARPET);
        if (dyeColor == null) {
            return InteractionResult.PASS;
        }

        // 第一种情况，椅子上没有地毯
        if (!hasCarpet) {
            level.setBlockAndUpdate(pos, state.setValue(HAS_CARPET, true));
            if (level.getBlockEntity(pos) instanceof ChairBlockEntity chairBlockEntity) {
                level.playSound(null, pos, SoundType.WOOL.getPlaceSound(), player.getSoundSource(), 1.0F, 1.0F);
                chairBlockEntity.setColor(dyeColor);
                chairBlockEntity.refresh();
                itemInHand.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }

        // 第二种情况：有地毯，但是颜色不一致
        if (hasCarpet && level.getBlockEntity(pos) instanceof ChairBlockEntity chairBlockEntity && chairBlockEntity.getColor() != dyeColor) {
            // 掉落原地毯
            DyeColor originalColor = chairBlockEntity.getColor();
            ItemStack carpetItem = getCarpetByColor(originalColor).getDefaultInstance();
            BlockDrop.popResource(level, pos, 0.25, carpetItem);
            level.playSound(null, pos, SoundType.WOOL.getPlaceSound(), player.getSoundSource(), 1.0F, 1.0F);

            chairBlockEntity.setColor(dyeColor);
            chairBlockEntity.refresh();
            level.setBlockAndUpdate(pos, state.setValue(HAS_CARPET, true));
            itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        levelAccessor.getEntitiesOfClass(SitEntity.class, new AABB(pos)).forEach(Entity::discard);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParamsBuilder) {
        List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
        BlockEntity parameter = lootParamsBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (state.getValue(HAS_CARPET) && parameter instanceof ChairBlockEntity chairBlockEntity) {
            Item carpet = getCarpetByColor(chairBlockEntity.getColor());
            drops.add(new ItemStack(carpet));
        }
        return drops;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_CARPET, WATERLOGGED);
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
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> NORTH;
        };
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(HAS_CARPET)) {
            return new ChairBlockEntity(pos, state);
        }
        return null;
    }
}
