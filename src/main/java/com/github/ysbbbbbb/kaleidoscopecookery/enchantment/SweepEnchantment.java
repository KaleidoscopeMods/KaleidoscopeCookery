package com.github.ysbbbbbb.kaleidoscopecookery.enchantment;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class SweepEnchantment extends Enchantment {
    public SweepEnchantment() {
        super(Rarity.UNCOMMON, ModEnchantments.SICKLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
