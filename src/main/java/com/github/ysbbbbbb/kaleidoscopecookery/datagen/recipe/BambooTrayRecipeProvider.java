package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.BambooTrayRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class BambooTrayRecipeProvider extends ModRecipeProvider {
    private static final int TWO_MINUTES = 2 * 60 * 20;

    public BambooTrayRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        BambooTrayRecipeBuilder.drying()
                .setIngredient(ModItems.FRESH_TEA_LEAVES.get())
                .setResult(ModItems.DRIED_TEA_LEAVES.get())
                .setDuration(TWO_MINUTES)
                .save(consumer, "fresh_tea_leaves_to_dried_tea_leaves");
        BambooTrayRecipeBuilder.drying()
                .setIngredient(Items.ROTTEN_FLESH)
                .setResult(Items.LEATHER)
                .setDuration(TWO_MINUTES)
                .save(consumer, "rotten_flesh_to_leather");
        BambooTrayRecipeBuilder.drying()
                .setIngredient(Blocks.MUD)
                .setResult(Blocks.PACKED_MUD)
                .setDuration(TWO_MINUTES)
                .save(consumer, "mud_to_packed_mud");
        BambooTrayRecipeBuilder.drying()
                .setIngredient(Blocks.WET_SPONGE)
                .setResult(Blocks.SPONGE)
                .setDuration(TWO_MINUTES)
                .save(consumer, "wet_sponge_to_sponge");

        BambooTrayRecipeBuilder.wetting()
                .setIngredient(ModItems.DRIED_TEA_LEAVES.get())
                .setResult(ModItems.FRESH_TEA_LEAVES.get())
                .setDuration(TWO_MINUTES)
                .save(consumer, "dried_tea_leaves_to_fresh_tea_leaves");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Items.LEATHER)
                .setResult(Items.ROTTEN_FLESH)
                .setDuration(TWO_MINUTES)
                .save(consumer, "leather_to_rotten_flesh");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Blocks.PACKED_MUD)
                .setResult(Blocks.MUD)
                .setDuration(TWO_MINUTES)
                .save(consumer, "packed_mud_to_mud");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Blocks.SPONGE)
                .setResult(Blocks.WET_SPONGE)
                .setDuration(TWO_MINUTES)
                .save(consumer, "sponge_to_wet_sponge");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Blocks.DIRT)
                .setResult(Blocks.MUD)
                .setDuration(TWO_MINUTES)
                .save(consumer, "dirt_to_mud");
        BambooTrayRecipeBuilder.wetting()
                .setIngredient(Blocks.COPPER_BLOCK)
                .setResult(Blocks.OXIDIZED_COPPER)
                .setDuration(TWO_MINUTES)
                .save(consumer, "copper_block_to_oxidized_copper");
    }
}
