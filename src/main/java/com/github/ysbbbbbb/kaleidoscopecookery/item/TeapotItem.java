package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ITeapot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer.EMPTY_TEA_FLUID;

public class TeapotItem extends BlockItem {
    public TeapotItem() {
        super(ModBlocks.TEAPOT.get(), new Properties().stacksTo(1));
    }

    /**
     * 获取当前茶壶倾倒出的茶叶
     */
    public static ItemStack getPourOut(ItemStack stack) {
        CompoundTag data = BlockItem.getBlockEntityData(stack);
        if (data == null) {
            return ItemStack.EMPTY;
        }

        // 先判断状态
        int status = data.getInt(TeapotBlockEntity.STATUS);
        if (status != ITeapot.FINISHED) {
            return ItemStack.EMPTY;
        }

        // 还有茶水剩余么
        return ItemStack.of(data.getCompound(TeapotBlockEntity.RESULT));
    }

    /**
     * 执行倾倒，此时会扣除一数量成品
     */
    public static void pourOut(ItemStack stack) {
        CompoundTag data = BlockItem.getBlockEntityData(stack);
        if (data == null) {
            return;
        }

        int status = data.getInt(TeapotBlockEntity.STATUS);
        if (status != ITeapot.FINISHED) {
            return;
        }

        ItemStack result = ItemStack.of(data.getCompound(TeapotBlockEntity.RESULT));
        if (result.isEmpty()) {
            return;
        }

        result.shrink(1);
        // 如果倒完了，直接重置所有内容
        if (result.isEmpty()) {
            stack.removeTagKey(BlockItem.BLOCK_ENTITY_TAG);
            return;
        }

        // 否则只更新数量
        data.put(TeapotBlockEntity.RESULT, result.serializeNBT());
        BlockItem.setBlockEntityData(stack, ModBlocks.TEAPOT_BE.get(), data);
    }

    public static boolean fillFluid(ItemStack stack, Fluid fluid, LivingEntity user) {
        CompoundTag data = BlockItem.getBlockEntityData(stack);
        if (data == null) {
            data = new CompoundTag();
        }
        // 先判断状态
        int status = data.getInt(TeapotBlockEntity.STATUS);
        if (status != ITeapot.PUT_INGREDIENT) {
            return false;
        }
        // 再判断是否存在流体
        String fluidId = StringUtils.defaultIfBlank(data.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
        if (!fluidId.equals(EMPTY_TEA_FLUID.toString())) {
            return false;
        }
        // 执行流体添加
        ResourceLocation key = ForgeRegistries.FLUIDS.getKey(fluid);
        if (key == null) {
            return false;
        }
        data.putString(TeapotBlockEntity.TEA_FLUID_ID, key.toString());
        BlockItem.setBlockEntityData(stack, ModBlocks.TEAPOT_BE.get(), data);

        SoundEvent sound = fluid.getFluidType().getSound(user, SoundActions.BUCKET_FILL);
        if (sound != null) {
            user.playSound(sound);
        }
        return true;
    }

    private static void sendActionBarMessage(LivingEntity user, String key, Object... args) {
        if (user instanceof ServerPlayer serverPlayer) {
            MutableComponent message = Component.translatable(key, args);
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        // 潜行时只放置方块
        if (player == null || player.isSecondaryUseActive()) {
            return super.useOn(context);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);

        // 如果已经有流体了，返回
        CompoundTag data = BlockItem.getBlockEntityData(itemInHand);
        if (data != null) {
            String fluidId = StringUtils.defaultIfBlank(data.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
            if (!fluidId.equals(EMPTY_TEA_FLUID.toString())) {
                sendActionBarMessage(player, "tooltip.kaleidoscope_cookery.teapot.add_tea_fluid.has_fluid");
                return InteractionResultHolder.fail(itemInHand);
            }
        }

        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemInHand);
        }
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemInHand);
        }

        BlockPos pos = hitResult.getBlockPos();
        Direction direction = hitResult.getDirection();
        BlockPos relative = pos.relative(direction);

        // 权限检查
        if (!level.mayInteract(player, pos) || !player.mayUseItemAt(relative, direction, itemInHand)) {
            return InteractionResultHolder.fail(itemInHand);
        }

        BlockState blockState = level.getBlockState(pos);
        // 必须是可以用桶取流体的方块
        if (!(blockState.getBlock() instanceof BucketPickup bucketpickup)) {
            return InteractionResultHolder.fail(itemInHand);
        }

        // 执行取流体操作
        ItemStack pickup = bucketpickup.pickupBlock(level, pos, blockState);
        if (pickup.isEmpty()) {
            return InteractionResultHolder.fail(itemInHand);
        }

        var capability = pickup.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
        return capability.map(fluidHandler -> {
            Fluid fluid = fluidHandler.getFluidInTank(0).getFluid();
            boolean result = fillFluid(itemInHand, fluid, player);
            if (result) {
                return InteractionResultHolder.sidedSuccess(itemInHand, level.isClientSide());
            }
            sendActionBarMessage(player, "tooltip.kaleidoscope_cookery.teapot.add_tea_fluid.has_fluid");
            return InteractionResultHolder.fail(itemInHand);
        }).orElse(InteractionResultHolder.fail(itemInHand));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        // 两种情况显示进度条
        // 1 准备阶段，装了流体
        // 2 完成阶段，有产物
        CompoundTag data = BlockItem.getBlockEntityData(stack);
        if (data == null) {
            return false;
        }

        int status = data.getInt(TeapotBlockEntity.STATUS);

        if (status == ITeapot.PUT_INGREDIENT) {
            String fluidId = StringUtils.defaultIfBlank(data.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
            return !fluidId.equals(EMPTY_TEA_FLUID.toString());
        }

        if (status == ITeapot.FINISHED) {
            ItemStack result = ItemStack.of(data.getCompound(TeapotBlockEntity.RESULT));
            return !result.isEmpty();
        }

        return false;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x9df7ff;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        CompoundTag data = BlockItem.getBlockEntityData(stack);
        if (data == null) {
            return 0;
        }

        int status = data.getInt(TeapotBlockEntity.STATUS);

        if (status == ITeapot.PUT_INGREDIENT) {
            String fluidId = StringUtils.defaultIfBlank(data.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
            if (fluidId.equals(EMPTY_TEA_FLUID.toString())) {
                return 0;
            }
            return 13;
        }

        if (status == ITeapot.FINISHED) {
            ItemStack result = ItemStack.of(data.getCompound(TeapotBlockEntity.RESULT));
            if (result.isEmpty()) {
                return 0;
            }
            // 进度条长度根据剩余产物数量占总量的比例来计算，满了是13格
            int count = result.getCount();
            return Math.round(13.0F * count / TeapotRecipe.OUTPUT_COUNT);
        }

        return 0;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pFlag) {
        // 如果是成品阶段，那么显示成品信息
        CompoundTag data = BlockItem.getBlockEntityData(pStack);
        if (data == null) {
            return;
        }

        int status = data.getInt(TeapotBlockEntity.STATUS);
        if (status == ITeapot.PUT_INGREDIENT) {
            String fluidId = StringUtils.defaultIfBlank(data.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
            if (fluidId.equals(EMPTY_TEA_FLUID.toString())) {
                return;
            }
            ResourceLocation key = new ResourceLocation(fluidId);
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(key);
            if (fluid != null) {
                list.add(Component.translatable(fluid.getFluidType().getDescriptionId()).withStyle(ChatFormatting.GRAY));
            }
        }

        if (status == ITeapot.FINISHED) {
            ItemStack result = ItemStack.of(data.getCompound(TeapotBlockEntity.RESULT));
            if (result.isEmpty()) {
                return;
            }
            Component resultComponent = ComponentUtils.formatList(Arrays.asList(
                    result.getHoverName(),
                    Component.literal("x%d".formatted(result.getCount()))
            ), CommonComponents.space(), Function.identity()).withStyle(ChatFormatting.GRAY);
            list.add(resultComponent);
        }
    }
}
