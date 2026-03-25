package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype.TeaTypeManager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTeaTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        // 潜行时只尝试放置方块
        if (player == null || player.isSecondaryUseActive()) {
            return super.useOn(context);
        }

        FluidState fluidState = level.getFluidState(pos);
        FluidState fluidState1 = level.getFluidState(pos.relative(context.getClickedFace()));
        // 若茶壶为空，遍历检查所有绑定的流体类型
        if (teaType.getName().equals(ModTeaTypes.EMPTY)) {
            for (Map.Entry<ResourceLocation, FluidType> entry : TeaTypeManager.getBoundFluidTypes().entrySet()) {
                ITeaType type = TeaTypeManager.getTeaType(entry.getKey());
                FluidType fluidType = TeaTypeManager.getBoundFluid(entry.getKey());
                if (fluidState.getType().getFluidType() == fluidType || fluidState1.getType().getFluidType() == fluidType) {
                    setFluidAmount(itemInHand, TeapotBlockEntity.MAX_FLUID_AMOUNT);
                    setTeaType(itemInHand, type);
                    return InteractionResult.SUCCESS;
                }
            }
        } // 否则只检查茶壶装的流体
        else {
            FluidType fluidType = TeaTypeManager.getBoundFluid(teaType.getName());
            if (fluidType != null && (fluidState.getType().getFluidType() == fluidType || fluidState1.getType().getFluidType() == fluidType)) {
                setFluidAmount(itemInHand, TeapotBlockEntity.MAX_FLUID_AMOUNT);
                return InteractionResult.SUCCESS;
            }
        }

        // 开始倒茶
        if (TeapotItem.getFluidAmount(itemInHand) > 0) {
            InteractionResult result = this.use(context.getLevel(), player, context.getHand()).getResult();
            return result == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : result;
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (getFluidAmount(stack) > 0) {
            HitResult result = getHitResult(entity, 4.5);
            ITeaType teaType = getTeaType(stack);
            // 尝试向方块倒茶
            if (result instanceof BlockHitResult blockHit) {
                int consumed = teaType.onPouredOnBlock(level, blockHit, entity, stack);
                if (consumed != 0) {
                    shrinkFluidAmount(stack, consumed);
                }
            } // 尝试向实体倒茶
            else if (result instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity living) {
                int consumed = teaType.onPouredOnEntity(level, living, entity, stack);
                if (consumed != 0) {
                    TeapotItem.shrinkFluidAmount(stack, consumed);
                    entity.swing(InteractionHand.MAIN_HAND);
                }
            }
        }
        return stack;
    }

    @Nullable
    private static HitResult getHitResult(LivingEntity entity, double maxDistance) {
        Level level = entity.level();
        Vec3 start = entity.getEyePosition();
        Vec3 direction = entity.getViewVector(1.0F);
        Vec3 end = start.add(direction.scale(maxDistance));

        // 1. 检测实体
        AABB aabb = new AABB(start, end).inflate(1.0D);
        List<Entity> entities = level.getEntities(entity, aabb, e -> e != entity && e.isPickable());
        EntityHitResult entityHit = null;
        double closestDistSq = Double.MAX_VALUE;
        for (Entity e : entities) {
            AABB box = e.getBoundingBox().inflate(e.getPickRadius());
            Optional<Vec3> hitPos = box.clip(start, end);
            if (hitPos.isPresent()) {
                double distSq = start.distanceToSqr(hitPos.get());
                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    entityHit = new EntityHitResult(e, hitPos.get());
                }
            }
        }
        if (entityHit != null) {
            return entityHit;
        }

        // 2. 检测方块
        BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
        if (blockHit.getType() == HitResult.Type.BLOCK) {
            return blockHit;
        }

        return null;
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Player player = context.getPlayer();
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
}
