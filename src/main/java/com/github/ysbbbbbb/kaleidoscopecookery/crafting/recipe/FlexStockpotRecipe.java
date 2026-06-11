package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.List;
import java.util.Set;

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.DEFAULT_SOUP_BASE;

public record FlexStockpotRecipe(NonNullList<Ingredient> ingredients,
                                 ResourceLocation soupBase, ItemStack result, int time,
                                 Ingredient carrier, StockpotVisuals visuals) implements BaseRecipe<StockpotInput> {
    public FlexStockpotRecipe(List<Ingredient> ingredients,
                              ResourceLocation soupBase, ItemStack result,
                              int time, Ingredient carrier, StockpotVisuals visuals
    ) {
        this(NonNullList.of(Ingredient.EMPTY, BaseRecipe.fillInputs(ingredients)),
                soupBase, result, time, carrier, visuals);
    }

    public FlexStockpotRecipe(NonNullList<Ingredient> ingredients,
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
        if (!container.getSoupBase().equals(this.soupBase)) {
            return false;
        }
        NonNullList<ItemStack> merged = NonNullList.withSize(FlexStockpotRecipe.RECIPES_SIZE, ItemStack.EMPTY);
        Set<Item> record = Sets.newHashSet();
        int index = 0;
        for (int i = 0; i < container.size(); i++) {
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
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return this.result;
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
