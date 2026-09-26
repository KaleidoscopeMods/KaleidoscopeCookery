package com.github.ysbbbbbb.kaleidoscopecookery.enchantment;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class QuickKnifeEnchantment extends Enchantment {
    public QuickKnifeEnchantment() {
        super(Rarity.COMMON, ModEnchantments.KITCHEN_KNIFE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
