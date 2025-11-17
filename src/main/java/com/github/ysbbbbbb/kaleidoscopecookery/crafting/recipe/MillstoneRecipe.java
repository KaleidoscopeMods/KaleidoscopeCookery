package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

public class MillstoneRecipe extends SingleItemRecipe {
    public MillstoneRecipe(Ingredient ingredient, ItemStack result) {
        super(ModRecipes.MILLSTONE_RECIPE, ModRecipes.MILLSTONE_SERIALIZER.get(), StringUtils.EMPTY, ingredient, result);
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
}
