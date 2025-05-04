package com.github.ysbbbbbb.kaleidoscopecookery.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

public class ChoppingBoardRecipe extends SingleItemRecipe {
    private final int cutCount;
    private final ResourceLocation modelId;

    public ChoppingBoardRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, int cutCount, ResourceLocation modelId) {
        super(ModRecipes.CHOPPING_BOARD_RECIPE, ModRecipes.CHOPPING_BOARD_SERIALIZER.get(), id, StringUtils.EMPTY, ingredient, result);
        this.cutCount = Math.max(cutCount, 1);
        this.modelId = modelId;
    }

    @Override
    public boolean matches(Container inv, Level level) {
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

    public int getCutCount() {
        return cutCount;
    }

    public ResourceLocation getModelId() {
        return modelId;
    }
}
