package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.SteamerBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class SteamerRecipeProvider extends ModRecipeProvider {
    public SteamerRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        SteamerBuilder.builder()
                .setIngredient(ModItems.STUFFED_DOUGH_FOOD.get())
                .setResult(ModItems.BAOZI.get())
                .save(consumer);

        SteamerBuilder.builder()
                .setIngredient(TagCommon.DOUGH)
                .setResult(ModItems.MANTOU.get())
                .save(consumer);
    }
}
