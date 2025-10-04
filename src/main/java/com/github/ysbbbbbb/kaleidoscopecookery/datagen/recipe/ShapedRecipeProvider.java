package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class ShapedRecipeProvider extends ModRecipeProvider {
    public ShapedRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.STOVE.get())
                .pattern("###")
                .pattern("#F#")
                .pattern("###")
                .define('#', Tags.Items.COBBLESTONE)
                .define('F', Items.CAMPFIRE)
                .unlockedBy("has_campfire", has(Items.CAMPFIRE))
                .save(consumer, "kaleidoscope_cookery:stove_campfire");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.STOVE.get())
                .pattern("###")
                .pattern("#F#")
                .pattern("###")
                .define('#', Tags.Items.COBBLESTONE)
                .define('F', Items.SOUL_CAMPFIRE)
                .unlockedBy("has_soul_campfire", has(Items.SOUL_CAMPFIRE))
                .save(consumer, "kaleidoscope_cookery:stove_soul_campfire");

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.FRUIT_BASKET.get())
                .pattern(" S ")
                .pattern("#C#")
                .pattern("###")
                .define('S', Items.STICK)
                .define('#', ItemTags.PLANKS)
                .define('C', Items.CHEST)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.SCARECROW.get())
                .pattern(" H ")
                .pattern("SPS")
                .pattern(" # ")
                .define('H', TagMod.STRAW_HAT)
                .define('S', Items.STICK)
                .define('P', Items.PUMPKIN)
                .define('#', TagMod.STRAW_BALE)
                .unlockedBy("has_pumpkin", has(Items.PUMPKIN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.POT.get())
                .pattern("###")
                .pattern("###")
                .pattern(" # ")
                .define('#', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.IRON_KITCHEN_KNIFE.get())
                .pattern("##")
                .pattern("#S")
                .define('#', Tags.Items.INGOTS_IRON)
                .define('S', Items.STICK)
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.GOLD_KITCHEN_KNIFE.get())
                .pattern("##")
                .pattern("#S")
                .define('#', Tags.Items.INGOTS_GOLD)
                .define('S', Items.STICK)
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.DIAMOND_KITCHEN_KNIFE.get())
                .pattern("##")
                .pattern("#S")
                .define('#', Tags.Items.GEMS_DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.KITCHEN_SHOVEL.get())
                .pattern("I  ")
                .pattern(" N ")
                .pattern("  S")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('N', Tags.Items.NUGGETS_IRON)
                .define('S', Items.STICK)
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STRAW_HAT.get())
                .pattern(" W ")
                .pattern(" S ")
                .pattern("WWW")
                .define('W', Items.WHEAT)
                .define('S', Items.STRING)
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STRAW_HAT_FLOWER.get())
                .pattern("FFF")
                .pattern("FHF")
                .pattern("FFF")
                .define('F', ItemTags.FLOWERS)
                .define('H', ModItems.STRAW_HAT.get())
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.OIL_BLOCK.get())
                .pattern("OOO")
                .pattern("OOO")
                .pattern("OOO")
                .define('O', ModItems.OIL.get())
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CHOPPING_BOARD.get())
                .pattern("PPP")
                .pattern("PPP")
                .define('P', ItemTags.WOODEN_PRESSURE_PLATES)
                .unlockedBy("has_wood", has(Items.OAK_PLANKS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.STOCKPOT.get())
                .pattern("B B")
                .pattern("I I")
                .pattern("III")
                .define('B', Items.BRICK)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.STOCKPOT_LID.get())
                .pattern(" B ")
                .pattern("III")
                .define('B', Items.BRICK)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.ENAMEL_BASIN.get())
                .pattern("OOO")
                .pattern("OOO")
                .pattern(" B ")
                .define('O', TagMod.OIL)
                .define('B', Items.BUCKET)
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.KITCHENWARE_RACKS.get())
                .pattern("SSS")
                .pattern("INI")
                .define('S', Items.STICK)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CHILI_RISTRA.get())
                .pattern("CC ")
                .pattern("CC ")
                .pattern("CC ")
                .define('C', ModItems.RED_CHILI.get())
                .unlockedBy("has_red_chili", has(ModItems.RED_CHILI.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.STRAW_BLOCK.get())
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .define('R', ModItems.RICE_PANICLE.get())
                .unlockedBy("has_rice_panicle", has(ModItems.RICE_PANICLE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.FARMER_CHEST_PLATE.get())
                .pattern("I I")
                .pattern("LLL")
                .pattern("LLL")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('L', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.FARMER_LEGGINGS.get())
                .pattern("LIL")
                .pattern("L L")
                .pattern("L L")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('L', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.FARMER_BOOTS.get())
                .pattern("I I")
                .pattern("L L")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('L', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.SHAWARMA_SPIT.get())
                .pattern("ICI")
                .pattern("ICI")
                .define('I', Items.CHAIN)
                .define('C', Items.CAMPFIRE)
                .unlockedBy("has_campfire", has(Items.CAMPFIRE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.MILLSTONE.get())
                .pattern(" F ")
                .pattern("SG ")
                .pattern("TTT")
                .define('F', Tags.Items.FENCES_WOODEN)
                .define('S', Items.STICK)
                .define('G', Items.GRINDSTONE)
                .define('T', Items.SMOOTH_STONE)
                .unlockedBy("has_smooth_stone", has(Items.SMOOTH_STONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.RECIPE_ITEM.get())
                .pattern("PP")
                .pattern("PP")
                .define('P', Items.PAPER)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STEAMER.get())
                .pattern("TTT")
                .pattern("BBB")
                .define('T', Items.BAMBOO_TRAPDOOR)
                .define('B', Items.BAMBOO_BLOCK)
                .unlockedBy("has_bamboo", has(Items.BAMBOO))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.TRANSMUTATION_LUNCH_BAG.get())
                .pattern(" L ")
                .pattern("LSL")
                .pattern("LLL")
                .define('L', Items.LEATHER)
                .define('S', Items.NETHER_STAR)
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.OIL_POT.get())
                .pattern("P ")
                .pattern("BS")
                .define('P', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .define('B', Items.BUCKET)
                .define('S', Items.STICK)
                .unlockedBy("has_bucket", has(Items.BUCKET))
                .save(consumer);
    }
}
