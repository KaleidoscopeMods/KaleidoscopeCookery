package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public class TeapotRecipeSerializer implements RecipeSerializer<TeapotRecipe> {
    public static final int DEFAULT_TIME = 2400;
    public static final int DEFAULT_INGREDIENT_COUNT = 12;

    public static final ResourceLocation EMPTY_TEA_FLUID = new ResourceLocation("empty");

    @Override
    public TeapotRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient = Ingredient.EMPTY;
        ResourceLocation baseTeaFluid = EMPTY_TEA_FLUID;
        if (json.has("tea_fluid")) {
            baseTeaFluid = new ResourceLocation(GsonHelper.getAsString(json, "tea_fluid"));
        }
        if (json.has("ingredient")) {
            ingredient = Ingredient.fromJson(json.get("ingredient"));
        }
        int ingredientCount = GsonHelper.getAsInt(json, "ingredient_count", DEFAULT_INGREDIENT_COUNT);
        int time = GsonHelper.getAsInt(json, "time", DEFAULT_TIME);
        ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true, true);
        return new TeapotRecipe(recipeId, baseTeaFluid, ingredient, ingredientCount, time, result);
    }

    @Override
    public @Nullable TeapotRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        ResourceLocation baseTeaFluid = buf.readResourceLocation();
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        int ingredientCount = buf.readVarInt();
        int time = buf.readVarInt();
        ItemStack result = buf.readItem();
        return new TeapotRecipe(recipeId, baseTeaFluid, ingredient, ingredientCount, time, result);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, TeapotRecipe recipe) {
        buf.writeResourceLocation(recipe.teaFluid());
        recipe.ingredient().toNetwork(buf);
        buf.writeVarInt(recipe.ingredientCount());
        buf.writeVarInt(recipe.time());
        buf.writeItem(recipe.result());
    }
}