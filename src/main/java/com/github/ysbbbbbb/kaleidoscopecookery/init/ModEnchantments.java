package com.github.ysbbbbbb.kaleidoscopecookery.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

import static com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery.MOD_ID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public interface ModEnchantments {
    ResourceKey<Enchantment> QUICK_KNIFE = ResourceKey.create(Registries.ENCHANTMENT, fromNamespaceAndPath(MOD_ID, "quick_knife"));
    ResourceKey<Enchantment> SWEEP = ResourceKey.create(Registries.ENCHANTMENT, fromNamespaceAndPath(MOD_ID, "sweep"));
}
