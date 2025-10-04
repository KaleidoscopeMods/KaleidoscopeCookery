package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeGenerator extends ModRecipeProvider {
    private final List<ModRecipeProvider> providers = Lists.newArrayList();

    public ModRecipeGenerator(PackOutput output) {
        super(output);
        providers.add(new ChoppingBoardRecipeProvider(output));
        providers.add(new DecorationRecipeProvider(output));
        providers.add(new FoodBiteRecipeProvider(output));
        providers.add(new PotRecipeProvider(output));
        providers.add(new ShapedRecipeProvider(output));
        providers.add(new ShapelessRecipeProvider(output));
        providers.add(new SimpleCookingRecipeProvider(output));
        providers.add(new SimplePotRecipeProvider(output));
        providers.add(new StockpotRecipeProvider(output));
        providers.add(new MillstoneRecipeProvider(output));
        providers.add(new SteamerRecipeProvider(output));
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        netheriteSmithing(consumer, ModItems.DIAMOND_KITCHEN_KNIFE.get(), RecipeCategory.TOOLS, ModItems.NETHERITE_KITCHEN_KNIFE.get());
        for (ModRecipeProvider provider : providers) {
            provider.buildRecipes(consumer);
        }
    }
}
