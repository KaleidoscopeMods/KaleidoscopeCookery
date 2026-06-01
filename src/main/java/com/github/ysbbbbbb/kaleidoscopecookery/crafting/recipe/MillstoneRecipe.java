package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.base.Preconditions;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public record MillstoneRecipe(ResourceLocation id, Ingredient ingredient, List<RandomOutput> results)
        implements BaseRecipe<SimpleContainer> {
    public MillstoneRecipe {
        Preconditions.checkArgument(!results.isEmpty(), "Millstone recipe must have at least one output");
        Preconditions.checkArgument(results.size() <= 4, "Millstone recipe can have at most 4 outputs");
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.results.get(0).stack();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.ingredient);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MILLSTONE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.MILLSTONE_RECIPE;
    }

    public Ingredient getIngredient() { return this.ingredient; }
}
