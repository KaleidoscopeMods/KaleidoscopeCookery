package com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight;

import com.github.ysbbbbbb.kaleidoscopecookery.api.event.StockpotMatchRecipeEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;

public class CookingPotCompat {
    static void getTransformRecipeForJei(Level level, List<RecipeHolder<StockpotRecipe>> recipes) {
        if (level == null) {
            return;
        }
        RecipeManager recipeManager = level.getRecipeManager();
        recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING.get()).forEach(recipe -> {
            recipes.add(transformRecipe(recipe, level));
        });
    }

    static RecipeHolder<StockpotRecipe> transformRecipe(RecipeHolder<CookingPotRecipe> holder, Level level) {
        CookingPotRecipe cookingPotRecipe = holder.value();
        // 默认全部使用水作为汤底
        StockpotRecipe recipe = new StockpotRecipe(
                cookingPotRecipe.getIngredients(),
                cookingPotRecipe.getResultItem(level.registryAccess()),
                cookingPotRecipe.getCookTime(),
                cookingPotRecipe.getOutputContainer()
        );
        return new RecipeHolder<>(holder.id(), recipe);
    }

    @SubscribeEvent
    static void afterStockpotRecipeMatch(StockpotMatchRecipeEvent.Post event) {
        RecipeManager recipeManager = event.getLevel().getRecipeManager();

        if (!event.getRawOutput().equals(StockpotRecipeSerializer.EMPTY_ID)) {
            return;
        }

        // 开始寻找农夫乐事的厨锅配方进行匹配
        List<ItemStack> items = event.getInput().getInputs();
        RecipeWrapper wrapper = new RecipeWrapper(new ItemStackHandler(NonNullList.copyOf(items)));
        recipeManager.getRecipeFor(ModRecipeTypes.COOKING.get(), wrapper, event.getLevel()).ifPresent(recipe -> {
            // 如果找到匹配的农夫乐事厨锅配方，则将其转换为本模组汤锅配方
            event.setOutput(transformRecipe(recipe, event.getLevel()));
        });
    }
}
