package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class RiceBowlRecipe extends CustomRecipe {
    public static final Ingredient COOKED_RICE = Ingredient.of(TagCommon.COOKED_RICE);

    private final Ingredient ingredient;
    private final ItemStack result;

    public RiceBowlRecipe(CraftingBookCategory category, Ingredient ingredient, ItemStack result) {
        super(category);
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        List<ItemStack> items = container.items();

        // 有且只能有一个物品匹配 ingredient
        if (items.stream().filter(ingredient).count() != 1) {
            return false;
        }

        // 有且只能有一个物品匹配 COOKED_RICE
        return items.stream().filter(COOKED_RICE).count() == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider registries) {
        ItemStack assembled = result.copy();
        copyBestQuality(container, assembled);
        return assembled;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.ingredient, COOKED_RICE);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    public ItemStack getResult() {
        return result;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.RICE_BOWL_SERIALIZER.get();
    }

    private static void copyBestQuality(CraftingInput container, ItemStack result) {
        boolean hasQuality = false;
        Quality bestQuality = Quality.POOR;

        for (int i = 0; i < container.size(); i++) {
            ItemStack ingredient = container.getItem(i);
            if (!QualityUtils.hasQuality(ingredient)) {
                continue;
            }

            Quality quality = QualityUtils.getQuality(ingredient);
            if (!hasQuality || quality.getScore() > bestQuality.getScore()) {
                bestQuality = quality;
                hasQuality = true;
            }
        }

        if (hasQuality) {
            QualityUtils.setQuality(result, bestQuality);
        }
    }
}
