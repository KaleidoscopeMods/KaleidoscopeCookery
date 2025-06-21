package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class SoundDefinitionsGenerator extends SoundDefinitionsProvider {
    protected SoundDefinitionsGenerator(PackOutput output, ExistingFileHelper helper) {
        super(output, KaleidoscopeCookery.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        SoundDefinition stockpotSound = definition().subtitle(subtitle("block.stockpot"));
        for (int i = 0; i < 7; i++) {
            stockpotSound.with(sound("block/stockpot/stockpot_" + i));
        }
        this.add(ModSounds.BLOCK_STOCKPOT.get(), stockpotSound);

        SoundDefinition paddySound = definition().subtitle(subtitle("block.paddy"))
                .with(sound("block/paddy/paddy_0"))
                .with(sound("block/paddy/paddy_1"));
        this.add(ModSounds.BLOCK_PADDY.get(), paddySound);
    }

    protected static SoundDefinition.Sound sound(final String name) {
        return sound(new ResourceLocation(KaleidoscopeCookery.MOD_ID, name));
    }

    protected static String subtitle(String subtitle) {
        return "subtitles.%s.%s".formatted(KaleidoscopeCookery.MOD_ID, subtitle);
    }
}
