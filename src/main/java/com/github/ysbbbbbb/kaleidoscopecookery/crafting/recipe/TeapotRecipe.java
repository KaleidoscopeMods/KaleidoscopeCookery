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

public record TeapotRecipe(ResourceLocation id, ResourceLocation teaFluid,
                           Ingredient ingredient, int ingredientCount,
                           int time, ItemStack result
) implements BaseRecipe<TeapotContainer> {
    /**
     * 配方强制输出 12 个
     */
    public static final int OUTPUT_COUNT = 12;

    @Override
    public boolean matches(TeapotContainer container, Level level) {
        ItemStack stack = container.getItemStack();
        ResourceLocation fluid = container.getTeaFluid();
        return teaFluid.equals(fluid) && ingredient.test(stack) && stack.getCount() >= ingredientCount;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public ItemStack assemble(TeapotContainer container, RegistryAccess registryAccess) {
        return getResultItem(registryAccess).copyWithCount(OUTPUT_COUNT);
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