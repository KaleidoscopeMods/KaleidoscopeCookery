package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ChoppingBoardRecipe extends SingleItemRecipe {
    private final int cutCount;
    private final ResourceLocation modelId;
    private final List<ItemStack> results;

    public ChoppingBoardRecipe(Ingredient ingredient, List<ItemStack> results, int cutCount, ResourceLocation modelId) {
        super(ModRecipes.CHOPPING_BOARD_RECIPE, ModRecipes.CHOPPING_BOARD_SERIALIZER.get(), StringUtils.EMPTY, ingredient,
                results.isEmpty() ? ItemStack.EMPTY : results.get(0));
        this.cutCount = Math.max(cutCount, 1);
        this.modelId = modelId;
        this.results = results.stream().map(ItemStack::copy).toList();
    }

    @Override
    public boolean matches(SingleRecipeInput inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public List<ItemStack> getResults() {
        return this.results;
    }

    public int getCutCount() {
        return cutCount;
    }

    public ResourceLocation getModelId() {
        return modelId;
    }
}
