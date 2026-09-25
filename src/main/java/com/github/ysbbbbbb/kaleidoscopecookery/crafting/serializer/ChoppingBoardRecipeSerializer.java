package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.ArrayList;
import java.util.List;

public class ChoppingBoardRecipeSerializer implements RecipeSerializer<ChoppingBoardRecipe> {
    @Override
    public ChoppingBoardRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient;
        if (GsonHelper.isArrayNode(json, "ingredient")) {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
        } else {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
        }
        List<ItemStack> results = new ArrayList<>();
        if (GsonHelper.isArrayNode(json, "result")) {
            JsonArray resultArray = GsonHelper.getAsJsonArray(json, "result");
            resultArray.forEach(element -> results.add(CraftingHelper.getItemStack(element.getAsJsonObject(), true, true)));
        } else {
            results.add(CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true, true));
        }
        int cutCount = GsonHelper.getAsInt(json, "cut_count", 3);
        ResourceLocation modelId = new ResourceLocation(GsonHelper.getAsString(json, "model_id", ""));
        return new ChoppingBoardRecipe(recipeId, ingredient, results, cutCount, modelId);
    }

    @Override
    public ChoppingBoardRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        int resultCount = buffer.readVarInt();
        List<ItemStack> results = new ArrayList<>();
        for (int i = 0; i < resultCount; i++) {
            results.add(buffer.readItem());
        }
        int cutCount = buffer.readVarInt();
        ResourceLocation modelId = buffer.readResourceLocation();
        return new ChoppingBoardRecipe(recipeId, ingredient, results, cutCount, modelId);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ChoppingBoardRecipe recipe) {
        recipe.getIngredient().toNetwork(buffer);
        buffer.writeVarInt(recipe.getResults().size());
        recipe.getResults().forEach(buffer::writeItem);
        buffer.writeVarInt(recipe.getCutCount());
        buffer.writeResourceLocation(recipe.getModelId());
    }
}
