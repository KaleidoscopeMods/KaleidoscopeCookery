package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.RiceBowlRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.List;

import static net.minecraft.world.item.crafting.CraftingBookCategory.MISC;

public class RiceBowlRecipeMaker {
    public static List<CraftingRecipe> createRecipes() {
        List<CraftingRecipe> recipes = Lists.newArrayList();

        // 盖饭类
        addRiceBowlRecipe(recipes,
                ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get(),
                ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL.get(),
                "scramble_egg_with_tomatoes_rice_bowl"
        );

        addRiceBowlRecipe(recipes,
                ModItems.BRAISED_BEEF.get(),
                ModItems.BRAISED_BEEF_RICE_BOWL.get(),
                "braised_beef_rice_bowl"
        );

        addRiceBowlRecipe(recipes,
                ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get(),
                ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL.get(),
                "stir_fried_pork_with_peppers_rice_bowl"
        );

        addRiceBowlRecipe(recipes,
                ModItems.SWEET_AND_SOUR_PORK.get(),
                ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL.get(),
                "sweet_and_sour_pork_rice_bowl"
        );

        addRiceBowlRecipe(recipes,
                ModItems.FISH_FLAVORED_SHREDDED_PORK.get(),
                ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL.get(),
                "fish_flavored_shredded_pork_rice_bowl"
        );

        return recipes;
    }

    private static void addRiceBowlRecipe(List<CraftingRecipe> recipes, ItemLike input, ItemLike output, String name) {
        ResourceLocation id = new ResourceLocation(KaleidoscopeCookery.MOD_ID, name);

        NonNullList<Ingredient> ingredients = NonNullList.of(
                Ingredient.EMPTY, Ingredient.of(input),
                RiceBowlRecipe.COOKED_RICE
        );

        ItemStack result = output.asItem().getDefaultInstance();
        ShapelessRecipe recipe = new ShapelessRecipe(id, "rice_bowl", MISC, result, ingredients);
        recipes.add(recipe);
    }
}
