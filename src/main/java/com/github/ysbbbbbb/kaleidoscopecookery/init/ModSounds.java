package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, KaleidoscopeCookery.MOD_ID);

    public static final RegistryObject<SoundEvent> BLOCK_STOCKPOT = registerSound("block.stockpot");
    public static final RegistryObject<SoundEvent> BLOCK_PADDY = registerSound("block.paddy");
    public static final RegistryObject<SoundEvent> BLOCK_MILLSTONE = registerSound("block.millstone");
    public static final RegistryObject<SoundEvent> BLOCK_RECIPE_BLOCK = registerSound("block.recipe_block");
    public static final RegistryObject<SoundEvent> ENTITY_FART = registerSound("entity.fart");
    public static final RegistryObject<SoundEvent> ITEM_DOUGH_TRANSFORM = registerSound("item.dough_transform");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(KaleidoscopeCookery.MOD_ID, name), 16.0F));
    }
}
