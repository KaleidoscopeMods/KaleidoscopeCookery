package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BambooTrayRecipe;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

public class BambooTrayRecipeSerializer implements RecipeSerializer<BambooTrayRecipe> {
    public static final int DEFAULT_DURATION = 60 * 20;

    @Override
    public BambooTrayRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient;
        if (GsonHelper.isArrayNode(json, "ingredient")) {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
        } else {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
        }
        ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true, true);
        BambooTrayRecipe.Subtype subtype;
        try {
            subtype = BambooTrayRecipe.Subtype.fromSerializedName(GsonHelper.getAsString(json, "subtype"));
        } catch (IllegalArgumentException exception) {
            throw new JsonSyntaxException(exception.getMessage());
        }
        int duration = GsonHelper.getAsInt(json, "duration", DEFAULT_DURATION);
        return new BambooTrayRecipe(recipeId, ingredient, result, subtype, duration);
    }

    @Override
    public BambooTrayRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        BambooTrayRecipe.Subtype subtype = BambooTrayRecipe.Subtype.fromSerializedName(buffer.readUtf());
        int duration = buffer.readVarInt();
        return new BambooTrayRecipe(recipeId, ingredient, result, subtype, duration);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, BambooTrayRecipe recipe) {
        recipe.getIngredient().toNetwork(buffer);
        buffer.writeItem(recipe.getResult());
        buffer.writeUtf(recipe.getSubtype().getSerializedName());
        buffer.writeVarInt(recipe.getDuration());
    }
}
