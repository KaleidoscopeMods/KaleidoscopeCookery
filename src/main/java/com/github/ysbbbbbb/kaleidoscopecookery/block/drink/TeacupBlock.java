package com.github.ysbbbbbb.kaleidoscopecookery.block.drink;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeacupItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeapotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class TeacupBlock extends HorizontalDirectionalBlock {
    public static final VoxelShape AABB = Block.box(1, 0, 1, 15, 2, 15);

    protected final IntegerProperty cupCount;
    protected final IntegerProperty teaCount;
    protected final int maxCount;

    protected VoxelShape aabb = AABB;

    public TeacupBlock(int maxCount) {
        super(BlockBehaviour.Properties.of()
                .forceSolidOn()
                .instabreak()
                .mapColor(MapColor.WOOD)
                .sound(SoundType.WOOD)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion());

        this.maxCount = maxCount;
        this.cupCount = IntegerProperty.create("cup_count", 1, maxCount);
        this.teaCount = IntegerProperty.create("tea_count", 1, maxCount);

        // 重置一遍 BlockState，因为在父类 FoodBlock 中已经创建了一个默认的 BlockStateDefinition
        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        this.createCountBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(cupCount, 1)
                .setValue(teaCount, 1)
                .setValue(FACING, Direction.SOUTH));
    }

    public TeacupBlock() {
        this(4);
    }

    public TeacupBlock setAABB(VoxelShape aabb) {
        this.aabb = aabb;
        return this;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public IntegerProperty getCupCountProperty() {
        return cupCount;
    }

    public IntegerProperty getTeaCountProperty() {
        return teaCount;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        ItemStack itemInHand = player.getItemInHand(hand);

        // 如果是茶壶
        if (itemInHand.is(ModItems.TEAPOT.get())) {
            ItemStack pourOut = TeapotItem.getPourOut(itemInHand);
            if (pourOut.isEmpty() || pourOut.getItem() != this.asItem()) {
                return InteractionResult.CONSUME;
            }
            // 如果茶杯茶没有满
            int count = state.getValue(teaCount);
            if (count < state.getValue(cupCount)) {
                level.setBlockAndUpdate(pos, state.setValue(teaCount, count + 1));
                level.playSound(player, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
                TeapotItem.pourOut(itemInHand);
                spawnPourParticles(level, pos);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        // 如果是空杯
        if (itemInHand.is(ModItems.EMPTY_CUP.get())) {
            // 如果茶杯数量没满
            int count = state.getValue(cupCount);
            if (count < this.maxCount) {
                level.setBlockAndUpdate(pos, state.setValue(cupCount, count + 1));
                level.playSound(player, pos, this.soundType.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                itemInHand.shrink(1);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        // 如果是对应的物品类型
        if (itemInHand.getItem() instanceof TeacupItem teacupItem) {
            if (teacupItem.getBlock() != this) {
                return InteractionResult.CONSUME;
            }
            // 如果茶杯数量没满
            int cupCountNum = state.getValue(cupCount);
            int teaCountNum = state.getValue(teaCount);
            if (cupCountNum < this.maxCount) {
                level.setBlockAndUpdate(pos, state
                        .setValue(cupCount, cupCountNum + 1)
                        .setValue(teaCount, teaCountNum + 1));
                level.playSound(player, pos, this.soundType.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                itemInHand.shrink(1);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        // 如果是空手，先取下茶杯，空杯
        if (itemInHand.isEmpty()) {
            int cupCountNum = state.getValue(cupCount);
            int teaCountNum = state.getValue(teaCount);
            int emptyCountNum = cupCountNum - teaCountNum;

            // 取下空杯
            if (emptyCountNum > 0) {
                ItemStack cupStack = new ItemStack(ModItems.EMPTY_CUP.get());
                ItemUtils.getItemToLivingEntity(player, cupStack);
                if (cupCountNum == 1) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                } else {
                    level.setBlockAndUpdate(pos, state.setValue(cupCount, cupCountNum - 1));
                }
                level.playSound(player, pos, this.soundType.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

            if (teaCountNum > 0) {
                // 取下茶水
                ItemStack teaStack = new ItemStack(this);
                ItemUtils.getItemToLivingEntity(player, teaStack);
                if (cupCountNum == 1) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                } else {
                    level.setBlockAndUpdate(pos, state
                            .setValue(teaCount, teaCountNum - 1)
                            .setValue(cupCount, cupCountNum - 1));
                    level.playSound(player, pos, this.soundType.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private static void spawnPourParticles(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        RandomSource random = level.random;
        serverLevel.sendParticles(ModParticles.COOKING.get(),
                pos.getX() + 0.5,
                pos.getY() + 0.35,
                pos.getZ() + 0.5,
                4,
                0.12 + random.nextDouble() * 0.04,
                0.08,
                0.12 + random.nextDouble() * 0.04,
                0.02);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(20) != 0) {
            return;
        }
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        level.addParticle(ModParticles.COOKING.get(),
                x + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                y + random.nextDouble() / 3,
                z + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                0.3, 0.1, 0.3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    protected void createCountBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(cupCount, teaCount, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.aabb;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
