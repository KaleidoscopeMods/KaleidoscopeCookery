package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChoppingBoardBuilder implements RecipeBuilder {
    private static final String NAME = "chopping_board";

    private Ingredient ingredient = Ingredient.EMPTY;
    private List<ItemStack> results = List.of();
    private int cutCount = 3;
    private ResourceLocation modelId;

    public static ChoppingBoardBuilder builder() {
        return new ChoppingBoardBuilder();
    }

    public ChoppingBoardBuilder setIngredient(ItemLike itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public ChoppingBoardBuilder setIngredient(TagKey<Item> itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public ChoppingBoardBuilder setResult(ItemStack stack) {
        this.results = List.of(stack);
        return this;
    }

    public ChoppingBoardBuilder setResult(ItemLike itemLike) {
        this.results = List.of(new ItemStack(itemLike));
        return this;
    }

    public ChoppingBoardBuilder setResult(ItemLike itemLike, int count) {
        this.results = List.of(new ItemStack(itemLike, count));
        return this;
    }

    public ChoppingBoardBuilder setResults(ItemStack... stacks) {
        this.results = List.of(stacks);
        return this;
    }

    public ChoppingBoardBuilder setCutCount(int cutCount) {
        this.cutCount = Math.max(cutCount, 1);
        return this;
    }

    public ChoppingBoardBuilder setModelId(ResourceLocation modelId) {
        this.modelId = modelId;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.results.getFirst().getItem();
    }

    @Override
    public void save(RecipeOutput output) {
        String path = RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath();
        ResourceLocation filePath = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, NAME + "/" + path);
        this.save(output, filePath);
    }

    @Override
    public void save(RecipeOutput output, String recipeId) {
        ResourceLocation filePath = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, NAME + "/" + recipeId);
        this.save(output, filePath);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        ChoppingBoardRecipe recipe = new ChoppingBoardRecipe(this.ingredient, this.results, this.cutCount, this.modelId);
        recipeOutput.accept(id, recipe, null);
    }
}
