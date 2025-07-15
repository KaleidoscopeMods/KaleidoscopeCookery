package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.ChoppingBoardBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ChoppingBoardRecipeProvider extends ModRecipeProvider {
    public ChoppingBoardRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ChoppingBoardBuilder.builder()
                .setIngredient(Items.MUTTON)
                .setResult(ModItems.RAW_LAMB_CHOPS.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("raw_lamb_chops"))
                .save(consumer);

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.TROPICAL_FISH)
                .setResult(ModItems.SASHIMI.get(), 2)
                .setCutCount(3)
                .setModelId(modLoc("sashimi"))
                .save(consumer);

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.BEEF)
                .setResult(ModItems.RAW_COW_OFFAL.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("raw_cow_offal"))
                .save(consumer);

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.PORKCHOP)
                .setResult(ModItems.RAW_PORK_BELLY.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("raw_pork_belly"))
                .save(consumer);
    }
}
