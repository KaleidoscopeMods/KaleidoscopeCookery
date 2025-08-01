package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ShapelessRecipeProvider extends ModRecipeProvider {
    public ShapelessRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.RICE_PANICLE.get(), 9)
                .requires(ModItems.STRAW_BLOCK.get())
                .unlockedBy("has_rice_panicle", has(ModItems.RICE_PANICLE.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.OIL.get(), 9)
                .requires(ModItems.OIL_BLOCK.get())
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RICE_SEED.get(), 3)
                .requires(ModItems.RICE_PANICLE.get())
                .unlockedBy("has_rice_panicle", has(ModItems.RICE_PANICLE.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHILI_SEED.get(), 1)
                .requires(ModItems.GREEN_CHILI.get())
                .unlockedBy("has_chili", has(ModItems.GREEN_CHILI.get()))
                .save(consumer, "chili_seed_from_green_chili");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHILI_SEED.get(), 1)
                .requires(ModItems.RED_CHILI.get())
                .unlockedBy("has_chili", has(ModItems.RED_CHILI.get()))
                .save(consumer, "chili_seed_from_red_chili");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.TOMATO_SEED.get(), 1)
                .requires(ModItems.TOMATO.get())
                .unlockedBy("has_tomato", has(ModItems.TOMATO.get()))
                .save(consumer);
    }
}
