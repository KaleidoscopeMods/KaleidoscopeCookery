package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util.IngredientListComponent;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface PotRecipeSchema {
    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK.outputKey("result");
    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientListComponent.TYPE.inputKey("ingredients");
    RecipeKey<Ingredient> CARRIER = IngredientComponent.INGREDIENT.inputKey("carrier").optional(Ingredient.EMPTY);
    RecipeKey<Integer> TIME = NumberComponent.INT.otherKey("time").optional(200);
    RecipeKey<Integer> STIR_FRY_COUNT = NumberComponent.INT.otherKey("stir_fry_count").optional(3);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENTS, CARRIER, TIME, STIR_FRY_COUNT);
}
