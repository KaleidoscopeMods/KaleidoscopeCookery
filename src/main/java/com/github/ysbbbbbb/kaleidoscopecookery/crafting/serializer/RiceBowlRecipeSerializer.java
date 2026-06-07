package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.RiceBowlRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class RiceBowlRecipeSerializer implements RecipeSerializer<RiceBowlRecipe> {
    @Override
    public RiceBowlRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        CraftingBookCategory category = CraftingBookCategory.CODEC.byName(
                GsonHelper.getAsString(json, "category", null),
                CraftingBookCategory.MISC
        );

        Ingredient ingredient;
        if (GsonHelper.isArrayNode(json, "ingredient")) {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
        } else {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
        }

        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        return new RiceBowlRecipe(recipeId, category, ingredient, result);
    }

    @Override
    public RiceBowlRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        ItemStack result = buf.readItem();
        return new RiceBowlRecipe(recipeId, category, ingredient, result);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, RiceBowlRecipe recipe) {
        buf.writeEnum(recipe.category());
        recipe.getIngredients().get(0).toNetwork(buf);
        buf.writeItem(recipe.getResult());
    }
}
