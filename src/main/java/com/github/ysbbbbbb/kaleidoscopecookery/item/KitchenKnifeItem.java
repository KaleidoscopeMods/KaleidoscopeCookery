package com.github.ysbbbbbb.kaleidoscopecookery.item;

import io.github.fabricators_of_create.porting_lib.tool.ToolAction;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class KitchenKnifeItem extends SwordItem {
    public KitchenKnifeItem(Tier tier) {
        super(tier, 0, -2.0F, new Properties());
    }

    public KitchenKnifeItem(Tier tier, Properties properties) {
        super(tier, 0, -2.0F, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.kitchen_knife").withStyle(ChatFormatting.GRAY));
    }

//    @Override
//    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
//        // 菜刀不能横扫之刃
//        return toolAction == SWORD_DIG;
//    }
}
