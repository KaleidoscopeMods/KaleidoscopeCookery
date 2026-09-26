package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.enchantment.QuickKnifeEnchantment;
import com.github.ysbbbbbb.kaleidoscopecookery.enchantment.SweepEnchantment;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenKnifeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SickleItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public interface ModEnchantments {
    DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, KaleidoscopeCookery.MOD_ID);

    EnchantmentCategory KITCHEN_KNIFE = EnchantmentCategory.create("kitchen_knife", item -> item instanceof KitchenKnifeItem);
    EnchantmentCategory SICKLE = EnchantmentCategory.create("sickle", item -> item instanceof SickleItem);

    RegistryObject<Enchantment> QUICK_KNIFE = ENCHANTMENTS.register("quick_knife", QuickKnifeEnchantment::new);
    RegistryObject<Enchantment> SWEEP = ENCHANTMENTS.register("sweep", SweepEnchantment::new);
}
