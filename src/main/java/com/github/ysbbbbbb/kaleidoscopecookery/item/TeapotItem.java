package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ITeapot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import org.apache.commons.lang3.StringUtils;

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
    public static ItemStack getPourOut(ItemStack stack, Level level) {
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) {
            return ItemStack.EMPTY;
        }
        CompoundTag tag = data.copyTag();

        // 先判断状态
        int status = tag.getInt(TeapotBlockEntity.STATUS);
        if (status != ITeapot.FINISHED) {
            return ItemStack.EMPTY;
        }

        // 还有茶水剩余么
        return ItemStack.parseOptional(level.registryAccess(), tag.getCompound(TeapotBlockEntity.RESULT));
    }

    /**
     * 执行倾倒，此时会扣除一数量成品
     */
    public static void pourOut(ItemStack stack, Level level) {
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) {
            return;
        }
        CompoundTag tag = data.copyTag();

        int status = tag.getInt(TeapotBlockEntity.STATUS);
        if (status != ITeapot.FINISHED) {
            return;
        }

        ItemStack result = ItemStack.parseOptional(level.registryAccess(), tag.getCompound(TeapotBlockEntity.RESULT));
        if (result.isEmpty()) {
            return;
        }

        result.shrink(1);
        // 如果倒完了，直接重置所有内容
        if (result.isEmpty()) {
            stack.remove(DataComponents.BLOCK_ENTITY_DATA);
            return;
        }

        // 否则只更新数量
        tag.put(TeapotBlockEntity.RESULT, result.saveOptional(level.registryAccess()));
        BlockItem.setBlockEntityData(stack, ModBlocks.TEAPOT_BE.get(), tag);
    }

    public static boolean fillFluid(ItemStack stack, Fluid fluid, LivingEntity user) {
        CustomData data = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
        CompoundTag tag = data.copyTag();
        // 先判断状态
        int status = tag.getInt(TeapotBlockEntity.STATUS);
        if (status != ITeapot.PUT_INGREDIENT) {
            return false;
        }
        // 再判断是否存在流体
        String fluidId = StringUtils.defaultIfBlank(tag.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
        if (!fluidId.equals(EMPTY_TEA_FLUID.toString())) {
            return false;
        }
        // 执行流体添加
        ResourceLocation key = BuiltInRegistries.FLUID.getKey((fluid));
        tag.putString(TeapotBlockEntity.TEA_FLUID_ID, key.toString());
        BlockItem.setBlockEntityData(stack, ModBlocks.TEAPOT_BE.get(), tag);

        SoundEvent sound = fluid.getFluidType().getSound(user, SoundActions.BUCKET_FILL);
        if (sound != null) {
            user.playSound(sound);
        }
        return true;
    }

    public static void clearAll(ItemStack stack, Player player) {
        stack.remove(DataComponents.BLOCK_ENTITY_DATA);
        player.playSound(SoundEvents.BUCKET_EMPTY, 1.0F, 1.0F);
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
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) {
            return InteractionResult.PASS;
        }
        CompoundTag tag = data.copyTag();
        Level level = player.level();

        // 先判断状态
        int status = tag.getInt(TeapotBlockEntity.STATUS);
        if (status == ITeapot.FINISHED) {
            pourOut(stack, level);
        } else if (status == ITeapot.PUT_INGREDIENT) {
            String fluidId = StringUtils.defaultIfBlank(tag.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
            if (!"minecraft:lava".equals(fluidId)) {
                // 仅岩浆能烫伤生物
                return InteractionResult.PASS;
            }
            // 概率消耗
            if (player.getRandom().nextFloat() < 0.3F) {
                clearAll(stack, player);
            }
        }


        RandomSource random = level.random;
        target.hurt(level.damageSources().inFire(), 3);

        double x = target.getX();
        double y = target.getY() + target.getEyeHeight() + 0.25;
        double z = target.getZ();

        player.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);

        for (int i = 0; i < 10; i++) {
            level.addParticle(ParticleTypes.LAVA,
                    x + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    y + random.nextDouble() / 3,
                    z + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    0.3, 0.1, 0.3);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);

        // 如果已经有流体了，返回
        CustomData data = itemInHand.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data != null) {
            CompoundTag tag = data.copyTag();
            String fluidId = StringUtils.defaultIfBlank(tag.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
            if (!fluidId.equals(EMPTY_TEA_FLUID.toString())) {
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
        ItemStack pickup = bucketpickup.pickupBlock(player, level, pos, blockState);
        if (pickup.isEmpty()) {
            return InteractionResultHolder.fail(itemInHand);
        }

        var capability = pickup.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability == null) {
            return InteractionResultHolder.fail(itemInHand);
        }
        Fluid fluid = capability.getFluidInTank(0).getFluid();
        boolean result = fillFluid(itemInHand, fluid, player);
        if (result) {
            return InteractionResultHolder.sidedSuccess(itemInHand, level.isClientSide());
        }
        return InteractionResultHolder.fail(itemInHand);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        // 两种情况显示进度条
        // 1 准备阶段，装了流体
        // 2 完成阶段，有产物
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) {
            return false;
        }
        CompoundTag tag = data.copyTag();

        int status = tag.getInt(TeapotBlockEntity.STATUS);

        if (status == ITeapot.PUT_INGREDIENT) {
            String fluidId = StringUtils.defaultIfBlank(tag.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
            return !fluidId.equals(EMPTY_TEA_FLUID.toString());
        }

        return status == ITeapot.FINISHED;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getBarColor(ItemStack stack) {
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) {
            return 0x9df7ff;
        }
        CompoundTag tag = data.copyTag();
        int status = tag.getInt(TeapotBlockEntity.STATUS);
        if (status == ITeapot.PUT_INGREDIENT) {
            String fluidId = StringUtils.defaultIfBlank(tag.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
            if (fluidId.equals(EMPTY_TEA_FLUID.toString())) {
                return 0x9df7ff;
            }
            Fluid fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(fluidId));
            // 熔岩特殊
            if (fluid.equals(Fluids.LAVA)) {
                return 0xfba800;
            }
            return IClientFluidTypeExtensions.of(fluid).getTintColor();
        }
        return 0x9df7ff;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) {
            return 0;
        }
        CompoundTag tag = data.copyTag();

        int status = tag.getInt(TeapotBlockEntity.STATUS);

        if (status == ITeapot.PUT_INGREDIENT) {
            String fluidId = StringUtils.defaultIfBlank(tag.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
            if (fluidId.equals(EMPTY_TEA_FLUID.toString())) {
                return 0;
            }
            return 13;
        }

        if (status == ITeapot.FINISHED) {
            int count = tag.getCompound(TeapotBlockEntity.RESULT).getInt("count");
            if (count <= 0) {
                return 0;
            }
            // 进度条长度根据剩余产物数量占总量的比例来计算，满了是13格
            return Math.round(13.0F * count / TeapotRecipe.OUTPUT_COUNT);
        }

        return 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag pFlag) {
        // 如果是成品阶段，那么显示成品信息
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) {
            return;
        }
        CompoundTag tag = data.copyTag();

        int status = tag.getInt(TeapotBlockEntity.STATUS);
        if (status == ITeapot.PUT_INGREDIENT) {
            String fluidId = StringUtils.defaultIfBlank(tag.getString(TeapotBlockEntity.TEA_FLUID_ID), EMPTY_TEA_FLUID.toString());
            if (fluidId.equals(EMPTY_TEA_FLUID.toString())) {
                return;
            }
            ResourceLocation key = ResourceLocation.parse(fluidId);
            Fluid fluid = BuiltInRegistries.FLUID.get(key);
            list.add(Component.translatable(fluid.getFluidType().getDescriptionId()).withStyle(ChatFormatting.GRAY));
        }

        ClientLevel level = Minecraft.getInstance().level;
        if (status == ITeapot.FINISHED && level != null) {
            ItemStack result = ItemStack.parseOptional(level.registryAccess(), tag.getCompound(TeapotBlockEntity.RESULT));
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
