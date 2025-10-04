package com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TagEntityType extends EntityTypeTagsProvider {
    public TagEntityType(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, KaleidoscopeCookery.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(TagMod.PIG_OIL_SOURCE).add(EntityType.PIG, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.HOGLIN);

        this.tag(TagMod.MILLSTONE_BINDABLE).add(
                EntityType.MULE, EntityType.DONKEY,
                EntityType.HORSE, EntityType.ZOMBIE_HORSE, EntityType.SKELETON_HORSE,
                EntityType.LLAMA, EntityType.TRADER_LLAMA,
                EntityType.COW, EntityType.MOOSHROOM,
                EntityType.SHEEP, EntityType.GOAT,
                EntityType.VILLAGER
        );

        this.tag(TagMod.RICE_GROWTH_BOOSTER).add(
                EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH,
                EntityType.PUFFERFISH, EntityType.TADPOLE, EntityType.AXOLOTL
        );
    }
}
