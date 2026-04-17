package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

public class TeapotBuilder implements RecipeBuilder {
    private static final String NAME = "teapot";

    private ResourceLocation teaFluid = TeapotRecipeSerializer.EMPTY_TEA_FLUID;
    private Ingredient ingredient = Ingredient.EMPTY;
    private int ingredientCount = TeapotRecipeSerializer.DEFAULT_INGREDIENT_COUNT;
    private int time = TeapotRecipeSerializer.DEFAULT_TIME;
    private ItemStack result = ItemStack.EMPTY;

    public static TeapotBuilder builder() {
        return new TeapotBuilder();
    }

    public TeapotBuilder setTeaFluid(ResourceLocation baseTeaFluid) {
        this.teaFluid = baseTeaFluid;
        return this;
    }

    public TeapotBuilder setTeaFluid(Fluid fluid) {
        this.teaFluid = BuiltInRegistries.FLUID.getKey((fluid));
        return this;
    }

    @SuppressWarnings("all")
    public TeapotBuilder setIngredient(Object ingredient) {
        if (ingredient instanceof ItemLike itemLike) {
            this.ingredient = Ingredient.of(itemLike);
        } else if (ingredient instanceof ItemStack stack) {
            this.ingredient = Ingredient.of(stack);
        } else if (ingredient instanceof TagKey tagKey) {
            this.ingredient = Ingredient.of(tagKey);
        } else if (ingredient instanceof Ingredient ingredientObj) {
            this.ingredient = ingredientObj;
        } else if (ingredient instanceof DeferredItem<?>) {
            this.ingredient = Ingredient.of(((DeferredItem<?>) ingredient).get());
        } else {
            throw new IllegalArgumentException("Unsupported ingredient type: " + ingredient.getClass().getName());
        }
        return this;
    }

    public TeapotBuilder setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
        return this;
    }

    public TeapotBuilder setTime(int time) {
        this.time = time;
        return this;
    }

    public TeapotBuilder setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public TeapotBuilder setResult(ItemLike result) {
        this.result = new ItemStack(result);
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput output) {
        String path = RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath();
        ResourceLocation filePath = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, NAME + "/" + path);
        this.save(output, filePath);
    }

    @Override
    public void save(RecipeOutput output, String recipeId) {
        ResourceLocation filePath = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, NAME + "/" + recipeId);
        this.save(output, filePath);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        recipeOutput.accept(id, new TeapotRecipe(this.teaFluid, this.ingredient, this.ingredientCount, this.time, this.result), null);
    }
}