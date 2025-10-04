package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.SteamerRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

public class SteamerRecipeSerializer implements RecipeSerializer<SteamerRecipe> {
    @Override
    public SteamerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient;
        if (GsonHelper.isArrayNode(json, "ingredient")) {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
        } else {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
        }
        ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true, true);
        int cookTick = GsonHelper.getAsInt(json, "cook_tick", 60 * 20);
        return new SteamerRecipe(recipeId, ingredient, result, cookTick);
    }

    @Override
    public SteamerRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        int cookTick = buffer.readVarInt();
        return new SteamerRecipe(recipeId, ingredient, result, cookTick);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, SteamerRecipe recipe) {
        recipe.getIngredient().toNetwork(buffer);
        buffer.writeItem(recipe.getResult());
        buffer.writeVarInt(recipe.getCookTick());
    }
}
