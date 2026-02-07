package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.scenes.*;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class KitchenBlockPonderScreen {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot"))
                .addStoryBoard("stockpot/stockpot_introduction", StockpotScenes::introduction);

        helper.forComponents(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pot"))
                .addStoryBoard("pot/pot_introduction", PotScenes::introduction);

        helper.forComponents(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "steamer"))
                .addStoryBoard("steamer/steamer_introduction", SteamerScenes::introduction)
                .addStoryBoard("steamer/steamer_introduction", SteamerScenes::cooking);

        helper.forComponents(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "enamel_basin"))
                .addStoryBoard("enamel_basin/enamel_basin_introduction", EnamelBasinScenes::introduction);

        helper.forComponents(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "millstone"))
                .addStoryBoard("millstone/millstone_introduction", MillstoneScenes::introduction);

        helper.forComponents(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "shawarma_spit"))
                .addStoryBoard("shawarma_spit/shawarma_spit_introduction", ShawarmaSpitScenes::introduction);

        helper.forComponents(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "recipe_item"))
                .addStoryBoard("stockpot/stockpot_introduction", RecipeItemScenes::introduction);
    }
}
