package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class SimpleCookingRecipeProvider extends ModRecipeProvider {
    public SimpleCookingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        simpleCookingRecipe(ModItems.RAW_LAMB_CHOPS.get(), ModItems.COOKED_LAMB_CHOPS.get(), 0.35F, consumer);
        simpleCookingRecipe(ModItems.RAW_COW_OFFAL.get(), ModItems.COOKED_COW_OFFAL.get(), 0.35F, consumer);
        simpleCookingRecipe(ModItems.RAW_PORK_BELLY.get(), ModItems.COOKED_PORK_BELLY.get(), 0.35F, consumer);
        simpleCookingRecipe(ModItems.RAW_DONKEY_MEAT.get(), ModItems.COOKED_DONKEY_MEAT.get(), 0.35F, consumer);
        simpleCookingRecipe(ModItems.RAW_CUT_SMALL_MEATS.get(), ModItems.COOKED_CUT_SMALL_MEATS.get(), 0.35F, consumer);
        vanillaCookingRecipe(ModItems.STUFFED_DOUGH_FOOD.get(), ModItems.SAMSA.get(), 0.35F, consumer);
    }

    public void simpleCookingRecipe(ItemLike input, ItemLike output, float experience, RecipeOutput consumer) {
        vanillaCookingRecipe(input, output, experience, consumer);
        PotRecipeBuilder.builder().addInput(input).setResult(output.asItem()).save(consumer);
    }

    public void vanillaCookingRecipe(ItemLike input, ItemLike output, float experience, RecipeOutput consumer) {
        simpleCookingRecipe(consumer, "smoking", RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, 100, input, output, experience);
        simpleCookingRecipe(consumer, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, 600, input, output, experience);
        simpleCookingRecipe(consumer, "smelting", RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, 200, input, output, experience);
    }
}
