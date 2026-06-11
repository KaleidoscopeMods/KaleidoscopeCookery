package com.github.ysbbbbbb.kaleidoscopecookery.compat.create;

import com.github.ysbbbbbb.kaleidoscopecookery.api.event.MillstoneMatchRecipeEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.MillstoneRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MillstoneCompat {
    // 供 JEI、REI、EMI 查询的工具类
    static void getTransformRecipeForSearch(Level level, List<RecipeHolder<MillstoneRecipe>> recipes) {
        RecipeManager recipeManager = level.getRecipeManager();
        RecipeType<MillingRecipe> type = AllRecipeTypes.MILLING.getType();
        recipeManager.getAllRecipesFor(type).forEach(recipe -> {
            Ingredient ingredient = recipe.value().getIngredients().getFirst();
            for (ItemStack stack : ingredient.getItems()) {
                SimpleInput input = new SimpleInput(List.of(stack));
                // 如果机械动力的配方和本模组配方有重合，优先选择本模组的配方
                if (recipeManager.getRecipeFor(ModRecipes.MILLSTONE_RECIPE, input, level).isPresent()) {
                    return;
                }
            }
            recipes.add(transformRecipe(recipe));
        });
    }

    private static RecipeHolder<MillstoneRecipe> transformRecipe(RecipeHolder<MillingRecipe> holder) {
        List<RandomOutput> outputs = holder.value()
                .getRollableResults()
                .stream()
                .map(getOutputFunction())
                .toList();
        ResourceLocation id = holder.id();
        Ingredient ingredient = holder.value().getIngredients().getFirst();
        MillstoneRecipe recipe = new MillstoneRecipe(ingredient, outputs);
        return new RecipeHolder<>(id, recipe);
    }

    @NotNull
    private static Function<ProcessingOutput, RandomOutput> getOutputFunction() {
        return output -> new RandomOutput(output.getStack(), output.getChance());
    }

    @SubscribeEvent
    static void afterMillstoneRecipeMatch(MillstoneMatchRecipeEvent.Post event) {
        RecipeHolder<MillstoneRecipe> rawOutput = event.getRawOutput();
        // 如果机械动力的配方和本模组配方有重合，优先选择本模组的配方
        if (rawOutput.id() != MillstoneRecipeSerializer.EMPTY_ID) {
            return;
        }
        List<ItemStack> items = event.getInput().getInputs();
        RecipeWrapper wrapper = new RecipeWrapper(new ItemStackHandler(NonNullList.copyOf(items)));
        Optional<RecipeHolder<MillingRecipe>> recipe = AllRecipeTypes.MILLING.find(wrapper, event.getLevel());
        recipe.ifPresent(millingRecipe -> event.setOutput(transformRecipe(millingRecipe)));
    }
}