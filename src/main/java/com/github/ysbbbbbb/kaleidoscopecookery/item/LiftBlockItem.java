package com.github.ysbbbbbb.kaleidoscopecookery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * 带有举起姿势的方块物品
 */
public class LiftBlockItem extends WithTooltipsBlockItem {
    public LiftBlockItem(Block block, Properties properties, String name) {
        super(block, properties, name);
    }

    public LiftBlockItem(Block block, String name) {
        super(block, name);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
    }
}
