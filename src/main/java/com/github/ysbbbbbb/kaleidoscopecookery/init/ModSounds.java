package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent BLOCK_STOCKPOT = SoundEvent.createFixedRangeEvent(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "block.stockpot"), 16.0F);
    public static final SoundEvent BLOCK_PADDY = SoundEvent.createFixedRangeEvent(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "block.paddy"), 16.0F);
    public static final SoundEvent ENTITY_FART = SoundEvent.createFixedRangeEvent(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "entity.fart"), 16.0F);

    public static void registerSounds() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "block.stockpot"), BLOCK_STOCKPOT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "block.paddy"), BLOCK_PADDY);
        Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "entity.fart"), ENTITY_FART);
    }
}
