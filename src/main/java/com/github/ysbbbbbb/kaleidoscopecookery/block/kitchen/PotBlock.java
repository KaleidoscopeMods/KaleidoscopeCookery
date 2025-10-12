package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IPot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoundType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty HAS_OIL = BooleanProperty.create("has_oil");
    public static final BooleanProperty SHOW_OIL = BooleanProperty.create("show_oil");
    public static final BooleanProperty HAS_BASE = BooleanProperty.create("has_base");

    private static final VoxelShape AABB = Block.box(2, 0, 2, 14, 4, 14);
    private static final double DURABILITY_COST_PROBABILITY = 0.25;

    public PotBlock() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .sound(ModSoundType.POT)
                .noOcclusion()
                .strength(1.5F, 6.0F));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.SOUTH)
                .setValue(HAS_OIL, false)
                .setValue(SHOW_OIL, false)
                .setValue(WATERLOGGED, false)
                .setValue(HAS_BASE, false));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        // 如果下方是不完整方块，则添加基座
        if (direction == Direction.DOWN) {
            return state.setValue(HAS_BASE, !neighborState.isFaceSturdy(levelAccessor, neighborPos, Direction.UP));
        }
        return super.updateShape(state, direction, neighborState, levelAccessor, pos, neighborPos);
    }

    @Nullable
    @SuppressWarnings("all")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer instanceof Player player && level.getBlockEntity(pos) instanceof IPot pot && pot.hasHeatSource(level)) {
            ModTrigger.EVENT.trigger(player, ModEventTriggerType.PLACE_POT_ON_HEAT_SOURCE);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand == InteractionHand.OFF_HAND) {
            return InteractionResult.PASS;
        }
        // 开始执行炒菜逻辑检查
        if (!(level.getBlockEntity(pos) instanceof IPot pot)) {
            return InteractionResult.PASS;
        }
        ItemStack itemInHand = player.getItemInHand(hand);
        RandomSource random = level.random;
        // 先检查执行配菜取出逻辑
        if ((itemInHand.isEmpty() || itemInHand.is(TagMod.INGREDIENT_CONTAINER)) && pot.removeIngredient(level, player)) {
            return InteractionResult.SUCCESS;
        }
        // 再检查成品取出逻辑
        if (pot.takeOutProduct(level, player, player.getMainHandItem())) {
            return InteractionResult.SUCCESS;
        }
        // 然后检查热源
        if (!pot.hasHeatSource(level)) {
            this.sendBarMessage(player, "tip.kaleidoscope_cookery.pot.need_lit_stove");
            return InteractionResult.FAIL;
        }
        // 检查油
        if (!state.getValue(HAS_OIL)) {
            if (pot.onPlaceOil(level, player, itemInHand)) {
                return InteractionResult.SUCCESS;
            } else {
                sendBarMessage(player, "tip.kaleidoscope_cookery.pot.need_oil");
                return InteractionResult.FAIL;
            }
        }
        // 如果拿着锅铲，那么开始执行锅铲逻辑
        if (itemInHand.is(TagMod.KITCHEN_SHOVEL)) {
            if (level.random.nextDouble() < DURABILITY_COST_PROBABILITY) {
                itemInHand.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
            }
            pot.onShovelHit(level, player, itemInHand);
            level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F,
                    1F + (random.nextFloat() - random.nextFloat()) * 0.8F);
            return InteractionResult.SUCCESS;
        }
        // 放入配菜
        if (pot.addIngredient(level, player, itemInHand)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void sendBarMessage(Player player, String key) {
        if (player instanceof ServerPlayer serverPlayer) {
            MutableComponent message = Component.translatable(key);
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
        }
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }
        if (!state.getValue(HAS_OIL)) {
            return null;
        }
        return createTickerHelper(blockEntityType, ModBlocks.POT_BE,
                (levelIn, pos, stateIn, pot) -> pot.tick(levelIn));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState blockState = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        // 如果下方是不完整方块，则添加基座
        BlockState belowState = context.getLevel().getBlockState(context.getClickedPos().below());
        if (!belowState.isFaceSturdy(context.getLevel(), context.getClickedPos().below(), Direction.UP)) {
            return blockState.setValue(HAS_BASE, true);
        }
        return blockState;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_OIL, SHOW_OIL, HAS_BASE, FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return AABB;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.pot").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.pot.fail").withStyle(ChatFormatting.GRAY));
    }
}
