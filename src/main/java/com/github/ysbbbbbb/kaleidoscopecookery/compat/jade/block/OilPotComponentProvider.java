package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.OilPotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum OilPotComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockState blockState = blockAccessor.getBlockState();
        if (!blockState.hasProperty(OilPotBlock.OIL_COUNT)) {
            return;
        }
        int count = blockState.getValue(OilPotBlock.OIL_COUNT);
        Component text;
        if (count > 0) {
            text = Component.translatable("tooltip.kaleidoscope_cookery.oil_pot.count", count);
        } else {
            text = Component.translatable("tooltip.kaleidoscope_cookery.oil_pot.empty");
        }
        iTooltip.add(iTooltip.getElementHelper().text(text));
    }

    @Override
    public ResourceLocation getUid() {
        return ModPlugin.OIL_POT;
    }
}
