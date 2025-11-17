package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ShawarmaSpitBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum ShawarmaSpitComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        if (!(accessor.getBlockEntity() instanceof ShawarmaSpitBlockEntity shawarmaSpitFirst)) {
            return;
        }
        Level level = accessor.getLevel();
        BlockPos pos = accessor.getPosition();
        DoubleBlockHalf value = accessor.getBlockState().getValue(ShawarmaSpitBlock.HALF);
        if (value == DoubleBlockHalf.LOWER && level.getBlockEntity(pos.above()) instanceof ShawarmaSpitBlockEntity shawarmaSpitSecond) {
            addItemInfo(tooltip, shawarmaSpitSecond);
            addItemInfo(tooltip, shawarmaSpitFirst);
            return;
        }

        if (value == DoubleBlockHalf.UPPER && level.getBlockEntity(pos.below()) instanceof ShawarmaSpitBlockEntity shawarmaSpitSecond) {
            addItemInfo(tooltip, shawarmaSpitFirst);
            addItemInfo(tooltip, shawarmaSpitSecond);
        }
    }

    private void addItemInfo(ITooltip tooltip, ShawarmaSpitBlockEntity shawarmaSpit) {
        IElementHelper helper = IElementHelper.get();
        ItemStack showItem = shawarmaSpit.cookingItem.isEmpty() ? shawarmaSpit.cookedItem : shawarmaSpit.cookingItem;
        if (!showItem.isEmpty()) {
            IElement icon = helper.smallItem(showItem.copyWithCount(1));
            MutableComponent stackName = IDisplayHelper.get().stripColor(showItem.getHoverName());
            IElement text = helper.text(Component.literal("%d×".formatted(showItem.getCount())).append(stackName).withStyle(ChatFormatting.GRAY));
            tooltip.add(icon);
            tooltip.append(helper.spacer(3, 1));
            tooltip.append(text);
            if (shawarmaSpit.cookTime > 0) {
                tooltip.append(helper.spacer(3, 1));
                tooltip.append(IThemeHelper.get().seconds(shawarmaSpit.cookTime).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModPlugin.SHAWARMA_SPIT;
    }
}
