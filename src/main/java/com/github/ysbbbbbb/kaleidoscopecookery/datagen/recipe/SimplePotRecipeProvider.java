package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class SimplePotRecipeProvider extends ModRecipeProvider {
    public SimplePotRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        PotRecipeBuilder.builder().addInput(Items.POTATO).setResult(Items.BAKED_POTATO).save(consumer);
        PotRecipeBuilder.builder().addInput(Items.KELP).setResult(Items.DRIED_KELP).save(consumer);
        PotRecipeBuilder.builder().addInput(Items.CHORUS_FRUIT).setResult(Items.POPPED_CHORUS_FRUIT).save(consumer);
        PotRecipeBuilder.builder().addInput(Items.BEEF).setResult(Items.COOKED_BEEF).save(consumer);
        PotRecipeBuilder.builder().addInput(Items.CHICKEN).setResult(Items.COOKED_CHICKEN).save(consumer);
        PotRecipeBuilder.builder().addInput(Items.COD).setResult(Items.COOKED_COD).save(consumer);
        PotRecipeBuilder.builder().addInput(Items.SALMON).setResult(Items.COOKED_SALMON).save(consumer);
        PotRecipeBuilder.builder().addInput(Items.MUTTON).setResult(Items.COOKED_MUTTON).save(consumer);
        PotRecipeBuilder.builder().addInput(Items.PORKCHOP).setResult(Items.COOKED_PORKCHOP).save(consumer);
        PotRecipeBuilder.builder().addInput(Items.RABBIT).setResult(Items.COOKED_RABBIT).save(consumer);

        addSingleItemRecipe(Items.EGG, ModItems.FRIED_EGG, "egg", consumer);
    }

    public void addSingleItemRecipe(ItemLike inputItem, Item outputItem, String idInput, RecipeOutput consumer) {
        this.addSingleItemRecipe(inputItem, outputItem, idInput, Ingredient.EMPTY, consumer);
    }

    public void addSingleItemRecipe(ItemLike inputItem, Item outputItem, String idInput, Ingredient carrier, RecipeOutput consumer) {
        for (int i = 1; i <= 9; i++) {
            ItemLike[] inputs = this.getItemsWithCount(inputItem, i);
            ItemStack output = new ItemStack(outputItem, i);
            String idOutput = this.getRecipeIdWithCount(outputItem, i);
            String id = String.format("%s_to_%s", idInput, idOutput);
            PotRecipeBuilder.builder().addInput((Object) inputs).setResult(output).setCarrier(carrier).save(consumer, id);
        }
    }
}
