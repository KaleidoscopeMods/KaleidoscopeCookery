package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.enchantment.QuickKnifeEnchantment;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public interface ModEnchantments {
    DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, KaleidoscopeCookery.MOD_ID);

    RegistryObject<Enchantment> QUICK_KNIFE = ENCHANTMENTS.register("quick_knife", QuickKnifeEnchantment::new);
}
