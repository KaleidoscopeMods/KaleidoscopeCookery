package com.github.ysbbbbbb.kaleidoscopecookery.compat.create;

import com.github.ysbbbbbb.kaleidoscopecookery.api.event.MillstoneMatchRecipeEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.MillstoneRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

public class MillstoneCompat {
    static void getTransformRecipeForJei(Level level, List<MillstoneRecipe> recipes) {
        if (level == null) {
            return;
        }
        RecipeManager recipeManager = level.getRecipeManager();
        List<MillingRecipe> millingRecipes = recipeManager.getAllRecipesFor(AllRecipeTypes.MILLING.getType());
//        millingRecipes.forEach(recipe -> {
//            boolean existed = Arrays.stream(recipe.getIngredients().get(0).getItems()).anyMatch(stack ->
//                    recipeManager.getRecipeFor(ModRecipes.MILLSTONE_RECIPE, new SimpleContainer(stack), level).isPresent()
//            );
//            if (!existed) {
//                recipes.add(transformRecipe(recipe, level));
//            }
//        });
        millingRecipes.forEach(recipe -> {
            Ingredient ingredient = recipe.getIngredients().get(0);
            ItemStack[] items = ingredient.getItems();
            boolean existed = false;
            if (items.length == 1) {
                existed = recipeManager.getRecipeFor(ModRecipes.MILLSTONE_RECIPE, new SimpleContainer(items[0]), level).isPresent();
            }
            if (!existed) {
                recipes.add(transformRecipe(recipe, level));
            }
        });
    }

    static MillstoneRecipe transformRecipe(MillingRecipe millingRecipe, Level level) {
        List<RandomOutput> outputs = millingRecipe.getRollableResults().stream().map(processingOutput ->
                new RandomOutput(processingOutput.getStack(), processingOutput.getChance())
        ).toList();
        return new MillstoneRecipe(millingRecipe.getId(), millingRecipe.getIngredients().get(0), outputs);
    }

    @SubscribeEvent
    static void afterMillstoneRecipeMatch(MillstoneMatchRecipeEvent.Post event) {
        MillstoneRecipe rawOutput = event.getRawOutput();

        if (rawOutput.getId() != MillstoneRecipeSerializer.EMPTY_ID) {
            return;
        }
        NonNullList<ItemStack> items = event.getContainer().items;
        RecipeWrapper wrapper = new RecipeWrapper(new ItemStackHandler(items));
        Optional<MillingRecipe> recipe = AllRecipeTypes.MILLING.find(wrapper, event.getLevel());
        recipe.ifPresent(millingRecipe -> {
            event.setOutput(transformRecipe(millingRecipe, event.getLevel()));
        });
    }
}
