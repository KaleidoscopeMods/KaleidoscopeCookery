package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category.*;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;

public class ModREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        ReiChoppingBoardRecipeCategory.registerCategories(registry);
        ReiMillstoneRecipeCategory.registerCategories(registry);
        ReiPotRecipeCategory.registerCategories(registry);
        ReiStockpotRecipeCategory.registerCategories(registry);
        ReiSteamerRecipeCategory.registerCategories(registry);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        ReiChoppingBoardRecipeCategory.registerDisplays(registry);
        ReiMillstoneRecipeCategory.registerDisplays(registry);
        ReiPotRecipeCategory.registerDisplays(registry);
        ReiStockpotRecipeCategory.registerDisplays(registry);
        ReiSteamerRecipeCategory.registerDisplays(registry);
    }
}