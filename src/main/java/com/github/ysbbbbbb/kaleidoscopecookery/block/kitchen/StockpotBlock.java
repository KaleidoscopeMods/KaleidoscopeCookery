package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IStockpot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoundType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"deprecation", "unchecked"})
public class StockpotBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty HAS_LID = BooleanProperty.create("has_lid");
    public static final BooleanProperty HAS_BASE = BooleanProperty.create("has_base");
    public static final BooleanProperty HAS_CHAINS = BooleanProperty.create("has_chains");

    private static final VoxelShape AABB = Shapes.or(
            Block.box(2, 0, 2, 14, 5, 14),
            Block.box(1, 5, 1, 15, 7, 15));
    private static final VoxelShape AABB_WITH_LID = Shapes.or(
            Block.box(2, 0, 2, 14, 9, 14),
            Block.box(1, 5, 1, 15, 7, 15));

    public StockpotBlock() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .sound(ModSoundType.POT).noOcclusion()
                .strength(1.5F, 6.0F));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.SOUTH)
                .setValue(WATERLOGGED, false)
                .setValue(HAS_LID, false)
                .setValue(HAS_BASE, false)
                .setValue(HAS_CHAINS, false));
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer instanceof Player player && level.getBlockEntity(pos) instanceof IStockpot stockpot && stockpot.hasHeatSource(level)) {
            ModTrigger.EVENT.trigger(player, ModEventTriggerType.PLACE_STOCKPOT_ON_HEAT_SOURCE);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }

        // 上方无法支撑，取消锁链
        // 下方无法支撑，添加基座
        if (direction == Direction.DOWN && !state.getValue(HAS_CHAINS)) {
            return state.setValue(HAS_CHAINS, canSupportCenter(levelAccessor, pos.above(), Direction.DOWN))
                    .setValue(HAS_BASE, !neighborState.isFaceSturdy(levelAccessor, neighborPos, Direction.UP));
        }
        if (direction == Direction.UP && !state.getValue(HAS_BASE)) {
            BlockState belowState = levelAccessor.getBlockState(pos.below());
            return state.setValue(HAS_CHAINS, canSupportCenter(levelAccessor, neighborPos, Direction.DOWN))
                    .setValue(HAS_BASE, !belowState.isFaceSturdy(levelAccessor, pos.below(), Direction.UP));
        }

        return super.updateShape(state, direction, neighborState, levelAccessor, pos, neighborPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof IStockpot stockpot)) {
            return InteractionResult.PASS;
        }
        // 先检查盖子
        ItemStack mainHandItem = player.getMainHandItem();
        if (stockpot.onLitClick(level, player, mainHandItem)) {
            return InteractionResult.SUCCESS;
        }
        // 加入汤底
        if (stockpot.addSoupBase(level, player, mainHandItem)) {
            ModTrigger.EVENT.trigger(player, ModEventTriggerType.PUT_SOUP_BASE_IN_STOCKPOT);
            return InteractionResult.SUCCESS;
        }
        // 取出汤底
        if (stockpot.removeSoupBase(level, player, mainHandItem)) {
            return InteractionResult.SUCCESS;
        }
        // 加入原料
        if (!mainHandItem.isEmpty() && stockpot.addIngredient(level, player, mainHandItem)) {
            return InteractionResult.SUCCESS;
        }
        // 取出原料
        if ((mainHandItem.isEmpty() || mainHandItem.is(TagMod.INGREDIENT_CONTAINER)) && stockpot.removeIngredient(level, player)) {
            return InteractionResult.SUCCESS;
        }
        // 取出成品
        if (stockpot.takeOutProduct(level, player, mainHandItem)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StockpotBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return createTickerHelper(blockEntityType, ModBlocks.STOCKPOT_BE,
                    (lvl, blockPos, blockState, pot) -> pot.clientTick());
        }
        return createTickerHelper(blockEntityType, ModBlocks.STOCKPOT_BE,
                (lvl, blockPos, blockState, pot) -> pot.tick(lvl));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        FluidState fluidState = level.getFluidState(context.getClickedPos());
        Direction clickFace = context.getClickedFace();
        BlockState blockState = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);

        // 如果点击的是上方，那么依据是否是可支持方块添加锁链
        BlockPos abovePos = context.getClickedPos().above();
        if (clickFace == Direction.DOWN && canSupportCenter(level, abovePos, Direction.DOWN)) {
            return blockState.setValue(HAS_CHAINS, true);
        }

        // 如果下方是不完整方块，则添加基座
        BlockPos belowPos = context.getClickedPos().below();
        BlockState belowState = level.getBlockState(belowPos);
        if (!belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
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
        builder.add(FACING, WATERLOGGED, HAS_LID, HAS_BASE, HAS_CHAINS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        if (state.getValue(HAS_LID)) {
            return AABB_WITH_LID;
        }
        return AABB;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParamsBuilder) {
        List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
        if (state.getValue(HAS_LID)) {
            BlockEntity parameter = lootParamsBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (parameter instanceof StockpotBlockEntity stockpot && !stockpot.getLidItem().isEmpty()) {
                drops.add(stockpot.getLidItem().copy());
            } else {
                drops.add(new ItemStack(ModItems.STOCKPOT_LID));
            }
        }
        BlockEntity parameter = lootParamsBuilder.getParameter(LootContextParams.BLOCK_ENTITY);
        if (parameter instanceof StockpotBlockEntity stockpotBlock && stockpotBlock.getStatus() == StockpotBlockEntity.PUT_INGREDIENT) {
            stockpotBlock.getInputs().forEach(stack -> {
                if (!stack.isEmpty()) {
                    drops.add(stack);
                }
            });
        }
        return drops;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.stockpot").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.stockpot.fail").withStyle(ChatFormatting.GRAY));
    }
}
