package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;


import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface ChoppingBoardRecipeSchema {
    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK.outputKey("result");
    RecipeKey<Ingredient> INGREDIENT = IngredientComponent.INGREDIENT.inputKey("ingredient");
    RecipeKey<String> MODEL_ID = StringComponent.ID.otherKey("model_id");
    RecipeKey<Integer> CUT_COUNT = NumberComponent.INT.otherKey("cut_count").optional(4);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENT, MODEL_ID, CUT_COUNT);
}
