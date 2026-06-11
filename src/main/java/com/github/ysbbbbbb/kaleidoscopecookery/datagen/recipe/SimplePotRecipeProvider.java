package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems.*;

public class SimplePotRecipeProvider extends ModRecipeProvider {
    public SimplePotRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        addSingleItemRecipe(Items.POTATO, Items.BAKED_POTATO, "potato", consumer);
        addSingleItemRecipe(Items.KELP, Items.DRIED_KELP, "kelp", consumer);
        addSingleItemRecipe(Items.CHORUS_FRUIT, Items.POPPED_CHORUS_FRUIT, "chorus_fruit", consumer);

        addSingleItemRecipe(Tags.Items.EGGS, FRIED_EGG.get(), "egg", consumer);

        addSingleItemRecipe(Items.BEEF, Items.COOKED_BEEF, "beef", consumer);
        addSingleItemRecipe(Items.CHICKEN, Items.COOKED_CHICKEN, "chicken", consumer);
        addSingleItemRecipe(Items.COD, Items.COOKED_COD, "cod", consumer);
        addSingleItemRecipe(Items.SALMON, Items.COOKED_SALMON, "salmon", consumer);
        addSingleItemRecipe(Items.MUTTON, Items.COOKED_MUTTON, "mutton", consumer);
        addSingleItemRecipe(Items.PORKCHOP, Items.COOKED_PORKCHOP, "porkchop", consumer);
        addSingleItemRecipe(Items.RABBIT, Items.COOKED_RABBIT, "rabbit", consumer);

        addSingleItemRecipe(RAW_LAMB_CHOPS.get(), COOKED_LAMB_CHOPS.get(), "raw_lamb_chops", consumer);
        addSingleItemRecipe(RAW_COW_OFFAL.get(), COOKED_COW_OFFAL.get(), "raw_cow_offal", consumer);
        addSingleItemRecipe(RAW_PORK_BELLY.get(), COOKED_PORK_BELLY.get(), "raw_pork_belly", consumer);
        addSingleItemRecipe(RAW_CUT_SMALL_MEATS.get(), COOKED_CUT_SMALL_MEATS.get(), "raw_cut_small_meats", consumer);

        addSingleItemRecipe(RAW_MEATBALL.get(), COOKED_MEATBALL.get(), "raw_meatball", consumer);
        addSingleItemRecipe(STUFFED_DOUGH_FOOD.get(), MEAT_PIE.get(), "stuffed_dough_food", consumer);

        addSingleItemRecipe(COOKED_RICE.get(), STICKY_RICE_CAKE.get(), "cooked_rice", consumer);
    }

    public void addSingleItemRecipe(TagKey<Item> inputItem, Item outputItem, String idInput, RecipeOutput consumer) {
        this.addSingleItemRecipe(inputItem, outputItem, idInput, Ingredient.EMPTY, consumer);
    }

    public void addSingleItemRecipe(ItemLike inputItem, Item outputItem, String idInput, RecipeOutput consumer) {
        this.addSingleItemRecipe(inputItem, outputItem, idInput, Ingredient.EMPTY, consumer);
    }

    public void addSingleItemRecipe(TagKey<Item> inputItem, Item outputItem, String idInput, Ingredient carrier, RecipeOutput consumer) {
        for (int i = 1; i <= 9; i++) {
            TagKey<Item>[] inputs = this.getItemsWithCount(inputItem, i);
            ItemStack output = new ItemStack(outputItem, i);
            String idOutput = this.getRecipeIdWithCount(outputItem, i);
            String id = String.format("%s_to_%s", idInput, idOutput);
            PotRecipeBuilder.builder().addInput((Object[]) inputs).setResult(output).setCarrier(carrier).save(consumer, id);
        }
    }

    public void addSingleItemRecipe(ItemLike inputItem, Item outputItem, String idInput, Ingredient carrier, RecipeOutput consumer) {
        for (int i = 1; i <= 9; i++) {
            ItemLike[] inputs = this.getItemsWithCount(inputItem, i);
            ItemStack output = new ItemStack(outputItem, i);
            String idOutput = this.getRecipeIdWithCount(outputItem, i);
            String id = String.format("%s_to_%s", idInput, idOutput);
            PotRecipeBuilder.builder().addInput((Object[]) inputs).setResult(output).setCarrier(carrier).save(consumer, id);
        }
    }
}
