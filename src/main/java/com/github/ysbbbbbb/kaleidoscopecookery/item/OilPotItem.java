package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.OilPotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BlockItemStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OilPotItem extends BlockItem {
    public static final ResourceLocation HAS_OIL_PROPERTY = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "has_oil");
    public static final int MAX_COUNT = 8;

    private static final int NO_OIL = 0;
    private static final int HAS_OIL = 1;

    public OilPotItem() {
        super(ModBlocks.OIL_POT, new Properties().stacksTo(16));
    }

    public static void setOilCount(ItemStack stack, int count) {
        count = Mth.clamp(count, 0, MAX_COUNT);
        BlockItemStateProperties properties = new BlockItemStateProperties(Maps.newHashMap());
        stack.set(DataComponents.BLOCK_STATE, properties.with(OilPotBlock.OIL_COUNT, count));
    }

    public static int getOilCount(ItemStack stack) {
        BlockItemStateProperties properties = stack.get(DataComponents.BLOCK_STATE);
        if (properties == null) {
            return 0;
        }
        Integer i = properties.get(OilPotBlock.OIL_COUNT);
        return i == null ? 0 : i;
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
        ItemStack stack = new ItemStack(ModBlocks.OIL_POT);
        setOilCount(stack, MAX_COUNT);
        return stack;
    }

    @Environment(EnvType.CLIENT)
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
