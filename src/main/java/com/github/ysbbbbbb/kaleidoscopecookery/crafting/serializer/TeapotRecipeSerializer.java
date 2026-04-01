package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTeaFluids;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class TeapotRecipeSerializer implements RecipeSerializer<TeapotRecipe> {
    public static final int DEFAULT_TIME = 2400;
    public static final int DEFAULT_INGREDIENT_COUNT = 12;
    public static final ResourceLocation DEFAULT_BASE_TEA_FLUID = ModTeaFluids.WATER;
    public static final ResourceLocation DEFAULT_RESULT_TEA_FLUID = ModTeaFluids.WATER;
    public static final ResourceLocation EMPTY_ID = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "teapot/empty");

    @Override
    public TeapotRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient = Ingredient.EMPTY;
        if (json.has("ingredient")) {
            ingredient = Ingredient.fromJson(json.get("ingredient"));
        }
        int ingredientCount = GsonHelper.getAsInt(json, "ingredient_count", DEFAULT_INGREDIENT_COUNT);
        ResourceLocation baseTeaFluid = DEFAULT_BASE_TEA_FLUID;
        if (json.has("base_tea_fluid")) {
            baseTeaFluid = new ResourceLocation(GsonHelper.getAsString(json, "base_tea_fluid"));
        }
        ResourceLocation resultTeaFluid = DEFAULT_RESULT_TEA_FLUID;
        if (json.has("result_tea_fluid")) {
            resultTeaFluid = new ResourceLocation(GsonHelper.getAsString(json, "result_tea_fluid"));
        }
        int time = GsonHelper.getAsInt(json, "time", DEFAULT_TIME);
        return new TeapotRecipe(recipeId, ingredient, ingredientCount, baseTeaFluid, resultTeaFluid, time);
    }

    @Override
    public @Nullable TeapotRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        int ingredientCount = buf.readVarInt();
        ResourceLocation baseTeaFluid = buf.readResourceLocation();
        ResourceLocation resultTeaFluid = buf.readResourceLocation();
        int time = buf.readVarInt();
        return new TeapotRecipe(recipeId, ingredient, ingredientCount, baseTeaFluid, resultTeaFluid, time);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, TeapotRecipe recipe) {
        recipe.ingredient().toNetwork(buf);
        buf.writeVarInt(recipe.ingredientCount());
        buf.writeResourceLocation(recipe.baseTeaFluid());
        buf.writeResourceLocation(recipe.resultTeaFluid());
        buf.writeVarInt(recipe.time());
    }
}
