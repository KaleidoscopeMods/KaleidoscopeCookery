package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teafluid.ITeaFluid;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeaBlockItem;
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

public class TeaBlock extends TeacupBlock {
    protected final IntegerProperty filledCountProperty;
    protected final ResourceLocation teaFluidId;

    public TeaBlock(Properties properties, ResourceLocation teaFluidId, int maxCount,
                    Supplier<Item> teacupItem, VoxelShape... shapes) {
        super(properties, maxCount, teacupItem, shapes);
        this.teaFluidId = teaFluidId;
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

    public TeaBlock(ResourceLocation teaFluidId, int maxCount, Supplier<Item> teacupItem, VoxelShape... shapes) {
        this(Properties.of()
                .noOcclusion()
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.BAMBOO),
                teaFluidId, maxCount, teacupItem, shapes);
    }

    protected void createFilledCountBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, countProperty, filledCountProperty, WATERLOGGED);
    }

    @Override
    public boolean tryIncreaseCount(Level level, BlockPos pos, BlockState state, ItemStack stack, boolean simulate) {
        int count = state.getValue(this.countProperty);
        int filled = state.getValue(this.filledCountProperty);
        if (count >= this.maxCount) {
            return false;
        }

        if (stack.is(teacupItem.get())) {
            if (!simulate) {
                level.setBlockAndUpdate(pos, state.cycle(this.countProperty));
            }
            return true;
        }

        if (filled == 0 && stack.getItem() instanceof TeaBlockItem blockItem) {
            if (!simulate) {
                transformToTea(level, pos, state.cycle(this.countProperty), (TeaBlock) blockItem.getBlock(), 1);
            }
            return true;
        }

        if (stack.is(this.asItem())) {
            if (!simulate) {
                level.setBlockAndUpdate(pos, state.cycle(this.countProperty).cycle(this.filledCountProperty));
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean tryPourTeaOn(Level level, BlockPos pos, BlockState state, ITeaFluid teaFluid, boolean simulate) {
        int filled = state.getValue(filledCountProperty);
        if (teaFluid.getName().equals(this.teaFluidId) && filled < state.getValue(countProperty)) {
            if (!simulate) {
                level.setBlockAndUpdate(pos, state.cycle(filledCountProperty));
            }
            return true;
        }

        return filled == 0 && super.tryPourTeaOn(level, pos, state, teaFluid, simulate);
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
            ItemHandlerHelper.giveItemToPlayer(player, this.asItem().getDefaultInstance());
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
        stacks.add(this.asItem().getDefaultInstance().copyWithCount(filled));
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
        private ResourceLocation teaFluidId;
        private int maxCount;
        private VoxelShape[] shapes;
        private Supplier<Item> teacupItem;

        public Builder teaFluidId(ResourceLocation teaFluidId) {
            this.teaFluidId = teaFluidId;
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

        public Supplier<? extends Block> build() {
            return () -> new TeaBlock(teaFluidId, maxCount, teacupItem, shapes);
        }

        public Supplier<? extends Block> build(Properties properties) {
            return () -> new TeaBlock(properties, teaFluidId, maxCount, teacupItem, shapes);
        }
    }
}
