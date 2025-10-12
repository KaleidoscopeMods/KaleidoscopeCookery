package com.github.ysbbbbbb.kaleidoscopecookery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StrawHatItem extends ArmorItem {
    private final boolean hasFlower;

    public StrawHatItem(boolean hasFlower) {
        super(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties());
        this.hasFlower = hasFlower;
    }

    public boolean hasFlower() {
        return hasFlower;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.straw_hat").withStyle(ChatFormatting.GRAY));
    }
}
