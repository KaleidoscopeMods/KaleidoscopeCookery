package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent BLOCK_RECIPE_BLOCK = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block.recipe_block"));
    public static final SoundEvent BLOCK_MILLSTONE = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block.millstone"));
    public static final SoundEvent BLOCK_STOCKPOT = SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block.stockpot"), 16.0F);
    public static final SoundEvent BLOCK_PADDY = SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block.paddy"), 16.0F);
    public static final SoundEvent ENTITY_FART = SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "entity.fart"), 16.0F);
    public static final SoundEvent ITEM_DOUGH_TRANSFORM = SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item.dough_transform"), 16.0F);

    public static void registerSounds() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block.recipe_block"), BLOCK_RECIPE_BLOCK);
        Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block.millstone"), BLOCK_MILLSTONE);
        Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block.stockpot"), BLOCK_STOCKPOT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block.paddy"), BLOCK_PADDY);
        Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "entity.fart"), ENTITY_FART);
        Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item.dough_transform"), ITEM_DOUGH_TRANSFORM);
    }
}
