package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.SteamerBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class SteamerRecipeProvider extends ModRecipeProvider {
    public SteamerRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        SteamerBuilder.builder()
                .setIngredient(ModItems.STUFFED_DOUGH_FOOD.get())
                .setResult(ModItems.BAOZI.get())
                .save(consumer);

        SteamerBuilder.builder()
                .setIngredient(TagCommon.DOUGH)
                .setResult(ModItems.MANTOU.get())
                .save(consumer);

        SteamerBuilder.builder()
                .setIngredient(Items.SLIME_BALL)
                .setResult(ModItems.QINGTUAN.get())
                .save(consumer);

        SteamerBuilder.builder()
                .setIngredient(ModItems.RAW_BAMBOO_TUBE_RICE.get())
                .setResult(ModItems.BAMBOO_TUBE_RICE.get())
                .save(consumer);
    }
}
