package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record MillstoneRecipe(ResourceLocation id, Ingredient ingredient, NonNullList<RandomOutput> results)
        implements BaseRecipe<SimpleContainer> {
    public MillstoneRecipe(ResourceLocation id, Ingredient ingredient, List<RandomOutput> results) {
        this(id, ingredient, fillOutputs(results));
    }

    private static NonNullList<RandomOutput> fillOutputs(List<RandomOutput> outputs) {
        NonNullList<RandomOutput> newOutputs = NonNullList.withSize(4, RandomOutput.EMPTY);
        for (int i = 0; i < Math.min(4, outputs.size()); i++) {
            newOutputs.set(i, outputs.get(i));
        }
        return newOutputs;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.results.get(0).getStack();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
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

    public List<RandomOutput> getRollableResults() {
        return results.stream().filter(o -> !o.isEmpty()).toList();
    }

    public List<ItemStack> getRollableResultsAsItemStacks() {
        return getRollableResults().stream().map(RandomOutput::getStack).toList();
    }

    public List<ItemStack> rollResults(RandomSource random) {
        List<ItemStack> ans = new ArrayList<>();
        for (var output : getRollableResults()) {
            ItemStack itemStack = output.rollOutput(random);
            if (!itemStack.isEmpty()) {
                ans.add(itemStack);
            }
        }
        return ans;
    }
}
