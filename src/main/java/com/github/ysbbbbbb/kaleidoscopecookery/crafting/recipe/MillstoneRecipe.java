package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.base.Preconditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public record MillstoneRecipe(Ingredient ingredient, List<RandomOutput> results)
        implements BaseRecipe<SimpleInput> {
    public MillstoneRecipe {
        Preconditions.checkArgument(!results.isEmpty(), "Millstone recipe must have at least one output");
        Preconditions.checkArgument(results.size() <= 4, "Millstone recipe can have at most 4 outputs");
    }

    @Override
    public boolean matches(SimpleInput input, Level level) {
        return this.ingredient.test(input.getItem(0));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.ingredient);
        return ingredients;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return this.results.getFirst().stack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MILLSTONE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.MILLSTONE_RECIPE;
    }
}
