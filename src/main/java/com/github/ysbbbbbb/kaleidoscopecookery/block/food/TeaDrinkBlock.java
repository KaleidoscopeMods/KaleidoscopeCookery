package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TeaDrinkBlock extends TeacupBlock {
    protected final IntegerProperty filledCountProperty;
    protected final ResourceLocation teaTypeId;
    protected final Supplier<Item> teaDrinkItem;

    public TeaDrinkBlock(Properties properties, ResourceLocation teaTypeId, int maxCount,
                         Supplier<Item> teapotItem, Supplier<Item> teaDrinkItem, VoxelShape... shapes) {
        super(properties, maxCount, teapotItem, shapes);
        this.teaTypeId = teaTypeId;
        this.teaDrinkItem = teaDrinkItem;
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

    public TeaDrinkBlock(ResourceLocation teaTypeId, int maxCount, Supplier<Item> teapotItem,
                         Supplier<Item> teaDrinkItem, VoxelShape... shapes) {
        this(Properties.of()
                .noOcclusion()
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.BAMBOO),
                teaTypeId, maxCount, teapotItem, teaDrinkItem, shapes);
    }

    protected void createFilledCountBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, countProperty, filledCountProperty, WATERLOGGED);
    }

    @Override
    public boolean tryIncreaseCount(Level level, BlockPos pos, BlockState state, ItemStack stack, boolean simulate) {
        if (!stack.is(teacupItem.get()) && !stack.is(teaDrinkItem.get())) {
            return false;
        }

        int count = state.getValue(this.countProperty);
        if (count < this.maxCount) {
            if (!simulate) {
                BlockState newState = state.cycle(this.countProperty);
                if (stack.is(this.asItem())) {
                    newState = newState.cycle(this.filledCountProperty);
                }
                level.setBlockAndUpdate(pos, newState);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean tryPourTeaOn(Level level, BlockPos pos, BlockState state, ITeaType teaType, boolean simulate) {
        if (teaType.getName().equals(this.teaTypeId)) {
            if (state.getValue(filledCountProperty) < state.getValue(countProperty)) {
                if (!simulate) {
                    level.setBlockAndUpdate(pos, state.cycle(filledCountProperty));
                }
                return true;
            }
        }

        return super.tryPourTeaOn(level, pos, state, teaType, simulate);
    }

    @Override
    public boolean isAllEmpty(BlockState state) {
        return state.getValue(filledCountProperty) == 0;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        // 如果是空手，那么可以尝试取回
        if (!player.getItemInHand(hand).isEmpty()) {
            return super.use(state, level, pos, player, hand, hitResult);
        }

        int count = state.getValue(this.countProperty);
        int filled = state.getValue(this.filledCountProperty);
        // 尝试给玩家物品
        if (filled > 0) {
            ItemHandlerHelper.giveItemToPlayer(player, this.teaDrinkItem.get().getDefaultInstance());
        } else {
            ItemHandlerHelper.giveItemToPlayer(player, this.teacupItem.get().getDefaultInstance());
        }
        // 播放放置的音效
        level.playSound(null, pos, SoundEvents.BAMBOO_PLACE, SoundSource.BLOCKS);

        if (count > 1) {
            // 如果数量大于 1，那么就减少数量
            BlockState newState = state
                    .setValue(this.countProperty, count - 1)
                    .setValue(this.filledCountProperty, Math.max(0, filled - 1));
            level.setBlockAndUpdate(pos, newState);
        } else {
            // 否则就直接破坏
            level.removeBlock(pos, false);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> stacks = new ArrayList<>();
        int count = state.getValue(countProperty);
        int filled = state.getValue(filledCountProperty);
        stacks.add(teaDrinkItem.get().getDefaultInstance().copyWithCount(filled));
        if (count - filled > 0) {
            stacks.add(teacupItem.get().getDefaultInstance().copyWithCount(count - filled));
        }
        return stacks;
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
        private Supplier<Item> teacupItem;
        private Supplier<Item> teaDrinkItem;

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

        public Builder teacupItem(Supplier<Item> teacupItem) {
            this.teacupItem = teacupItem;
            return this;
        }

        public Builder teaDrinkItem(Supplier<Item> teaDrinkItem) {
            this.teaDrinkItem = teaDrinkItem;
            return this;
        }

        public Supplier<? extends Block> build() {
            return () -> new TeaDrinkBlock(teaTypeId, maxCount, teacupItem, teaDrinkItem, shapes);
        }

        public Supplier<? extends Block> build(Properties properties) {
            return () -> new TeaDrinkBlock(properties, teaTypeId, maxCount, teacupItem, teaDrinkItem, shapes);
        }
    }
}
