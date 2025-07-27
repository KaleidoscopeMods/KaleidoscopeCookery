package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.ChoppingBoardRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.PotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {
    public static final RecipeSerializer<PotRecipe> POT_SERIALIZER = new PotRecipeSerializer();
    public static final RecipeSerializer<ChoppingBoardRecipe> CHOPPING_BOARD_SERIALIZER = new ChoppingBoardRecipeSerializer();
    public static final RecipeSerializer<StockpotRecipe> STOCKPOT_SERIALIZER = new StockpotRecipeSerializer();

    public static final RecipeType<PotRecipe> POT_RECIPE = simple(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pot"));
    public static final RecipeType<ChoppingBoardRecipe> CHOPPING_BOARD_RECIPE = simple(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chopping_board"));
    public static final RecipeType<StockpotRecipe> STOCKPOT_RECIPE = simple(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot"));

    public static void registerRecipes() {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pot"), POT_SERIALIZER);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chopping_board"), CHOPPING_BOARD_SERIALIZER);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot"), STOCKPOT_SERIALIZER);

        Registry.register(BuiltInRegistries.RECIPE_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pot"), POT_RECIPE);
        Registry.register(BuiltInRegistries.RECIPE_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chopping_board"), CHOPPING_BOARD_RECIPE);
        Registry.register(BuiltInRegistries.RECIPE_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot"), STOCKPOT_RECIPE);
    }

    private static <T extends Recipe<?>> RecipeType<T> simple(final ResourceLocation id) {
        return new RecipeType<>() {
            @Override
            public String toString() {
                return id.toString();
            }
        };
    }
}
