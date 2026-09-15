package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TeaBannerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeaBannerItem extends BlockItem {
    public TeaBannerItem() {
        super(ModBlocks.TEA_BANNER.get(), new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = BlockItem.getBlockEntityData(stack);
        DyeColor color = tag == null ? DyeColor.RED : TeaBannerBlockEntity.getColor(tag);
        ItemStack patternItem = tag == null ? ItemStack.EMPTY : TeaBannerBlockEntity.getPatternItem(tag);

        MutableComponent pattern = Component.translatable("tooltip.kaleidoscope_cookery.tea_banner.pattern.tea");
        if (patternItem.getItem() instanceof BannerPatternItem bannerPatternItem) {
            pattern = bannerPatternItem.getDisplayName();
        }
        MutableComponent colorName = Component.translatable("color.minecraft." + color.getName());

        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.tea_banner.color", colorName)
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.tea_banner.pattern", pattern)
                .withStyle(ChatFormatting.GRAY));
    }
}
