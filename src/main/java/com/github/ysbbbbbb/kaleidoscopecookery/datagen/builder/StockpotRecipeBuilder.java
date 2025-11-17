package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.google.common.collect.Lists;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class StockpotRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "stockpot";
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private ItemStack result = ItemStack.EMPTY;
    private int time = StockpotRecipeSerializer.DEFAULT_TIME;
    private Ingredient carrier = StockpotRecipeSerializer.DEFAULT_CARRIER;
    private ResourceLocation soupBase = StockpotRecipeSerializer.DEFAULT_SOUP_BASE;
    private ResourceLocation cookingTexture = StockpotRecipeSerializer.DEFAULT_COOKING_TEXTURE;
    private ResourceLocation finishedTexture = StockpotRecipeSerializer.DEFAULT_FINISHED_TEXTURE;
    private int cookingBubbleColor = StockpotRecipeSerializer.DEFAULT_COOKING_BUBBLE_COLOR;
    private int finishedBubbleColor = StockpotRecipeSerializer.DEFAULT_FINISHED_BUBBLE_COLOR;

    public static StockpotRecipeBuilder builder() {
        return new StockpotRecipeBuilder();
    }

    @SuppressWarnings("all")
    public StockpotRecipeBuilder addInput(Object... ingredients) {
        for (Object ingredient : ingredients) {
            if (ingredient instanceof ItemLike itemLike) {
                this.ingredients.add(Ingredient.of(itemLike));
            } else if (ingredient instanceof ItemStack stack) {
                this.ingredients.add(Ingredient.of(stack));
            } else if (ingredient instanceof TagKey tagKey) {
                this.ingredients.add(Ingredient.of(tagKey));
            } else if (ingredient instanceof Ingredient ingredientObj) {
                this.ingredients.add(ingredientObj);
            }
        }
        return this;
    }

    public StockpotRecipeBuilder setSoupBase(ResourceLocation soupBase) {
        this.soupBase = soupBase;
        return this;
    }

    public StockpotRecipeBuilder setCarrier(ItemLike carrier) {
        this.carrier = Ingredient.of(carrier);
        return this;
    }

    public StockpotRecipeBuilder setResult(Item result) {
        this.result = new ItemStack(result, 3);
        return this;
    }

    public StockpotRecipeBuilder setResult(Item result, int count) {
        return this.setResult(new ItemStack(result, count));
    }

    public StockpotRecipeBuilder setResult(ResourceLocation result) {
        this.result = new ItemStack(Objects.requireNonNull(BuiltInRegistries.ITEM.get(result)));
        return this;
    }

    public StockpotRecipeBuilder setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public StockpotRecipeBuilder setTime(int time) {
        this.time = time;
        return this;
    }

    public StockpotRecipeBuilder setCookingTexture(ResourceLocation cookingTexture) {
        this.cookingTexture = cookingTexture;
        return this;
    }

    public StockpotRecipeBuilder setFinishedTexture(ResourceLocation finishedTexture) {
        this.finishedTexture = finishedTexture;
        return this;
    }

    public StockpotRecipeBuilder setCookingBubbleColor(int cookingBubbleColor) {
        this.cookingBubbleColor = cookingBubbleColor;
        return this;
    }

    public StockpotRecipeBuilder setFinishedBubbleColor(int finishedBubbleColor) {
        this.finishedBubbleColor = finishedBubbleColor;
        return this;
    }

    public StockpotRecipeBuilder setBubbleColors(int cookingBubbleColor, int finishedBubbleColor) {
        this.cookingBubbleColor = cookingBubbleColor;
        this.finishedBubbleColor = finishedBubbleColor;
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
        recipeOutput.accept(id, new StockpotRecipe(this.ingredients, this.soupBase, this.result, this.time, this.carrier,
                this.cookingTexture, this.finishedTexture, this.cookingBubbleColor, this.finishedBubbleColor), null);
    }
}
