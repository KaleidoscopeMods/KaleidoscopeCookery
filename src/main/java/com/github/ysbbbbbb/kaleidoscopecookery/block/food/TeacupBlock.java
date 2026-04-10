package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.api.block.ITeacup;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teafluid.ITeaFluid;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teafluid.DrinkTeaFluid;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeaBlockItem;
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
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class TeacupBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, ITeacup {
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

    @Override
    public boolean tryIncreaseCount(Level level, BlockPos pos, BlockState state, ItemStack stack, boolean simulate) {
        int count = state.getValue(this.countProperty);
        if (count >= this.maxCount) {
            return false;
        }

        if (stack.is(teacupItem.get())) {
            if (!simulate) {
                level.setBlockAndUpdate(pos, state.cycle(this.countProperty));
            }
            return true;
        }

        if (stack.getItem() instanceof TeaBlockItem teaItem) {
            if (!simulate) {
                transformToTea(level, pos, state.cycle(this.countProperty), (TeaBlock) teaItem.getBlock(), 1);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean tryPourTeaOn(Level level, BlockPos pos, BlockState state, ITeaFluid teaFluid, boolean simulate) {
        if (!(teaFluid instanceof DrinkTeaFluid type)) {
            return false;
        }

        if (!simulate) {
            TeaBlock drink = type.getBlock();
            transformToTea(level, pos, state, drink, 1);
        }
        return true;
    }

    /**
     * 根据当前的方块状态转为对应的茶水方块状态
     *
     * @param level  所在世界
     * @param pos    位置
     * @param state  方块状态
     * @param tea    目标茶水方块
     * @param filled 转换后装满的杯数
     */
    public void transformToTea(Level level, BlockPos pos, BlockState state, TeaBlock tea, int filled) {
        if (!(state.getBlock() instanceof TeacupBlock teacup)) {
            return;
        }

        // 根据当前方块状态的信息生成对应的茶水方块状态
        int count = Math.min(state.getValue(teacup.getCountProperty()), tea.getMaxCount());
        BlockState newState = tea.defaultBlockState()
                .setValue(TeaBlock.FACING, state.getValue(FACING))
                .setValue(TeaBlock.WATERLOGGED, state.getValue(WATERLOGGED))
                .setValue(tea.getCountProperty(), count)
                .setValue(tea.getFilledCountProperty(), Math.min(filled, count));

        level.setBlockAndUpdate(pos, newState);
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

    protected void createCountBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, countProperty, WATERLOGGED);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> stacks = new ArrayList<>();
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
