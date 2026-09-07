package com.github.ysbbbbbb.kaleidoscopecookery.block.crop;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeaTreeBlock extends BushBlock implements BonemealableBlock {
    public static final int MAX_AGE = 5;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(3, 0, 3, 13, 4, 13),
            Block.box(3, 0, 3, 13, 5, 13),
            Block.box(3, 0, 3, 13, 6, 13),
            Block.box(3, 0, 3, 13, 7, 13),
            Block.box(3, 0, 3, 13, 8, 13),
            Block.box(3, 0, 3, 13, 9, 13)
    };

    public TeaTreeBlock() {
        super(Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public int getAge(BlockState state) {
        return state.getValue(AGE);
    }

    public BlockState getStateForAge(int age) {
        return this.defaultBlockState().setValue(AGE, age);
    }

    public boolean isMaxAge(BlockState state) {
        return this.getAge(state) >= MAX_AGE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return this.mayPlaceOn(level.getBlockState(below), level, below);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.TEA_SEED.get());
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !this.isMaxAge(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = this.getAge(state);
        int growthChance = this.isPlantedInRow(level, pos) ? 4 : 5;
        if (age < MAX_AGE
            && level.getRawBrightness(pos.above(), 0) >= 9
            && ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(growthChance) == 0)
        ) {
            BlockState grownState = state.setValue(AGE, age + 1);
            level.setBlock(pos, grownState, Block.UPDATE_CLIENTS);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(grownState));
            ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }

    private boolean isPlantedInRow(LevelReader level, BlockPos pos) {
        boolean hasXNeighbor = level.getBlockState(pos.west()).is(this) || level.getBlockState(pos.east()).is(this);
        boolean hasZNeighbor = level.getBlockState(pos.north()).is(this) || level.getBlockState(pos.south()).is(this);
        return hasXNeighbor != hasZNeighbor;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.getItemInHand(hand).is(ModItems.SICKLE.get())) {
            return InteractionResult.PASS;
        }
        if (!this.isMaxAge(state)) {
            return super.use(state, level, pos, player, hand, hitResult);
        }

        Block.popResource(level, pos, ModItems.FRESH_TEA_LEAVES.get().getDefaultInstance());
        level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        BlockState harvestedState = state.setValue(AGE, 3);
        level.setBlock(pos, harvestedState, Block.UPDATE_CLIENTS);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, harvestedState));
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return !this.isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(AGE, Math.min(MAX_AGE, this.getAge(state) + 1)), Block.UPDATE_CLIENTS);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.crop_seed").withStyle(ChatFormatting.GRAY));
    }
}
