package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe.*;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util.ModKubeJSUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util.SimpleSoupBaseBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class ModKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.POT_SERIALIZER.getId().getPath(), PotRecipeSchema.SCHEMA);
        event.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.CHOPPING_BOARD_SERIALIZER.getId().getPath(), ChoppingBoardRecipeSchema.SCHEMA);
        event.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.STOCKPOT_SERIALIZER.getId().getPath(), StockpotRecipeSchema.SCHEMA);
        event.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.MILLSTONE_SERIALIZER.getId().getPath(), MillstoneRecipeSchema.SCHEMA);
        event.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.STEAMER_SERIALIZER.getId().getPath(), SteamerRecipeSchema.SCHEMA);
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("KCookery", ModKubeJSUtil.class);
        event.add("KCookerySoupBase", SimpleSoupBaseBuilder.class);
    }
}
