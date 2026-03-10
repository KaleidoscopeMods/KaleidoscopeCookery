package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StrawHatItem extends ArmorItem {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/models/armor/straw_hat.png");
    private static final ResourceLocation TEXTURE_FLOWER = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/models/armor/straw_hat_flower.png");

    private final boolean hasFlower;

    public StrawHatItem(boolean hasFlower) {
        super(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1));
        this.hasFlower = hasFlower;
    }

    @Override
    @Nullable
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        if (this.hasFlower) {
            return TEXTURE_FLOWER;
        }
        return TEXTURE;
    }

    public boolean hasFlower() {
        return hasFlower;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.straw_hat").withStyle(ChatFormatting.GRAY));
    }
}
