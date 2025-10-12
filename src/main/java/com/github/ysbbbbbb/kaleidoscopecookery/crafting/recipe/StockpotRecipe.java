package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.util.RecipeMatcher;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.*;

public record StockpotRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients,
                             ResourceLocation soupBase, ItemStack result, int time,
                             Ingredient carrier, ResourceLocation cookingTexture,
                             ResourceLocation finishedTexture, int cookingBubbleColor,
                             int finishedBubbleColor) implements BaseRecipe<StockpotContainer> {
    public StockpotRecipe(ResourceLocation id, List<Ingredient> ingredients, ResourceLocation soupBase, ItemStack result,
                          int time, Ingredient carrier, ResourceLocation cookingTexture, ResourceLocation finishedTexture,
                          int cookingBubbleColor, int finishedBubbleColor) {
        this(id, NonNullList.of(Ingredient.EMPTY, BaseRecipe.fillInputs(ingredients)),
                soupBase, result, time, carrier, cookingTexture, finishedTexture,
                cookingBubbleColor, finishedBubbleColor);
    }

    public StockpotRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, int time, ItemStack container) {
        this(id, ingredients, DEFAULT_SOUP_BASE, result, time, Ingredient.of(container),
                DEFAULT_COOKING_TEXTURE, DEFAULT_FINISHED_TEXTURE,
                DEFAULT_COOKING_BUBBLE_COLOR, DEFAULT_FINISHED_BUBBLE_COLOR);
    }

    @Override
    public boolean matches(StockpotContainer container, Level level) {
        return container.getSoupBase().equals(this.soupBase)
               && RecipeMatcher.findMatches(container.items, ingredients) != null;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.STOCKPOT_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.STOCKPOT_RECIPE;
    }
}
