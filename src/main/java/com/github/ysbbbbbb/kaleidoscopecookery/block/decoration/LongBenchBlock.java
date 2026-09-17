package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class LongBenchBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 3);

    public static final int SINGLE = 0;
    public static final int LEFT = 1;
    public static final int MIDDLE = 2;
    public static final int RIGHT = 3;

    private static final VoxelShape EAST_WEST = Block.box(0, 0, 3, 16, 8, 13);
    private static final VoxelShape NORTH_SOUTH = Block.box(3, 0, 0, 13, 8, 16);

    public LongBenchBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .noOcclusion()
                .ignitedByLava());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AXIS, Direction.Axis.X)
                .setValue(POSITION, SINGLE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, POSITION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis axis = context.getHorizontalDirection().getClockWise().getAxis();
        return updateConnections(context.getLevel(), context.getClickedPos(), this.defaultBlockState().setValue(AXIS, axis));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis() == state.getValue(AXIS)) {
            return updateConnections(level, pos, state);
        }
        return state;
    }

    private BlockState updateConnections(LevelAccessor level, BlockPos pos, BlockState state) {
        Direction.Axis axis = state.getValue(AXIS);
        Direction negative = axis == Direction.Axis.X ? Direction.WEST : Direction.NORTH;
        Direction positive = negative.getOpposite();
        boolean connectsNegative = connectsTo(level.getBlockState(pos.relative(negative)), axis);
        boolean connectsPositive = connectsTo(level.getBlockState(pos.relative(positive)), axis);

        if (connectsNegative && connectsPositive) {
            return state.setValue(POSITION, MIDDLE);
        }
        if (connectsNegative) {
            return state.setValue(POSITION, LEFT);
        }
        if (connectsPositive) {
            return state.setValue(POSITION, RIGHT);
        }
        return state.setValue(POSITION, SINGLE);
    }

    private boolean connectsTo(BlockState state, Direction.Axis axis) {
        return state.is(this) && state.getValue(AXIS) == axis;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            List<SitEntity> entities = level.getEntitiesOfClass(SitEntity.class, new AABB(pos));
            if (entities.isEmpty()) {
                SitEntity sitEntity = new SitEntity(level, pos, 0.5);
                Direction facing = state.getValue(AXIS) == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
                sitEntity.setYRot(facing.toYRot());
                level.addFreshEntity(sitEntity);
                player.startRiding(sitEntity, true);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        return InteractionResult.sidedSuccess(true);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        level.getEntitiesOfClass(SitEntity.class, new AABB(pos)).forEach(Entity::discard);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.X ? EAST_WEST : NORTH_SOUTH;
    }
}
