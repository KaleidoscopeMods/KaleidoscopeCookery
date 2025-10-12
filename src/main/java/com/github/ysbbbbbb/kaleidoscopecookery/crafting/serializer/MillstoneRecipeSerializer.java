package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class MillstoneRecipeSerializer implements RecipeSerializer<MillstoneRecipe> {
    @Override
    public MillstoneRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient;
        if (GsonHelper.isArrayNode(json, "ingredient")) {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
        } else {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
        }
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

        Ingredient carrier;
        if (json.has("carrier")) {
            carrier = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "carrier"));
        } else {
            carrier = Ingredient.EMPTY;
        }

        return new MillstoneRecipe(recipeId, ingredient, result, carrier);
    }

    @Override
    public MillstoneRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        Ingredient carrier = Ingredient.fromNetwork(buffer);
        return new MillstoneRecipe(recipeId, ingredient, result, carrier);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, MillstoneRecipe recipe) {
        recipe.getIngredient().toNetwork(buffer);
        buffer.writeItem(recipe.getResult());
        recipe.getCarrier().toNetwork(buffer);
    }
}
