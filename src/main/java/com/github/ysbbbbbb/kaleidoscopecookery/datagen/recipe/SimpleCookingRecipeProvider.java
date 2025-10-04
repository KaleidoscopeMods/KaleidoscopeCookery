package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class SimpleCookingRecipeProvider extends ModRecipeProvider {
    public SimpleCookingRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        simpleCookingRecipe(ModItems.RAW_LAMB_CHOPS.get(), ModItems.COOKED_LAMB_CHOPS.get(), 0.35F, consumer);
        simpleCookingRecipe(ModItems.RAW_COW_OFFAL.get(), ModItems.COOKED_COW_OFFAL.get(), 0.35F, consumer);
        simpleCookingRecipe(ModItems.RAW_PORK_BELLY.get(), ModItems.COOKED_PORK_BELLY.get(), 0.35F, consumer);
        simpleCookingRecipe(ModItems.RAW_DONKEY_MEAT.get(), ModItems.COOKED_DONKEY_MEAT.get(), 0.35F, consumer);
        vanillaCookingRecipe(ModItems.STUFFED_DOUGH_FOOD.get(), ModItems.SAMSA.get(), 0.35F, consumer);
    }

    public void simpleCookingRecipe(ItemLike input, ItemLike output, float experience, Consumer<FinishedRecipe> consumer) {
        vanillaCookingRecipe(input, output, experience, consumer);
        PotRecipeBuilder.builder().addInput(input).setResult(output.asItem()).save(consumer);
    }

    public void vanillaCookingRecipe(ItemLike input, ItemLike output, float experience, Consumer<FinishedRecipe> consumer) {
        simpleCookingRecipe(consumer, "smoking", RecipeSerializer.SMOKING_RECIPE, 100, input, output, experience);
        simpleCookingRecipe(consumer, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, 600, input, output, experience);
        simpleCookingRecipe(consumer, "smelting", RecipeSerializer.SMELTING_RECIPE, 200, input, output, experience);
    }
}
