package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

public class MillstoneRecipe extends SingleItemRecipe {
    private final Ingredient carrier;

    public MillstoneRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, Ingredient carrier) {
        super(ModRecipes.MILLSTONE_RECIPE, ModRecipes.MILLSTONE_SERIALIZER, id, StringUtils.EMPTY, ingredient, result);
        this.carrier = carrier;
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

    public Ingredient getCarrier() {
        return this.carrier;
    }
}
