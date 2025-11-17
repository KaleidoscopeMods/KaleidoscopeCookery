package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.EnamelBasinBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EnamelBasinComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        BlockState blockState = accessor.getBlockState();
        int oilCount = blockState.getValue(EnamelBasinBlock.OIL_COUNT);
        MutableComponent info = Component.translatable("jade.kaleidoscope_cookery.enamel_basin.oil_count", oilCount);
        tooltip.add(info);
    }

    @Override
    public ResourceLocation getUid() {
        return ModPlugin.ENAMEL_BASIN;
    }
}
