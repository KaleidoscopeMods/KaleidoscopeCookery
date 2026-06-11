package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.DEFAULT_SOUP_BASE;

public record StockpotRecipe(NonNullList<Ingredient> ingredients,
                             ResourceLocation soupBase, ItemStack result, int time,
                             Ingredient carrier, StockpotVisuals visuals) implements BaseRecipe<StockpotInput> {

    public StockpotRecipe(List<Ingredient> ingredients,
                          ResourceLocation soupBase, ItemStack result,
                          int time, Ingredient carrier, StockpotVisuals visuals
    ) {
        this(NonNullList.of(Ingredient.EMPTY, BaseRecipe.fillInputs(ingredients)),
                soupBase, result, time, carrier, visuals);
    }

    public StockpotRecipe(NonNullList<Ingredient> ingredients,
                          ItemStack result, int time, ItemStack container) {
        this(ingredients, DEFAULT_SOUP_BASE, result,
                time, Ingredient.of(container), StockpotVisuals.DEFAULT
        );
    }

    public ResourceLocation cookingTexture() {
        return this.visuals.cookingTexture();
    }

    public ResourceLocation finishedTexture() {
        return this.visuals.finishedTexture();
    }

    public int cookingBubbleColor() {
        return this.visuals.cookingBubbleColor();
    }

    public int finishedBubbleColor() {
        return this.visuals.finishedBubbleColor();
    }

    @Override
    public boolean matches(StockpotInput container, Level level) {
        return container.getSoupBase().equals(this.soupBase)
               && RecipeMatcher.findMatches(container.getInputs(), ingredients) != null;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.STOCKPOT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.STOCKPOT_RECIPE;
    }
}
