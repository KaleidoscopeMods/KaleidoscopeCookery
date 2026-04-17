package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.TeapotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record TeapotRecipe(ResourceLocation teaFluid,
                           Ingredient ingredient, int ingredientCount,
                           int time, ItemStack result
) implements BaseRecipe<TeapotInput> {
    /**
     * 配方强制输出 12 个
     */
    public static final int OUTPUT_COUNT = 12;

    @Override
    public boolean matches(TeapotInput container, Level level) {
        ItemStack stack = container.getItemStack();
        ResourceLocation fluid = container.getTeaFluid();
        return teaFluid.equals(fluid) && ingredient.test(stack) && stack.getCount() >= ingredientCount;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public ItemStack assemble(TeapotInput container, HolderLookup.Provider registryAccess) {
        return getResultItem(registryAccess).copyWithCount(OUTPUT_COUNT);
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