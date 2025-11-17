package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents.KITCHEN_SHOVEL_HAS_OIL;

public class KitchenShovelItem extends ShovelItem {
    public static final ResourceLocation HAS_OIL_PROPERTY = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "has_oil");
    private static final int NO_OIL = 0;
    private static final int HAS_OIL = 1;

    public KitchenShovelItem() {
        super(Tiers.IRON, new Properties().attributes(ShovelItem.createAttributes(Tiers.IRON, -1, -2.0F)));
    }

    public static void setHasOil(ItemStack stack, boolean hasOil) {
        stack.set(KITCHEN_SHOVEL_HAS_OIL, hasOil);
    }

    public static boolean hasOil(ItemStack stack) {
        if (stack.has(KITCHEN_SHOVEL_HAS_OIL)) {
            return Boolean.TRUE.equals(stack.get(KITCHEN_SHOVEL_HAS_OIL));
        }
        return false;
    }

    public static float getTexture(ItemStack stack, Level level, LivingEntity entity, int seed) {
        if (hasOil(stack)) {
            return HAS_OIL;
        }
        return NO_OIL;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (level.getBlockEntity(clickedPos) instanceof PotBlockEntity potBlockEntity
            && player != null && player.isSecondaryUseActive()
            && potBlockEntity.getStatus() == PotBlockEntity.FINISHED
            && !potBlockEntity.hasCarrier()) {
            potBlockEntity.takeOutProduct(level, player, stack);
            return InteractionResult.SUCCESS;
        }

        InteractionResult result = super.useOn(context);
        if (result.indicateItemUse() && hasOil(context.getItemInHand())) {
            setHasOil(context.getItemInHand(), false);
        }
        return result;
    }
}
