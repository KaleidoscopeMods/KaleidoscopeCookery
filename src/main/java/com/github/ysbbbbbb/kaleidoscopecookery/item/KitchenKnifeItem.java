package com.github.ysbbbbbb.kaleidoscopecookery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.List;

import static net.neoforged.neoforge.common.ItemAbilities.SWORD_DIG;

public class KitchenKnifeItem extends SwordItem {
    public KitchenKnifeItem(Tier tier) {
        super(tier, new Properties().attributes(SwordItem.createAttributes(tier, 3, -2.4F)));
    }

    public KitchenKnifeItem(Tier tier, Properties properties) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, 3, -2.4F)));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.kitchen_knife").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        // 菜刀不能横扫之刃
        return itemAbility == SWORD_DIG;
    }
}
