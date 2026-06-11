package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe.*;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util.ModKubeJSUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util.SimpleSoupBaseBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;

public class ModKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.POT_SERIALIZER.getId().getPath(), PotRecipeSchema.SCHEMA);
        registry.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.FLEX_POT_SERIALIZER.getId().getPath(), PotRecipeSchema.SCHEMA);
        registry.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.CHOPPING_BOARD_SERIALIZER.getId().getPath(), ChoppingBoardRecipeSchema.SCHEMA);
        registry.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.STOCKPOT_SERIALIZER.getId().getPath(), StockpotRecipeSchema.SCHEMA);
        registry.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.FLEX_STOCKPOT_SERIALIZER.getId().getPath(), StockpotRecipeSchema.SCHEMA);
        registry.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.MILLSTONE_SERIALIZER.getId().getPath(), MillstoneRecipeSchema.SCHEMA);
        registry.namespace(KaleidoscopeCookery.MOD_ID).register(ModRecipes.STEAMER_SERIALIZER.getId().getPath(), SteamerRecipeSchema.SCHEMA);
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("KCookery", ModKubeJSUtil.class);
        bindings.add("KCookerySoupBase", SimpleSoupBaseBuilder.class);
    }
}
