package com.github.ysbbbbbb.kaleidoscopecookery.recipe.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.recipe.PotRecipe;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotRecipeSerializer implements RecipeSerializer<PotRecipe> {
    @Override
    public PotRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        int time = GsonHelper.getAsInt(json, "time", 200);
        int stirFryCount = GsonHelper.getAsInt(json, "stir_fry_count", 3);

        Ingredient carrier;
        if (json.has("carrier")) {
            carrier = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "carrier"));
        } else {
            carrier = Ingredient.EMPTY;
        }

        JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
        List<Ingredient> inputs = Lists.newArrayList();
        for (JsonElement e : ingredients) {
            inputs.add(Ingredient.fromJson(e));
        }
        ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true, true);
        return new PotRecipe(recipeId, time, stirFryCount, carrier, inputs, result);
    }

    @Override
    @Nullable
    public PotRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        int time = buf.readVarInt();
        int stirFryCount = buf.readVarInt();
        Ingredient carrier = Ingredient.fromNetwork(buf);
        int ingredientsSize = buf.readVarInt();
        List<Ingredient> inputs = Lists.newArrayList();
        for (int i = 0; i < ingredientsSize; i++) {
            inputs.add(Ingredient.fromNetwork(buf));
        }
        ItemStack result = buf.readItem();
        return new PotRecipe(recipeId, time, stirFryCount, carrier, inputs, result);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, PotRecipe recipe) {
        buf.writeVarInt(recipe.getTime());
        buf.writeVarInt(recipe.getStirFryCount());
        recipe.getCarrier().toNetwork(buf);
        buf.writeVarInt(recipe.getIngredients().size());
        recipe.getIngredients().forEach(i -> i.toNetwork(buf));
        buf.writeItem(recipe.getResult());
    }
}
