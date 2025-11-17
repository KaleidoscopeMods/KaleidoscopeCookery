package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface SteamerRecipeSchema {
    RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("result");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<Integer> COOK_TICK = NumberComponent.INT.key("cook_tick").optional(60 * 20);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENT, COOK_TICK);
}
