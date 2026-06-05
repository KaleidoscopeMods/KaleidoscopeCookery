package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Sets;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.List;
import java.util.Set;

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.*;

public record FlexStockpotRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients,
                                  ResourceLocation soupBase, ItemStack result, int time,
                                  Ingredient carrier, StockpotVisuals visuals) implements BaseRecipe<StockpotContainer> {
    public FlexStockpotRecipe(ResourceLocation id, List<Ingredient> ingredients, ResourceLocation soupBase, ItemStack result,
                               int time, Ingredient carrier, ResourceLocation cookingTexture, ResourceLocation finishedTexture,
                               int cookingBubbleColor, int finishedBubbleColor) {
        this(id, NonNullList.of(Ingredient.EMPTY, BaseRecipe.fillInputs(ingredients)),
                soupBase, result, time, carrier,
                new StockpotVisuals(cookingTexture, finishedTexture, cookingBubbleColor, finishedBubbleColor));
    }

    public FlexStockpotRecipe(ResourceLocation id, List<Ingredient> ingredients, ResourceLocation soupBase, ItemStack result,
                              int time, Ingredient carrier, StockpotVisuals visuals) {
        this(id, NonNullList.of(Ingredient.EMPTY, BaseRecipe.fillInputs(ingredients)),
                soupBase, result, time, carrier, visuals);
    }

    public FlexStockpotRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, int time, ItemStack container) {
        this(id, ingredients, DEFAULT_SOUP_BASE, result, time, Ingredient.of(container),
                StockpotVisuals.DEFAULT);
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
    public boolean matches(StockpotContainer container, Level level) {
        if (!container.getSoupBase().equals(this.soupBase)) {
            return false;
        }
        NonNullList<ItemStack> merged = NonNullList.withSize(FlexStockpotRecipe.RECIPES_SIZE, ItemStack.EMPTY);
        Set<Item> record = Sets.newHashSet();
        int index = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            Item item = stack.getItem();
            if (!stack.isEmpty() && !record.contains(item)) {
                merged.set(index, stack);
                record.add(item);
                index++;
                if (index >= merged.size()) {
                    break;
                }
            }
        }
        return RecipeMatcher.findMatches(merged, ingredients) != null;
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
        return ModRecipes.FLEX_STOCKPOT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FLEX_STOCKPOT_RECIPE;
    }
}
