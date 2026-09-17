package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TeaBannerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public class TeaBannerItem extends BlockItem {
    public TeaBannerItem() {
        super(ModBlocks.TEA_BANNER.get(), new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        CompoundTag tag = data == null ? null : data.copyTag();
        DyeColor color = tag == null ? DyeColor.RED : TeaBannerBlockEntity.getColor(tag);
        HolderLookup.Provider registries = context.registries();
        ItemStack patternItem = tag == null || registries == null || !tag.contains(TeaBannerBlockEntity.PATTERN_ITEM_TAG)
                ? ItemStack.EMPTY
                : ItemStack.parseOptional(registries, tag.getCompound(TeaBannerBlockEntity.PATTERN_ITEM_TAG));
        if (!TeaBannerBlockEntity.isSupportedPattern(patternItem)) {
            patternItem = ItemStack.EMPTY;
        }

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
