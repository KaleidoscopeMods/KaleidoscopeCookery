package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BambooTrayRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.BambooTrayRecipeSerializer;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface BambooTrayRecipeSchema {
    StringComponent SUBTYPE_COMPONENT = new StringComponent("must be 'wetting' or 'drying'", subtype -> {
        try {
            BambooTrayRecipe.Subtype.fromSerializedName(subtype);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    });

    RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("result");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<String> SUBTYPE = SUBTYPE_COMPONENT.key("subtype");
    RecipeKey<Integer> DURATION = NumberComponent.INT.key("duration").optional(BambooTrayRecipeSerializer.DEFAULT_DURATION);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENT, SUBTYPE, DURATION);
}
