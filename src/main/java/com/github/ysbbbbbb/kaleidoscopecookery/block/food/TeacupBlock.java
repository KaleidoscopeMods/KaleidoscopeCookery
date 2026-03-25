package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype.DrinkTeaType;
import com.github.ysbbbbbb.kaleidoscopecookery.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class TeacupBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected final IntegerProperty countProperty;
    protected final int maxCount;
    protected final EnumMap<Direction, VoxelShape>[] shapes;
    protected final Supplier<Item> teacupItem;

    @SuppressWarnings("unchecked")
    public TeacupBlock(Properties properties, int maxCount, Supplier<Item> teacupItem, VoxelShape... shapes) {
        super(properties);
        this.maxCount = maxCount;
        this.teacupItem = teacupItem;
        this.countProperty = IntegerProperty.create("count", 1, maxCount);
        this.shapes = new EnumMap[shapes.length];
        for (int i = 0; i < shapes.length; i++) {
            this.shapes[i] = VoxelShapeUtils.horizontalShapes(shapes[i]);
        }

        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        this.createCountBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(countProperty, 1)
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    public TeacupBlock(int maxCount, Supplier<Item> teacupItem, VoxelShape... shapes) {
        this(Properties.of()
                .noOcclusion()
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.BAMBOO), maxCount, teacupItem, shapes);
    }

    public boolean tryIncreaseCount(Level level, BlockPos pos, BlockState state, ItemStack stack) {
        if (!stack.is(teacupItem.get())) {
            return false;
        }

        int count = state.getValue(this.countProperty);
        if (count < this.maxCount) {
            level.setBlockAndUpdate(pos, state.cycle(this.countProperty));
            return true;
        }
        return false;
    }

    public boolean tryPourTea(Level level, BlockPos pos, BlockState state, ITeaType teaType) {
        if (!(teaType instanceof DrinkTeaType type)) {
            return false;
        }

        TeaDrinkBlock drink = type.getBlock();
        transformToDrink(level, pos, state, drink);
        level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(drink.getFilledCountProperty(), 1));
        return true;
    }

    /**
     * 根据当前的空茶杯BlockState转为对应的茶水BlockState
     *
     * @param state 茶杯BlockState
     * @param drink 目标茶水方块
     */
    public void transformToDrink(Level level, BlockPos pos, BlockState state, TeaDrinkBlock drink) {
        if (!(state.getBlock() instanceof TeacupBlock teacup)) {
            return;
        }

        // 根据空茶杯BlockState的信息生成对应的茶水BlockState
        BlockState newState = drink.defaultBlockState()
                .setValue(TeaDrinkBlock.FACING, state.getValue(FACING))
                .setValue(TeaDrinkBlock.WATERLOGGED, state.getValue(WATERLOGGED))
                .setValue(drink.getCountProperty(), Math.min(state.getValue(teacup.getCountProperty()), drink.getMaxCount()))
                .setValue(drink.getFilledCountProperty(), 0);

        level.setBlockAndUpdate(pos, newState);
    }

    public boolean isAllEmpty(BlockState state) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        // 如果是空手，那么可以尝试取回
        if (!player.getItemInHand(hand).isEmpty()) {
            return super.use(state, level, pos, player, hand, hitResult);
        }

        // 尝试给玩家物品
        ItemHandlerHelper.giveItemToPlayer(player, this.teacupItem.get().getDefaultInstance());
        // 播放放置的音效
        level.playSound(null, pos, SoundEvents.BAMBOO_PLACE, SoundSource.BLOCKS);

        int count = state.getValue(this.countProperty);
        if (count > 1) {
            // 如果数量大于 1，那么就减少数量
            level.setBlockAndUpdate(pos, state.setValue(this.countProperty, count - 1));
        } else {
            // 否则就直接破坏
            level.removeBlock(pos, false);
        }
        return InteractionResult.SUCCESS;
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    protected void createCountBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, countProperty, WATERLOGGED);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> stacks = super.getDrops(state, params);
        stacks.add(teacupItem.get().getDefaultInstance().copyWithCount(state.getValue(countProperty)));
        return stacks;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (this.shapes.length == 0) {
            return super.getShape(state, level, pos, context);
        }
        int count = state.getValue(this.countProperty);
        if (count > this.shapes.length) {
            count = this.shapes.length;
        }
        Direction direction = state.getValue(FACING);
        return this.shapes[count - 1].getOrDefault(direction, super.getShape(state, level, pos, context));
    }

    public IntegerProperty getCountProperty() {
        return countProperty;
    }

    public int getMaxCount() {
        return maxCount;
    }
}
