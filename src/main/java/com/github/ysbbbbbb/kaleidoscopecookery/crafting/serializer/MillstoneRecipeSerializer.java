package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.List;

public class MillstoneRecipeSerializer implements RecipeSerializer<MillstoneRecipe> {
    public static final ResourceLocation EMPTY_ID = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "millstone/empty");

    public static MillstoneRecipe getEmptyRecipe() {
        return new MillstoneRecipe(EMPTY_ID, Ingredient.EMPTY, NonNullList.withSize(4, RandomOutput.EMPTY));
    }

    @Override
    public MillstoneRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient;
        if (GsonHelper.isArrayNode(json, "ingredient")) {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
        } else {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
        }

        List<RandomOutput> results = new ArrayList<>();
        if (GsonHelper.isArrayNode(json, "results")) {
            JsonArray outputs = GsonHelper.getAsJsonArray(json, "results");
            for (JsonElement e : outputs) {
                results.add(RandomOutput.deserialize(e));
            }
        } else if (GsonHelper.isValidNode(json, "result")) {
            results.add(RandomOutput.deserialize(json.get("result")));
        } else {
            throw new JsonParseException("Invalid recipe format!");
        }

        return new MillstoneRecipe(recipeId, ingredient, results);
    }

    @Override
    public MillstoneRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        int outputSize = buf.readVarInt();
        List<RandomOutput> outputs = new ArrayList<>();
        for (int i = 0; i < outputSize; i++) {
            outputs.add(RandomOutput.fromNetwork(buf));
        }
        return new MillstoneRecipe(recipeId, ingredient, outputs);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, MillstoneRecipe recipe) {
        recipe.getIngredient().toNetwork(buf);
        buf.writeVarInt(recipe.results().size());
        recipe.results().forEach(o -> o.toNetwork(buf));
    }
}
