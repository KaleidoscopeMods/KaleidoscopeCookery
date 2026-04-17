package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, KaleidoscopeCookery.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_STOCKPOT = registerSound("block.stockpot");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_PADDY = registerSound("block.paddy");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_MILLSTONE = registerSound("block.millstone");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_RECIPE_BLOCK = registerSound("block.recipe_block");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_TEAPOT_PROCESSING = registerSound("block.teapot.processing");
    public static final DeferredHolder<SoundEvent, SoundEvent> TRASH_CAN = registerSound("block.trash_can");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_FART = registerSound("entity.fart");
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_DOUGH_TRANSFORM = registerSound("item.dough_transform");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, name), 16.0F));
    }
}
