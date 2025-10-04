package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class SimplePotRecipeProvider extends ModRecipeProvider {
    public SimplePotRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
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

        addSingleItemRecipe(TagCommon.EGGS, ModItems.FRIED_EGG.get(), "egg", consumer);
        addSingleItemRecipe(ModItems.STUFFED_DOUGH_FOOD.get(), ModItems.MEAT_PIE.get(), "stuffed_dough_food", consumer);
    }

    public void addSingleItemRecipe(TagKey<Item> inputItem, Item outputItem, String idInput, Consumer<FinishedRecipe> consumer) {
        this.addSingleItemRecipe(inputItem, outputItem, idInput, Ingredient.EMPTY, consumer);
    }

    public void addSingleItemRecipe(ItemLike inputItem, Item outputItem, String idInput, Consumer<FinishedRecipe> consumer) {
        this.addSingleItemRecipe(inputItem, outputItem, idInput, Ingredient.EMPTY, consumer);
    }

    @SuppressWarnings("all")
    public void addSingleItemRecipe(TagKey<Item> inputItem, Item outputItem, String idInput, Ingredient carrier, Consumer<FinishedRecipe> consumer) {
        for (int i = 1; i <= 9; i++) {
            TagKey<Item>[] inputs = this.getItemsWithCount(inputItem, i);
            ItemStack output = new ItemStack(outputItem, i);
            String idOutput = this.getRecipeIdWithCount(outputItem, i);
            String id = String.format("%s_to_%s", idInput, idOutput);
            PotRecipeBuilder.builder().addInput(inputs).setResult(output).setCarrier(carrier).save(consumer, id);
        }
    }

    @SuppressWarnings("all")
    public void addSingleItemRecipe(ItemLike inputItem, Item outputItem, String idInput, Ingredient carrier, Consumer<FinishedRecipe> consumer) {
        for (int i = 1; i <= 9; i++) {
            ItemLike[] inputs = this.getItemsWithCount(inputItem, i);
            ItemStack output = new ItemStack(outputItem, i);
            String idOutput = this.getRecipeIdWithCount(outputItem, i);
            String id = String.format("%s_to_%s", idInput, idOutput);
            PotRecipeBuilder.builder().addInput(inputs).setResult(output).setCarrier(carrier).save(consumer, id);
        }
    }
}
