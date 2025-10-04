package com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TagDamage extends DamageTypeTagsProvider {
    public TagDamage(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper fileHelper) {
        super(output, lookupProvider, KaleidoscopeCookery.MOD_ID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(TagMod.SATIATED_SHIELD_WEAKNESS)
                .add(
                        DamageTypes.WITHER_SKULL,
                        DamageTypes.WITHER,
                        DamageTypes.SONIC_BOOM,
                        DamageTypes.INDIRECT_MAGIC,
                        DamageTypes.IN_WALL,
                        DamageTypes.FREEZE
                )
                .addTag(DamageTypeTags.IS_EXPLOSION)
                .addTag(DamageTypeTags.IS_LIGHTNING);
    }
}
