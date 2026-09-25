package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface ChoppingBoardRecipeSchema {
    RecipeKey<?> OUTPUT = ItemComponents.OUTPUT.or(ItemComponents.OUTPUT_ARRAY).key("result");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<String> MODEL_ID = StringComponent.ID.key("model_id");
    RecipeKey<Integer> CUT_COUNT = NumberComponent.INT.key("cut_count").optional(4);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENT, MODEL_ID, CUT_COUNT);
}
