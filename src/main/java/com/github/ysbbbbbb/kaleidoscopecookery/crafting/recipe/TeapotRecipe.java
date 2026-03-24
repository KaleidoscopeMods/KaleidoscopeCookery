package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.TeapotContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record TeapotRecipe(ResourceLocation id, Ingredient ingredient, int ingredientCount,
                          ResourceLocation baseTeaType, ResourceLocation resultTeaType,
                          int time) implements BaseRecipe<TeapotContainer> {

    @Override
    public boolean matches(TeapotContainer container, Level level) {
        ItemStack itemStack = container.getItemStack();
        return ingredient.test(itemStack) && itemStack.getCount() >= ingredientCount && baseTeaType.equals(container.getTeaType());
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.TEAPOT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.TEAPOT_RECIPE;
    }
}
