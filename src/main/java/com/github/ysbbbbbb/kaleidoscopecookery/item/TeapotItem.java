package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype.TeaTypeManager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTeaTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.*;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TeapotItem extends BlockItem {
    private static final String FLUID_AMOUNT = "fluid_amount";
    private static final String TEA_TYPE = "tea_type";

    public TeapotItem() {
        super(ModBlocks.TEAPOT.get(), new Item.Properties());
    }

    public static void setFluidAmount(ItemStack stack, int amount) {
        amount = Mth.clamp(amount, 0, TeapotBlockEntity.MAX_FLUID_AMOUNT);
        stack.getOrCreateTag().putInt(FLUID_AMOUNT, amount);
        if (amount == 0) {
            setTeaType(stack, TeaTypeManager.getTeaType(ModTeaTypes.EMPTY));
        }
    }

    public static int getFluidAmount(ItemStack stack) {
        CompoundTag element = stack.getTag();
        if (element == null || !element.contains(FLUID_AMOUNT)) {
            return 0;
        }
        return element.getInt(FLUID_AMOUNT);
    }

    public static boolean isEmpty(ItemStack stack) {
        return getFluidAmount(stack) == 0;
    }

    public static void shrinkFluidAmount(ItemStack stack, int amount) {
        int currentAmount = getFluidAmount(stack);
        if (currentAmount > 0) {
            setFluidAmount(stack, Math.max(0, currentAmount - amount));
        }
    }

    public static void setTeaType(ItemStack stack, ITeaType teaType) {
        stack.getOrCreateTag().putString(TEA_TYPE, teaType.getName().toString());
    }

    public static ITeaType getTeaType(ItemStack stack) {
        CompoundTag element = stack.getTag();
        if (element == null || !element.contains(TEA_TYPE)) {
            return TeaTypeManager.getTeaType(ModTeaTypes.EMPTY);
        }
        return TeaTypeManager.getTeaType(new ResourceLocation(element.getString(TEA_TYPE)));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 12;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack itemInHand = context.getItemInHand();
        ITeaType teaType = getTeaType(itemInHand);

        // 潜行时只放置方块
        if (player == null || player.isSecondaryUseActive()) {
            return super.useOn(context);
        }

        FluidState fluidState = level.getFluidState(pos);
        FluidState fluidState1 = level.getFluidState(pos.relative(context.getClickedFace()));
        // 尝试装水
        if (tryFillWithFluid(itemInHand, teaType, fluidState, fluidState1)) {
            level.playSound(player, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }

        // 尝试向方块倒茶
        if (TeapotItem.getFluidAmount(itemInHand) > 0) {
            if (teaType.instantPouring(context)) {
                BlockHitResult blockHit = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, context.isInside());
                int consumed = teaType.onPouredOnBlock(level, blockHit, player, itemInHand);
                if (consumed != 0) {
                    shrinkFluidAmount(itemInHand, consumed);
                    return InteractionResult.SUCCESS;
                }
            } else {
                InteractionResult result = ItemUtils.startUsingInstantly(level, player, context.getHand()).getResult();
                return result == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : result;
            }
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // 尝试装水
        ItemStack itemInHand = player.getItemInHand(hand);
        ITeaType teaType = getTeaType(itemInHand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = blockhitresult.getBlockPos();
            FluidState fluidState = level.getFluidState(blockPos);
            if (tryFillWithFluid(itemInHand, teaType, fluidState)) {
                level.playSound(player, blockPos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
                return InteractionResultHolder.success(itemInHand);
            }
        }

        return super.use(level, player, hand);
    }

    protected boolean tryFillWithFluid(ItemStack teapotItem, ITeaType teaType, FluidState... fluidStates) {
        List<FluidState> fluidStateList = Arrays.stream(fluidStates).toList();
        // 若茶壶为空，遍历检查所有绑定有茶的流体类型
        if (teaType.getName().equals(ModTeaTypes.EMPTY)) {
            for (Map.Entry<ResourceLocation, FluidType> entry : TeaTypeManager.getBoundFluidTypes().entrySet()) {
                ITeaType type = TeaTypeManager.getTeaType(entry.getKey());
                FluidType fluidType = TeaTypeManager.getBoundFluid(entry.getKey());
                if (fluidStateList.stream().anyMatch(s -> s.getType().getFluidType() == fluidType)) {
                    setFluidAmount(teapotItem, TeapotBlockEntity.MAX_FLUID_AMOUNT);
                    setTeaType(teapotItem, type);
                    return true;
                }
            }
        } // 否则只检查茶壶装的流体
        else {
            FluidType fluidType = TeaTypeManager.getBoundFluid(teaType.getName());
            if (fluidType != null && fluidStateList.stream().anyMatch(s -> s.getType().getFluidType() == fluidType)) {
                setFluidAmount(teapotItem, TeapotBlockEntity.MAX_FLUID_AMOUNT);
                return true;
            }
        }

        return false;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (getFluidAmount(stack) > 0) {
            Vec3 start = entity.getEyePosition();
            Vec3 direction = entity.getViewVector(1.0F);
            Vec3 end = start.add(direction.scale(4.5));
            BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
            ITeaType teaType = getTeaType(stack);
            // 尝试向方块倒茶
            if (blockHit.getType() == HitResult.Type.BLOCK) {
                int consumed = teaType.onPouredOnBlock(level, blockHit, entity, stack);
                if (consumed != 0) {
                    shrinkFluidAmount(stack, consumed);
                }
            }
        }
        return stack;
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Player player = context.getPlayer();
        // 只有潜行时允许放置
        if (player != null && !player.isSecondaryUseActive()) {
            return false;
        }

        return super.placeBlock(context, state);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        // 放置需要载入内含流体信息
        if (level.getBlockEntity(pos) instanceof TeapotBlockEntity be) {
            be.loadFromItem(stack);
        }
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getFluidAmount(stack) > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return getTeaType(stack).getBarColor();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float)getFluidAmount(stack) * 13.0F / (float)TeapotBlockEntity.MAX_FLUID_AMOUNT);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("teatype.%s.name".formatted(getTeaType(stack).getName().toLanguageKey())).withStyle(ChatFormatting.GRAY));
    }
}
