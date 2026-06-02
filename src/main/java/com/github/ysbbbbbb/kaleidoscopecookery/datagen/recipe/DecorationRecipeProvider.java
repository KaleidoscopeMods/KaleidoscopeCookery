package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class DecorationRecipeProvider extends ModRecipeProvider {
    public DecorationRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        addCookStool(ModItems.COOK_STOOL_OAK, Blocks.OAK_PLANKS).save(consumer);
        addCookStool(ModItems.COOK_STOOL_SPRUCE, Blocks.SPRUCE_PLANKS).save(consumer);
        addCookStool(ModItems.COOK_STOOL_ACACIA, Blocks.ACACIA_PLANKS).save(consumer);
        addCookStool(ModItems.COOK_STOOL_BAMBOO, Blocks.BAMBOO_BLOCK).save(consumer);
        addCookStool(ModItems.COOK_STOOL_BIRCH, Blocks.BIRCH_PLANKS).save(consumer);
        addCookStool(ModItems.COOK_STOOL_CHERRY, Blocks.CHERRY_PLANKS).save(consumer);
        addCookStool(ModItems.COOK_STOOL_CRIMSON, Blocks.CRIMSON_PLANKS).save(consumer);
        addCookStool(ModItems.COOK_STOOL_DARK_OAK, Blocks.DARK_OAK_PLANKS).save(consumer);
        addCookStool(ModItems.COOK_STOOL_JUNGLE, Blocks.JUNGLE_PLANKS).save(consumer);
        addCookStool(ModItems.COOK_STOOL_MANGROVE, Blocks.MANGROVE_PLANKS).save(consumer);
        addCookStool(ModItems.COOK_STOOL_WARPED, Blocks.WARPED_PLANKS).save(consumer);

        addChair(ModItems.CHAIR_OAK, Blocks.OAK_FENCE, Blocks.OAK_SLAB).save(consumer);
        addChair(ModItems.CHAIR_SPRUCE, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_SLAB).save(consumer);
        addChair(ModItems.CHAIR_ACACIA, Blocks.ACACIA_FENCE, Blocks.ACACIA_SLAB).save(consumer);
        addChair(ModItems.CHAIR_BAMBOO, Blocks.BAMBOO_BLOCK, Blocks.BAMBOO_SLAB).save(consumer);
        addChair(ModItems.CHAIR_BIRCH, Blocks.BIRCH_FENCE, Blocks.BIRCH_SLAB).save(consumer);
        addChair(ModItems.CHAIR_CHERRY, Blocks.CHERRY_FENCE, Blocks.CHERRY_SLAB).save(consumer);
        addChair(ModItems.CHAIR_CRIMSON, Blocks.CRIMSON_FENCE, Blocks.CRIMSON_SLAB).save(consumer);
        addChair(ModItems.CHAIR_DARK_OAK, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_SLAB).save(consumer);
        addChair(ModItems.CHAIR_JUNGLE, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_SLAB).save(consumer);
        addChair(ModItems.CHAIR_MANGROVE, Blocks.MANGROVE_FENCE, Blocks.MANGROVE_SLAB).save(consumer);
        addChair(ModItems.CHAIR_WARPED, Blocks.WARPED_FENCE, Blocks.WARPED_SLAB).save(consumer);

        addTable(ModItems.TABLE_OAK, Blocks.OAK_FENCE, Blocks.OAK_SLAB).save(consumer);
        addTable(ModItems.TABLE_SPRUCE, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_SLAB).save(consumer);
        addTable(ModItems.TABLE_ACACIA, Blocks.ACACIA_FENCE, Blocks.ACACIA_SLAB).save(consumer);
        addTable(ModItems.TABLE_BAMBOO, Blocks.BAMBOO_BLOCK, Blocks.BAMBOO_SLAB).save(consumer);
        addTable(ModItems.TABLE_BIRCH, Blocks.BIRCH_FENCE, Blocks.BIRCH_SLAB).save(consumer);
        addTable(ModItems.TABLE_CHERRY, Blocks.CHERRY_FENCE, Blocks.CHERRY_SLAB).save(consumer);
        addTable(ModItems.TABLE_CRIMSON, Blocks.CRIMSON_FENCE, Blocks.CRIMSON_SLAB).save(consumer);
        addTable(ModItems.TABLE_DARK_OAK, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_SLAB).save(consumer);
        addTable(ModItems.TABLE_JUNGLE, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_SLAB).save(consumer);
        addTable(ModItems.TABLE_MANGROVE, Blocks.MANGROVE_FENCE, Blocks.MANGROVE_SLAB).save(consumer);
        addTable(ModItems.TABLE_WARPED, Blocks.WARPED_FENCE, Blocks.WARPED_SLAB).save(consumer);
    }

    private ShapedRecipeBuilder addCookStool(RegistryObject<Item> result, Block wood) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get())
                .pattern("   ")
                .pattern("###")
                .pattern("# #")
                .define('#', wood)
                .unlockedBy("has_wood", has(wood));
    }

    private ShapedRecipeBuilder addChair(RegistryObject<Item> result, Block fence, Block slab) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get())
                .pattern("F  ")
                .pattern("SSS")
                .pattern("F F")
                .define('F', fence)
                .define('S', slab)
                .unlockedBy("has_fence", has(fence));
    }

    private ShapedRecipeBuilder addTable(RegistryObject<Item> result, Block fence, Block slab) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get())
                .pattern("SSS")
                .pattern("F F")
                .define('F', fence)
                .define('S', slab)
                .unlockedBy("has_fence", has(fence));
    }
}
