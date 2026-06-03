package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class PlateBlock extends HorizontalDirectionalBlock {
    public static final VoxelShape AABB = Block.box(1, 0, 1, 15, 2, 15);

    protected final IntegerProperty servings;
    protected final List<Supplier<Item>> items;
    protected final int maxCount;
    protected VoxelShape aabb = AABB;

    public PlateBlock(int maxCount, List<Supplier<Item>> items) {
        super(BlockBehaviour.Properties.of()
                .forceSolidOn()
                .instabreak()
                .mapColor(MapColor.WOOD)
                .sound(SoundType.WOOD)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion());

        this.servings = IntegerProperty.create("servings", 0, maxCount);
        this.maxCount = maxCount;
        this.items = items;

        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        this.createServingBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.SOUTH)
                .setValue(servings, maxCount));
    }

    public PlateBlock setAABB(VoxelShape aabb) {
        this.aabb = aabb;
        return this;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public IntegerProperty getServingsProperty() {
        return servings;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        ItemStack itemInHand = player.getItemInHand(hand);
        int count = state.getValue(servings);

        // 尝试放回物品
        if (!itemInHand.isEmpty()) {
            if (count < maxCount && canRefill(itemInHand)) {
                itemInHand.shrink(1);
                level.setBlockAndUpdate(pos, state.cycle(servings));
                return InteractionResult.SUCCESS;
            }
        }

        // 尝试取出物品
        if (count > 0) {
            List<ItemStack> stacks = items.stream()
                    .map(s -> s.get().getDefaultInstance()).toList();
            if (itemInHand.isEmpty()) {
                stacks.forEach(s -> ItemHandlerHelper.giveItemToPlayer(player, s));
            } else {
                stacks.forEach(s -> Block.popResource(level, pos, s));
            }
            level.playSound(player, pos, this.soundType.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlockAndUpdate(pos, state.setValue(servings, count - 1));
        } else {
            level.destroyBlock(pos, true, player);
        }

        return InteractionResult.SUCCESS;
    }

    private boolean canRefill(ItemStack itemStack) {
        if (items.size() == 1) {
            return itemStack.is(items.get(0).get());
        }
        return false;
    }

    protected void createServingBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, servings);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.aabb;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter blockGetter, BlockPos pos, PathComputationType pathType) {
        return false;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        int count = state.getValue(servings);
        if (count > 0) {
            List<ItemStack> stacks = items.stream()
                    .map(s -> s.get().getDefaultInstance().copyWithCount(count)).toList();
            drops.addAll(stacks);
        }
        return drops;
    }
}
