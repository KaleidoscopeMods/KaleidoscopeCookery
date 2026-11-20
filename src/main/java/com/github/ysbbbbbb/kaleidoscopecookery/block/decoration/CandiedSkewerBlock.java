package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class CandiedSkewerBlock extends HorizontalDirectionalBlock {
    private static final VoxelShape UPRIGHT_SHAPE_LITTLE = Block.box(0, 0, 0, 16, 20, 16);
    private static final VoxelShape UPRIGHT_SHAPE_HUGE = Block.box(-4, 0, -4, 20, 20, 20);
    private static final VoxelShape SIDE_SHAPE = Block.box(0, 0, 0, 16, 24, 16);

    private final int maxCount;
    private final Supplier<Item> item;
    private final IntegerProperty countProperty;
    private final boolean upright;

    public CandiedSkewerBlock(int maxCount, Supplier<Item> item, int lightLevel, boolean upright) {
        super(BlockBehaviour.Properties.of()
                .forceSolidOn()
                .instabreak()
                .mapColor(MapColor.WOOD)
                .sound(SoundType.WOOD)
                .pushReaction(PushReaction.DESTROY)
                .lightLevel(state -> lightLevel)
                .noCollission());

        this.maxCount = maxCount;
        this.item = item;
        this.countProperty = IntegerProperty.create("count", 1, maxCount);
        this.upright = upright;

        // 重置一遍 BlockState，因为在父类中已经创建了一个默认的 BlockStateDefinition
        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        builder.add(FACING, countProperty);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(countProperty, 1));
    }

    public IntegerProperty getCountProperty() {
        return countProperty;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public Supplier<Item> getItem() {
        return item;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        Item skewer = this.getItem().get();
        ItemStack held = player.getItemInHand(hand);

        // 如果是同一种果串
        if (held.is(skewer)) {
            int count = state.getValue(countProperty);
            if (count >= maxCount) {
                return InteractionResult.PASS;
            }
            // 数量 +1
            level.setBlockAndUpdate(pos, state.setValue(countProperty, count + 1));
            level.playSound(player, pos, this.soundType.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!player.isCreative()) {
                held.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        // 空手那么直接给到玩家背包或者弹出
        if (held.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(skewer));
        } else {
            Block.popResourceFromFace(level, pos, Direction.UP, new ItemStack(skewer));
        }

        // 数量 -1
        int count = state.getValue(countProperty);
        if (count <= 1) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        } else {
            level.setBlockAndUpdate(pos, state.setValue(countProperty, count - 1));
        }
        level.playSound(player, pos, this.soundType.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        // 虽然可以通过数据包指定掉落，但是由于数量过大，还是改为硬编码实现
        Item skewer = this.getItem().get();
        ItemStack drop = new ItemStack(skewer, state.getValue(countProperty));
        return Lists.newArrayList(drop);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (this.upright) {
            int count = state.getValue(countProperty);
            if (count > 4) {
                return UPRIGHT_SHAPE_HUGE;
            }
            return UPRIGHT_SHAPE_LITTLE;
        }
        return SIDE_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
