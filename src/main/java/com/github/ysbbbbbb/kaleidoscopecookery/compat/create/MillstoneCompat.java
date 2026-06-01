package com.github.ysbbbbbb.kaleidoscopecookery.compat.create;

import com.github.ysbbbbbb.kaleidoscopecookery.api.event.MillstoneMatchRecipeEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.MillstoneRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MillstoneCompat {
    // 供 JEI、REI、EMI 查询的工具类
    static void getTransformRecipeForSearch(Level level, List<MillstoneRecipe> recipes) {
        RecipeManager recipeManager = level.getRecipeManager();
        RecipeType<MillingRecipe> type = AllRecipeTypes.MILLING.getType();
        recipeManager.getAllRecipesFor(type).forEach(recipe -> {
            Ingredient ingredient = recipe.getIngredients().get(0);
            for (ItemStack stack : ingredient.getItems()) {
                SimpleContainer container = new SimpleContainer(stack);
                // 如果机械动力的配方和本模组配方有重合，优先选择本模组的配方
                if (recipeManager.getRecipeFor(ModRecipes.MILLSTONE_RECIPE, container, level).isPresent()) {
                    return;
                }
            }
            recipes.add(transformRecipe(recipe));
        });
    }

    private static MillstoneRecipe transformRecipe(MillingRecipe millingRecipe) {
        List<RandomOutput> outputs = millingRecipe
                .getRollableResults()
                .stream()
                .map(getOutputFunction())
                .toList();
        ResourceLocation id = millingRecipe.getId();
        Ingredient ingredient = millingRecipe.getIngredients().get(0);
        return new MillstoneRecipe(id, ingredient, outputs);
    }

    @NotNull
    private static Function<ProcessingOutput, RandomOutput> getOutputFunction() {
        return output -> new RandomOutput(output.getStack(), output.getChance());
    }

    @SubscribeEvent
    static void afterMillstoneRecipeMatch(MillstoneMatchRecipeEvent.Post event) {
        MillstoneRecipe rawOutput = event.getRawOutput();
        // 如果机械动力的配方和本模组配方有重合，优先选择本模组的配方
        if (rawOutput.getId() != MillstoneRecipeSerializer.EMPTY_ID) {
            return;
        }
        NonNullList<ItemStack> items = event.getContainer().items;
        RecipeWrapper wrapper = new RecipeWrapper(new ItemStackHandler(items));
        Optional<MillingRecipe> recipe = AllRecipeTypes.MILLING.find(wrapper, event.getLevel());
        recipe.ifPresent(millingRecipe -> event.setOutput(transformRecipe(millingRecipe)));
    }
}