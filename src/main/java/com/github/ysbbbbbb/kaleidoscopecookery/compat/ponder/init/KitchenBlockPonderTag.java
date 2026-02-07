package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class KitchenBlockPonderTag {
    public static final ResourceLocation KITCHEN_BLOCKS = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "kitchen_blocks");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        helper.registerTag(KITCHEN_BLOCKS).addToIndex()
                .item(ModItems.KITCHEN_SHOVEL.get(), true, false)
                .title("")
                .description("")
                .register();

        helper.addToTag(KITCHEN_BLOCKS)
                .add(Objects.requireNonNull(ModItems.STOCKPOT.getId()))
                .add(Objects.requireNonNull(ModItems.POT.getId()))
                .add(Objects.requireNonNull(ModItems.RECIPE_ITEM.getId()))
                .add(Objects.requireNonNull(ModItems.STEAMER.getId()))
                .add(Objects.requireNonNull(ModItems.ENAMEL_BASIN.getId()))
                .add(Objects.requireNonNull(ModItems.MILLSTONE.getId()))
                .add(Objects.requireNonNull(ModItems.SHAWARMA_SPIT.getId()));
    }
}
