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
                .setIngredient(Items.COOKED_MUTTON)
                .setResult(ModItems.COOKED_LAMB_CHOPS.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("cooked_lamb_chops"))
                .save(consumer);

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.TROPICAL_FISH)
                .setResult(ModItems.SASHIMI.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("sashimi"))
                .save(consumer, "sashimi_from_tropical_fish");

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.COD)
                .setResult(ModItems.SASHIMI.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("cod"))
                .save(consumer, "sashimi_from_cod");

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.COOKED_COD)
                .setResult(Items.BONE, 2)
                .setCutCount(4)
                .setModelId(modLoc("cooked_cod"))
                .save(consumer, "bone_from_cod");

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.SALMON)
                .setResult(ModItems.SASHIMI.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("salmon"))
                .save(consumer, "sashimi_from_salmon");

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.COOKED_SALMON)
                .setResult(Items.BONE, 2)
                .setCutCount(4)
                .setModelId(modLoc("cooked_salmon"))
                .save(consumer, "bone_from_salmon");

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.BEEF)
                .setResult(ModItems.RAW_COW_OFFAL.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("raw_cow_offal"))
                .save(consumer);

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.COOKED_BEEF)
                .setResult(ModItems.COOKED_COW_OFFAL.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("cooked_cow_offal"))
                .save(consumer);

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.PORKCHOP)
                .setResult(ModItems.RAW_PORK_BELLY.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("raw_pork_belly"))
                .save(consumer);

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.COOKED_PORKCHOP)
                .setResult(ModItems.COOKED_PORK_BELLY.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("cooked_pork_belly"))
                .save(consumer);

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.CHICKEN)
                .setResult(ModItems.RAW_CUT_SMALL_MEATS.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("raw_chicken"))
                .save(consumer, "raw_cut_small_meats_from_chicken");

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.COOKED_CHICKEN)
                .setResult(ModItems.COOKED_CUT_SMALL_MEATS.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("cooked_chicken"))
                .save(consumer, "cooked_cut_small_meats_from_chicken");

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.RABBIT)
                .setResult(ModItems.RAW_CUT_SMALL_MEATS.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("raw_rabbit"))
                .save(consumer, "raw_cut_small_meats_from_rabbit");

        ChoppingBoardBuilder.builder()
                .setIngredient(Items.COOKED_RABBIT)
                .setResult(ModItems.COOKED_CUT_SMALL_MEATS.get(), 2)
                .setCutCount(4)
                .setModelId(modLoc("cooked_rabbit"))
                .save(consumer, "cooked_cut_small_meats_from_rabbit");
    }
}
