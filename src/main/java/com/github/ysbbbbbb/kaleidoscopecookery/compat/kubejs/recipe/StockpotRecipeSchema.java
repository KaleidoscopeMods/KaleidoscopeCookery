package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util.IngredientListComponent;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.*;

public interface StockpotRecipeSchema {
    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK.outputKey("result");
    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientListComponent.TYPE.inputKey("ingredients");
    RecipeKey<String> SOUP_BASE = StringComponent.ID.inputKey("soup_base").optional(DEFAULT_SOUP_BASE.toString());
    RecipeKey<Integer> TIME = NumberComponent.INT.otherKey("time").optional(DEFAULT_TIME);
    RecipeKey<Ingredient> CARRIER = IngredientComponent.INGREDIENT.inputKey("carrier").optional(DEFAULT_CARRIER);
    RecipeKey<String> COOKING_TEXTURE = StringComponent.ID.otherKey("cooking_texture").optional(DEFAULT_COOKING_TEXTURE.toString());
    RecipeKey<String> FINISHED_TEXTURE = StringComponent.ID.otherKey("finished_texture").optional(DEFAULT_FINISHED_TEXTURE.toString());
    RecipeKey<Integer> COOKING_BUBBLE_COLOR = NumberComponent.INT.otherKey("cooking_bubble_color").optional(DEFAULT_COOKING_BUBBLE_COLOR);
    RecipeKey<Integer> FINISHED_BUBBLE_COLOR = NumberComponent.INT.otherKey("finished_bubble_color").optional(DEFAULT_FINISHED_BUBBLE_COLOR);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENTS, SOUP_BASE, TIME, CARRIER,
            COOKING_TEXTURE, FINISHED_TEXTURE, COOKING_BUBBLE_COLOR, FINISHED_BUBBLE_COLOR);
}
