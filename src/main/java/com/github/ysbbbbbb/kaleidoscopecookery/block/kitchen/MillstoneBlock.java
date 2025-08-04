package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IMillstone;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MillstoneBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final EnumProperty<NinePart> PART = EnumProperty.create("part", NinePart.class);

    private static final VoxelShape CENTER = Block.box(-2, 0, -2, 18, 15, 18);

    private static final VoxelShape LEFT_UP = Shapes.or(
            Block.box(11, 0, 11, 16, 6, 16),
            Block.box(8, 6, 8, 16, 14, 16));

    private static final VoxelShape UP = Shapes.or(
            Block.box(0, 0, 11, 16, 6, 16),
            Block.box(0, 6, 8, 16, 14, 16));

    private static final VoxelShape RIGHT_UP = Shapes.or(
            Block.box(0, 0, 11, 5, 6, 16),
            Block.box(0, 6, 8, 8, 14, 16));

    private static final VoxelShape LEFT_CENTER = Shapes.or(
            Block.box(11, 0, 0, 16, 6, 16),
            Block.box(8, 6, 0, 16, 14, 16));

    private static final VoxelShape RIGHT_CENTER = Shapes.or(
            Block.box(0, 0, 0, 5, 6, 16),
            Block.box(0, 6, 0, 8, 14, 16));

    private static final VoxelShape LEFT_DOWN = Shapes.or(
            Block.box(11, 0, 0, 16, 6, 5),
            Block.box(8, 6, 0, 16, 14, 8));

    private static final VoxelShape DOWN = Shapes.or(
            Block.box(0, 0, 0, 16, 6, 5),
            Block.box(0, 6, 0, 16, 14, 8));

    private static final VoxelShape RIGHT_DOWN = Shapes.or(
            Block.box(0, 0, 0, 5, 6, 5),
            Block.box(0, 6, 0, 8, 14, 8));

    public MillstoneBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .sound(SoundType.WOOD)
                .forceSolidOn()
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PART, NinePart.CENTER)
                .setValue(FACING, Direction.NORTH));
    }

    private static void handleRemove(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide) {
            return;
        }
        NinePart part = state.getValue(PART);
        BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
        BlockEntity te = world.getBlockEntity(centerPos);
        if (!(te instanceof MillstoneBlockEntity)) {
            return;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                world.setBlockAndUpdate(centerPos.offset(i, 0, j), Blocks.AIR.defaultBlockState());
            }
        }
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
            return null;
        }
        return createTickerHelper(blockEntityType, ModBlocks.MILLSTONE_BE.get(),
                (levelIn, pos, stateIn, millstone) -> millstone.tick(levelIn));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        NinePart part = state.getValue(PART);
        BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
        BlockEntity te = level.getBlockEntity(centerPos);
        if (!(te instanceof IMillstone millstone)) {
            return InteractionResult.PASS;
        }
        ItemStack mainHandItem = player.getMainHandItem();
        if (millstone.onTakeItem(player, mainHandItem)) {
            return InteractionResult.SUCCESS;
        }
        if (millstone.onPutItem(level, mainHandItem)) {
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        // 每 5 tick 检查一次
        if (pEntity instanceof Mob mob && !pLevel.isClientSide && pLevel.getGameTime() % 5 == 4) {
            NinePart part = pState.getValue(PART);
            BlockPos centerPos = pPos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
            BlockEntity blockEntity = pLevel.getBlockEntity(centerPos);
            if (blockEntity instanceof MillstoneBlockEntity millstone && !millstone.hasEntity() && millstone.canBindEntity(mob)) {
                millstone.bindEntity(mob);
            }
        }
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        handleRemove(world, pos, state);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        handleRemove(world, pos, state);
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos centerPos = context.getClickedPos();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos searchPos = centerPos.offset(i, 0, j);
                if (!context.getLevel().getBlockState(searchPos).canBeReplaced(context)) {
                    return null;
                }
            }
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.isClientSide) {
            return;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos searchPos = pos.offset(i, 0, j);
                NinePart part = NinePart.getPartByPos(i, j);
                if (part != null && !part.isCenter()) {
                    worldIn.setBlock(searchPos, state.setValue(PART, part), Block.UPDATE_ALL);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(PART, FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART).isCenter()) {
            return new MillstoneBlockEntity(pos, state);
        }
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        NinePart value = pState.getValue(PART);
        return switch (value) {
            case LEFT_UP -> LEFT_UP;
            case UP -> UP;
            case RIGHT_UP -> RIGHT_UP;
            case LEFT_CENTER -> LEFT_CENTER;
            case CENTER -> CENTER;
            case RIGHT_CENTER -> RIGHT_CENTER;
            case LEFT_DOWN -> LEFT_DOWN;
            case DOWN -> DOWN;
            case RIGHT_DOWN -> RIGHT_DOWN;
        };
    }
}
