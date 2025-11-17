package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
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

public class MillstoneRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "millstone";

    private Ingredient ingredient = Ingredient.EMPTY;
    private ItemStack result = ItemStack.EMPTY;

    public static MillstoneRecipeBuilder builder() {
        return new MillstoneRecipeBuilder();
    }

    public MillstoneRecipeBuilder setIngredient(ItemLike itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public MillstoneRecipeBuilder setIngredient(TagKey<Item> itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public MillstoneRecipeBuilder setResult(ItemStack stack) {
        this.result = stack;
        return this;
    }

    public MillstoneRecipeBuilder setResult(ItemLike itemLike) {
        this.result = new ItemStack(itemLike);
        return this;
    }

    public MillstoneRecipeBuilder setResult(ItemLike itemLike, int count) {
        this.result = new ItemStack(itemLike, count);
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
        return this.result.getItem();
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
        MillstoneRecipe recipe = new MillstoneRecipe(this.ingredient, this.result);
        recipeOutput.accept(id, recipe, null);
    }
}
