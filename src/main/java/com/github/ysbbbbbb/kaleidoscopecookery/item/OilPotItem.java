package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.OilPotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents.OIL_POT_OIL_COUNT;

public class OilPotItem extends BlockItem {
    public static final ResourceLocation HAS_OIL_PROPERTY = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "has_oil");

    private static final String OIL_COUNT = "oil_count";
    private static final int NO_OIL = 0;
    private static final int HAS_OIL = 1;

    public OilPotItem() {
        super(ModBlocks.OIL_POT.get(), new Item.Properties().stacksTo(1));
    }

    public static void setOilCount(ItemStack stack, int count) {
        count = Mth.clamp(count, 0, OilPotBlockEntity.MAX_OIL_COUNT);
        stack.set(OIL_POT_OIL_COUNT, count);
    }

    public static int getOilCount(ItemStack stack) {
        return stack.getOrDefault(OIL_POT_OIL_COUNT, 0);
    }

    public static boolean hasOil(ItemStack stack) {
        return getOilCount(stack) > 0;
    }

    public static void shrinkOilCount(ItemStack stack) {
        int currentCount = getOilCount(stack);
        if (currentCount > 0) {
            setOilCount(stack, currentCount - 1);
        }
    }

    public static ItemStack getFullOilPot() {
        ItemStack stack = new ItemStack(ModBlocks.OIL_POT.get());
        setOilCount(stack, OilPotBlockEntity.MAX_OIL_COUNT);
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    public static float getTexture(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (hasOil(stack)) {
            return HAS_OIL;
        }
        return NO_OIL;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        int oilCount = getOilCount(pStack);
        if (oilCount > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.kaleidoscope_cookery.oil_pot.count", oilCount)
                    .withStyle(ChatFormatting.GRAY));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.kaleidoscope_cookery.oil_pot.empty")
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
