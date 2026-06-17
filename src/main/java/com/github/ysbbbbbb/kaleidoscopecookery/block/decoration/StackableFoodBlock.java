package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.util.VoxelShapeUtils;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class StackableFoodBlock extends HorizontalDirectionalBlock {
    protected final IntegerProperty countProperty;
    protected final int maxCount;
    protected final Supplier<Item> item;
    protected final EnumMap<Direction, VoxelShape>[] shapes;

    @SuppressWarnings("unchecked")
    public StackableFoodBlock(Properties properties, int maxCount, Supplier<Item> item, VoxelShape... shapes) {
        super(properties);
        this.maxCount = maxCount;
        this.countProperty = IntegerProperty.create("count", 1, maxCount);
        this.item = item;
        this.shapes = new EnumMap[shapes.length];
        for (int i = 0; i < shapes.length; i++) {
            this.shapes[i] = VoxelShapeUtils.horizontalShapes(shapes[i]);
        }

        // 重置一遍 BlockState，因为在父类 Block 中已经创建了一个默认的 BlockStateDefinition
        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        this.createCountBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(countProperty, 1)
                .setValue(FACING, Direction.NORTH));
    }

    public IntegerProperty getCountProperty() {
        return countProperty;
    }

    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        ItemStack itemInHand = player.getItemInHand(hand);

        if (itemInHand.is(this.item.get())) {
            int count = state.getValue(this.countProperty);
            if (count < this.maxCount) {
                level.setBlockAndUpdate(pos, state.cycle(this.countProperty));
                SoundType soundType = state.getSoundType(level, pos, player);
                SoundEvent sound = soundType.getPlaceSound();
                level.playSound(
                        player, pos, sound, SoundSource.BLOCKS,
                        (soundType.getVolume() + 1) / 2f,
                        soundType.getPitch() * 0.8f
                );
                if (!player.isCreative()) {
                    itemInHand.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }

        if (itemInHand.isEmpty()) {
            int count = state.getValue(this.countProperty);
            if (count > 1) {
                // 如果数量大于 1，那么就减少数量
                level.setBlockAndUpdate(pos, state.setValue(this.countProperty, count - 1));
            } else {
                // 否则就直接破坏
                level.removeBlock(pos, false);
            }
            ItemHandlerHelper.giveItemToPlayer(player, item.get().getDefaultInstance());
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = Lists.newArrayList();
        int count = state.getValue(this.countProperty);
        drops.add(new ItemStack(item.get(), count));
        return drops;
    }

    protected void createCountBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, countProperty);
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

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private int maxCount;
        private Supplier<Item> item;
        private VoxelShape[] shapes;
        private Properties properties;

        private Builder() {
            this.properties = BlockBehaviour.Properties.of()
                    .forceSolidOn()
                    .instabreak()
                    .mapColor(MapColor.WOOD)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion();
        }

        public static Builder create() { return new Builder(); }

        public Builder maxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder item(Supplier<Item> item) {
            this.item = item;
            return this;
        }

        public Builder shapes(VoxelShape... shapes) {
            this.shapes = shapes;
            return this;
        }

        public Builder soundType(SoundType soundType) {
            this.properties = this.properties.sound(soundType);
            return this;
        }

        public Builder mapColor(MapColor mapColor) {
            this.properties = this.properties.mapColor(mapColor);
            return this;
        }

        public Supplier<Block> build() {
            return () -> new StackableFoodBlock(properties, maxCount, item, shapes);
        }
    }
}
