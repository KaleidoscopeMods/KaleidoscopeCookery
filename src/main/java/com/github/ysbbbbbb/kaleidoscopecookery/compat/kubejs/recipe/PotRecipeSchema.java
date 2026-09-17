package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface PotRecipeSchema {
    RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("result");
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT_ARRAY.key("ingredients");
    RecipeKey<InputItem> CARRIER = ItemComponents.INPUT.key("carrier").optional(InputItem.EMPTY).allowEmpty();
    RecipeKey<Integer> TIME = NumberComponent.INT.key("time").optional(200);
    RecipeKey<Integer> STIR_FRY_COUNT = NumberComponent.INT.key("stir_fry_count").optional(3);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENTS, CARRIER, TIME, STIR_FRY_COUNT);
}
