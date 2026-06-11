package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category.*;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.RiceBowlRecipeMaker;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

@REIPluginClient
public class ModREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        ReiChoppingBoardRecipeCategory.registerCategories(registry);
        ReiMillstoneRecipeCategory.registerCategories(registry);
        ReiPotRecipeCategory.registerCategories(registry);
        ReiFlexPotRecipeCategory.registerCategories(registry);
        ReiStockpotRecipeCategory.registerCategories(registry);
        ReiFlexStockpotRecipeCategory.registerCategories(registry);
        ReiSteamerRecipeCategory.registerCategories(registry);
        ReiTeapotRecipeCategory.registerCategories(registry);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        ReiChoppingBoardRecipeCategory.registerDisplays(registry);
        ReiMillstoneRecipeCategory.registerDisplays(registry);
        ReiPotRecipeCategory.registerDisplays(registry);
        ReiFlexPotRecipeCategory.registerDisplays(registry);
        ReiStockpotRecipeCategory.registerDisplays(registry);
        ReiFlexStockpotRecipeCategory.registerDisplays(registry);
        ReiSteamerRecipeCategory.registerDisplays(registry);
        ReiTeapotRecipeCategory.registerDisplays(registry);

        for (RecipeHolder<CraftingRecipe> recipe : RiceBowlRecipeMaker.createRecipes()) {
            DefaultCraftingDisplay<?> display = DefaultCraftingDisplay.of(recipe);
            if (display != null) {
                registry.add(display);
            }
        }
    }
}