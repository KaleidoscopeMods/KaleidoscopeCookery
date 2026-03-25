package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.food.TeacupBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.function.Supplier;

public class TeaDrinkBlock extends TeacupBlock {
    protected final IntegerProperty filledCountProperty;
    protected final ResourceLocation teaTypeId;

    public TeaDrinkBlock(Properties properties, ResourceLocation teaTypeId, int maxCount, VoxelShape... shapes) {
        super(properties, maxCount, shapes);
        this.teaTypeId = teaTypeId;
        this.filledCountProperty = IntegerProperty.create("filled_count", 0, maxCount);

        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        this.createFilledCountBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(countProperty, 1)
                .setValue(filledCountProperty, 1)
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    public TeaDrinkBlock(ResourceLocation teaTypeId, int maxCount, VoxelShape... shapes) {
        this(Properties.of()
                .noOcclusion()
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.BAMBOO), teaTypeId, maxCount, shapes);
    }

    protected void createFilledCountBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, countProperty, filledCountProperty, WATERLOGGED);
    }

    @Override
    public boolean tryIncreaseCount(Level level, BlockPos pos, BlockState state, ItemStack stack) {
        int count = state.getValue(this.countProperty);
        if (count < this.maxCount) {
            BlockState newState = state.cycle(this.countProperty);
            if (stack.is(this.asItem())) {
                newState = newState.cycle(this.filledCountProperty);
            }
            if (level.getBlockEntity(pos) instanceof TeacupBlockEntity be) {
                if (be.addItem(stack)) {
                    be.refresh();
                }
            }
            level.setBlockAndUpdate(pos, newState);
            return true;
        }
        return false;
    }

    public boolean tryPourTea(Level level, BlockPos pos, BlockState state, ITeaType teaType) {
        if (!teaType.getName().equals(this.teaTypeId)) {
            return false;
        }

        if (level.getBlockEntity(pos) instanceof TeacupBlockEntity be) {
            if (be.replaceLast(s -> s.is(ModItems.TEACUP.get()), this.asItem().getDefaultInstance())) {
                be.refresh();
                level.setBlockAndUpdate(pos, state.cycle(filledCountProperty));
                return true;
            }
        }
        return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        // 如果是空手，那么可以尝试取回
        if (!player.getItemInHand(hand).isEmpty()) {
            return super.use(state, level, pos, player, hand, hitResult);
        }

        // 尝试给玩家物品
        if (level.getBlockEntity(pos) instanceof TeacupBlockEntity be) {
            ItemStack stack = be.removeItem();
            if (!stack.isEmpty()) {
                be.refresh();
                ItemHandlerHelper.giveItemToPlayer(player, stack);
                // 播放放置的音效
                level.playSound(null, pos, SoundEvents.BAMBOO_PLACE, SoundSource.BLOCKS);
            }
        }

        int count = state.getValue(this.countProperty);
        int filled = state.getValue(this.filledCountProperty);
        if (count > 1) {
            // 如果数量大于 1，那么就减少数量
            BlockState newState = state.setValue(this.countProperty, count - 1);
            // 同步茶杯状态
            if (filled > 0) {
                newState = newState.setValue(this.filledCountProperty, filled - 1);
            }
            level.setBlockAndUpdate(pos, newState);
        } else {
            // 否则就直接破坏
            level.removeBlock(pos, false);
        }
        return InteractionResult.SUCCESS;
    }

    public IntegerProperty getFilledCountProperty() {
        return filledCountProperty;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private ResourceLocation teaTypeId;
        private int maxCount;
        private VoxelShape[] shapes;

        public Builder teaTypeId(ResourceLocation teaTypeId) {
            this.teaTypeId = teaTypeId;
            return this;
        }

        public Builder maxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder shapes(VoxelShape... shapes) {
            this.shapes = shapes;
            return this;
        }

        public Supplier<? extends Block> build() {
            return () -> new TeaDrinkBlock(teaTypeId, maxCount, shapes);
        }

        public Supplier<? extends Block> build(Properties properties) {
            return () -> new TeaDrinkBlock(properties, teaTypeId, maxCount, shapes);
        }
    }
}
