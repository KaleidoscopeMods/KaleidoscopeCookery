package com.github.ysbbbbbb.kaleidoscopecookery.crafting;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.RiceBowlRecipe;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.List;

import static net.minecraft.world.item.crafting.CraftingBookCategory.MISC;

/**
 * 给 JEI EMI 和 REI 添加配方用的
 */
public class RiceBowlRecipeMaker {
    public static List<CraftingRecipe> createRecipes() {
        List<CraftingRecipe> recipes = Lists.newArrayList();

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }

        level.getRecipeManager()
                .byType(RecipeType.CRAFTING)
                .forEach((id, recipe) ->
                        addRiceBowlRecipe(recipes, id, recipe)
                );

        return recipes;
    }

    private static void addRiceBowlRecipe(List<CraftingRecipe> recipes, ResourceLocation id, CraftingRecipe recipe) {
        if (recipe instanceof RiceBowlRecipe riceBowlRecipe) {
            var ingredients = riceBowlRecipe.getIngredients();
            ItemStack result = riceBowlRecipe.getResult();
            ShapelessRecipe shapelessRecipe = new ShapelessRecipe(id, "rice_bowl", MISC, result, ingredients);
            recipes.add(shapelessRecipe);
        }
    }
}
