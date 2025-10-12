package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface MillstoneRecipeSchema {
    RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("result");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<InputItem> CARRIER = ItemComponents.INPUT.key("carrier").optional(InputItem.EMPTY);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENT, CARRIER);
}
