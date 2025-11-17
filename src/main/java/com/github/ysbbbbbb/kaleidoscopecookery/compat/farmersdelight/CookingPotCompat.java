package com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight;

import com.github.ysbbbbbb.kaleidoscopecookery.api.event.StockpotMatchRecipeEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.RecipeWrapper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;

public class CookingPotCompat {
    static void getTransformRecipeForJei(Level level, List<StockpotRecipe> recipes) {
        if (level == null) {
            return;
        }
        RecipeManager recipeManager = level.getRecipeManager();
        RecipeType<CookingPotRecipe> cookingPotRecipeRecipeType = ModRecipeTypes.COOKING.get();
        recipeManager.getAllRecipesFor(cookingPotRecipeRecipeType).forEach(recipe -> {
            recipes.add(transformRecipe(recipe, level));
        });
    }

    static StockpotRecipe transformRecipe(CookingPotRecipe cookingPotRecipe, Level level) {
        // 默认全部使用水作为汤底
        return new StockpotRecipe(
                cookingPotRecipe.getId(),
                cookingPotRecipe.getIngredients(),
                cookingPotRecipe.getResultItem(level.registryAccess()),
                cookingPotRecipe.getCookTime(),
                cookingPotRecipe.getOutputContainer()
        );
    }

    static void afterStockpotRecipeMatch(StockpotMatchRecipeEvent.Post event) {
        StockpotRecipe rawOutput = event.getRawOutput();
        RecipeManager recipeManager = event.getLevel().getRecipeManager();

        if (rawOutput.getId() != StockpotRecipeSerializer.EMPTY_ID) {
            return;
        }

        // 开始寻找农夫乐事的厨锅配方进行匹配
        NonNullList<ItemStack> items = event.getContainer().getItems();
        RecipeWrapper wrapper = new RecipeWrapper(new ItemStackHandler(items.toArray(new ItemStack[0])));
        recipeManager.getRecipeFor(ModRecipeTypes.COOKING.get(), wrapper, event.getLevel()).ifPresent(recipe -> {
            // 如果找到匹配的农夫乐事厨锅配方，则将其转换为本模组汤锅配方
            event.setOutput(transformRecipe(recipe, event.getLevel()));
        });
    }
}
