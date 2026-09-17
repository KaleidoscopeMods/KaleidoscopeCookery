package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.BambooTrayRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class BambooTrayRecipeProvider extends ModRecipeProvider {
    private static final int DRYING_DURATION = 90 * 20;
    private static final int WETTING_DURATION = 45 * 20;

    public BambooTrayRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        BambooTrayRecipeBuilder.drying()
                .setIngredient(ModItems.FRESH_TEA_LEAVES.get())
                .setResult(ModItems.DRIED_TEA_LEAVES.get())
                .setDuration(DRYING_DURATION)
                .save(consumer, "fresh_tea_leaves_to_dried_tea_leaves");
        BambooTrayRecipeBuilder.drying()
                .setIngredient(Items.ROTTEN_FLESH)
                .setResult(Items.LEATHER)
                .setDuration(DRYING_DURATION)
                .save(consumer, "rotten_flesh_to_leather");
        BambooTrayRecipeBuilder.drying()
                .setIngredient(Blocks.MUD)
                .setResult(Blocks.CLAY)
                .setDuration(DRYING_DURATION)
                .save(consumer, "mud_to_clay");
        BambooTrayRecipeBuilder.drying()
                .setIngredient(Blocks.WET_SPONGE)
                .setResult(Blocks.SPONGE)
                .setDuration(DRYING_DURATION)
                .save(consumer, "wet_sponge_to_sponge");
        BambooTrayRecipeBuilder.drying()
                .setIngredient(Items.KELP)
                .setResult(Items.DRIED_KELP)
                .setDuration(DRYING_DURATION)
                .save(consumer, "kelp_to_dried_kelp");

        BambooTrayRecipeBuilder.wetting()
                .setIngredient(ModItems.DRIED_TEA_LEAVES.get())
                .setResult(ModItems.FRESH_TEA_LEAVES.get())
                .setDuration(WETTING_DURATION)
                .save(consumer, "dried_tea_leaves_to_fresh_tea_leaves");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Items.LEATHER)
                .setResult(Items.ROTTEN_FLESH)
                .setDuration(WETTING_DURATION)
                .save(consumer, "leather_to_rotten_flesh");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Blocks.CLAY)
                .setResult(Blocks.MUD)
                .setDuration(WETTING_DURATION)
                .save(consumer, "clay_to_mud");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Blocks.SPONGE)
                .setResult(Blocks.WET_SPONGE)
                .setDuration(WETTING_DURATION)
                .save(consumer, "sponge_to_wet_sponge");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Blocks.DIRT)
                .setResult(Blocks.MUD)
                .setDuration(WETTING_DURATION)
                .save(consumer, "dirt_to_mud");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Blocks.COPPER_BLOCK)
                .setResult(Blocks.OXIDIZED_COPPER)
                .setDuration(WETTING_DURATION)
                .save(consumer, "copper_block_to_oxidized_copper");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Items.DRIED_KELP)
                .setResult(Items.KELP)
                .setDuration(WETTING_DURATION)
                .save(consumer, "dried_kelp_to_kelp");

        addConcretePowderRecipe(consumer, Blocks.WHITE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE, "white");
        addConcretePowderRecipe(consumer, Blocks.ORANGE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE, "orange");
        addConcretePowderRecipe(consumer, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE, "magenta");
        addConcretePowderRecipe(consumer, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE, "light_blue");
        addConcretePowderRecipe(consumer, Blocks.YELLOW_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE, "yellow");
        addConcretePowderRecipe(consumer, Blocks.LIME_CONCRETE_POWDER, Blocks.LIME_CONCRETE, "lime");
        addConcretePowderRecipe(consumer, Blocks.PINK_CONCRETE_POWDER, Blocks.PINK_CONCRETE, "pink");
        addConcretePowderRecipe(consumer, Blocks.GRAY_CONCRETE_POWDER, Blocks.GRAY_CONCRETE, "gray");
        addConcretePowderRecipe(consumer, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE, "light_gray");
        addConcretePowderRecipe(consumer, Blocks.CYAN_CONCRETE_POWDER, Blocks.CYAN_CONCRETE, "cyan");
        addConcretePowderRecipe(consumer, Blocks.PURPLE_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE, "purple");
        addConcretePowderRecipe(consumer, Blocks.BLUE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE, "blue");
        addConcretePowderRecipe(consumer, Blocks.BROWN_CONCRETE_POWDER, Blocks.BROWN_CONCRETE, "brown");
        addConcretePowderRecipe(consumer, Blocks.GREEN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE, "green");
        addConcretePowderRecipe(consumer, Blocks.RED_CONCRETE_POWDER, Blocks.RED_CONCRETE, "red");
        addConcretePowderRecipe(consumer, Blocks.BLACK_CONCRETE_POWDER, Blocks.BLACK_CONCRETE, "black");
    }

    private static void addConcretePowderRecipe(RecipeOutput consumer, Block powder, Block concrete, String color) {
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(powder)
                .setResult(concrete)
                .setDuration(WETTING_DURATION)
                .save(consumer, color + "_concrete_powder_to_" + color + "_concrete");
    }
}
