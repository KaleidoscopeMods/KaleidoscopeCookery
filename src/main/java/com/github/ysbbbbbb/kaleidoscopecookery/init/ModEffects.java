package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.effect.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, KaleidoscopeCookery.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> FLATULENCE = EFFECTS.register("flatulence", () -> new FlatulenceEffect(0xFFC6C6));
    public static final DeferredHolder<MobEffect, MobEffect> TUNDRA_STRIDER = EFFECTS.register("tundra_strider", () -> new BaseEffect(0xA1F8FC));
    public static final DeferredHolder<MobEffect, MobEffect> WARMTH = EFFECTS.register("warmth", () -> new WarmthEffect(0xFF5F0E));
    public static final DeferredHolder<MobEffect, MobEffect> SATIATED_SHIELD = EFFECTS.register("satiated_shield", () -> new BaseEffect(0xFF1313));
    public static final DeferredHolder<MobEffect, MobEffect> VIGOR = EFFECTS.register("vigor", () -> new VigorEffect(0x84C322));
    public static final DeferredHolder<MobEffect, MobEffect> SULFUR = EFFECTS.register("sulfur", () -> new SulfurEffect(0xE8B75E));
    public static final DeferredHolder<MobEffect, MobEffect> MUSTARD = EFFECTS.register("mustard", () -> new BaseEffect(0x5A6D09));
    public static final DeferredHolder<MobEffect, MobEffect> PRESERVATION = EFFECTS.register("preservation", () -> new BaseEffect(0xAEC639));
    public static final DeferredHolder<MobEffect, MobEffect> HINDER = EFFECTS.register("hinder", () -> new BaseEffect(0x9E7E5A));
    public static final DeferredHolder<MobEffect, MobEffect> PROJECTILE_DODGE = EFFECTS.register("projectile_dodge", () -> new BaseEffect(0x8E27F7));
    public static final DeferredHolder<MobEffect, MobEffect> INSTANT_SMELTING = EFFECTS.register("instant_smelting", () -> new BaseEffect(0xF07C1C));
    public static final DeferredHolder<MobEffect, MobEffect> VITALITY = EFFECTS.register("vitality", () -> new BaseEffect(0x6A9E4E));
}
