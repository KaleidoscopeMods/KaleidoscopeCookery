package com.github.ysbbbbbb.kaleidoscopecookery.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public class ModSoundType {
    public static final SoundType POT = new SoundType(1.0F, 0.8F, SoundEvents.LANTERN_BREAK, SoundEvents.LANTERN_STEP, SoundEvents.LANTERN_PLACE, SoundEvents.LANTERN_HIT, SoundEvents.LANTERN_FALL);
    public static final SoundType RECIPE_BLOCK = new SoundType(1.0F, 1.0F, ModSounds.BLOCK_RECIPE_BLOCK.get(), ModSounds.BLOCK_RECIPE_BLOCK.get(), ModSounds.BLOCK_RECIPE_BLOCK.get(), ModSounds.BLOCK_RECIPE_BLOCK.get(), ModSounds.BLOCK_RECIPE_BLOCK.get());
}
