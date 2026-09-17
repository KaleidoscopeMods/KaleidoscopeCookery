package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.BambooTrayRecipeSerializer;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface BambooTrayRecipeSchema {
    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK.outputKey("result");
    RecipeKey<Ingredient> INGREDIENT = IngredientComponent.INGREDIENT.inputKey("ingredient");
    RecipeKey<String> SUBTYPE = StringComponent.STRING.otherKey("subtype");
    RecipeKey<Integer> DURATION = NumberComponent.INT.otherKey("duration").optional(BambooTrayRecipeSerializer.DEFAULT_DURATION);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENT, SUBTYPE, DURATION);
}
