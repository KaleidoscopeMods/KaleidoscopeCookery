package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals.*;
import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.*;

public interface StockpotRecipeSchema {
    RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("result");
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT_ARRAY.key("ingredients");
    RecipeKey<String> SOUP_BASE = StringComponent.ID.key("soup_base").optional(DEFAULT_SOUP_BASE.toString());
    RecipeKey<Integer> TIME = NumberComponent.INT.key("time").optional(DEFAULT_TIME);
    RecipeKey<InputItem> CARRIER = ItemComponents.INPUT.key("carrier").optional(InputItem.of(DEFAULT_CARRIER, 1));
    RecipeKey<String> COOKING_TEXTURE = StringComponent.ID.key("cooking_texture").optional(DEFAULT_COOKING_TEXTURE.toString());
    RecipeKey<String> FINISHED_TEXTURE = StringComponent.ID.key("finished_texture").optional(DEFAULT_FINISHED_TEXTURE.toString());
    RecipeKey<Integer> COOKING_BUBBLE_COLOR = NumberComponent.INT.key("cooking_bubble_color").optional(DEFAULT_COOKING_BUBBLE_COLOR);
    RecipeKey<Integer> FINISHED_BUBBLE_COLOR = NumberComponent.INT.key("finished_bubble_color").optional(DEFAULT_FINISHED_BUBBLE_COLOR);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENTS, SOUP_BASE, TIME, CARRIER,
            COOKING_TEXTURE, FINISHED_TEXTURE, COOKING_BUBBLE_COLOR, FINISHED_BUBBLE_COLOR);
}
