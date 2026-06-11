package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
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
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlexStockpotRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "flex_stockpot";
    private List<Ingredient> ingredients = Lists.newArrayList();
    private ItemStack result = ItemStack.EMPTY;
    private int time = StockpotRecipeSerializer.DEFAULT_TIME;
    private Ingredient carrier = StockpotRecipeSerializer.DEFAULT_CARRIER;
    private ResourceLocation soupBase = StockpotRecipeSerializer.DEFAULT_SOUP_BASE;
    private StockpotVisuals visuals = StockpotVisuals.DEFAULT;

    public static FlexStockpotRecipeBuilder builder() {
        return new FlexStockpotRecipeBuilder();
    }

    @SuppressWarnings("all")
    public FlexStockpotRecipeBuilder addInput(Object... ingredients) {
        for (Object ingredient : ingredients) {
            if (ingredient instanceof ItemLike itemLike) {
                this.ingredients.add(Ingredient.of(itemLike));
            } else if (ingredient instanceof ItemStack stack) {
                this.ingredients.add(Ingredient.of(stack));
            } else if (ingredient instanceof TagKey tagKey) {
                this.ingredients.add(Ingredient.of(tagKey));
            } else if (ingredient instanceof Ingredient ingredientObj) {
                this.ingredients.add(ingredientObj);
            } else if (ingredient instanceof DeferredItem) {
                this.ingredients.add(Ingredient.of(((DeferredItem<Item>) ingredient).get()));
            }
        }
        return this;
    }

    public FlexStockpotRecipeBuilder setSoupBase(ResourceLocation soupBase) {
        this.soupBase = soupBase;
        return this;
    }

    public FlexStockpotRecipeBuilder setResult(Item result) {
        this.result = new ItemStack(result, 3);
        return this;
    }

    public FlexStockpotRecipeBuilder setResult(Item result, int count) {
        return this.setResult(new ItemStack(result, count));
    }

    public FlexStockpotRecipeBuilder setResult(ResourceLocation result) {
        this.result = new ItemStack(BuiltInRegistries.ITEM.get(result));
        return this;
    }

    public FlexStockpotRecipeBuilder setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public FlexStockpotRecipeBuilder setTime(int time) {
        this.time = time;
        return this;
    }

    public FlexStockpotRecipeBuilder setCarrier(ItemLike carrier) {
        this.carrier = Ingredient.of(carrier);
        return this;
    }

    public FlexStockpotRecipeBuilder setCookingTexture(ResourceLocation cookingTexture) {
        this.visuals = new StockpotVisuals(cookingTexture, this.visuals.finishedTexture(),
                this.visuals.cookingBubbleColor(), this.visuals.finishedBubbleColor());
        return this;
    }

    public FlexStockpotRecipeBuilder setFinishedTexture(ResourceLocation finishedTexture) {
        this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), finishedTexture,
                this.visuals.cookingBubbleColor(), this.visuals.finishedBubbleColor());
        return this;
    }

    public FlexStockpotRecipeBuilder setCookingBubbleColor(int cookingBubbleColor) {
        this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), this.visuals.finishedTexture(),
                cookingBubbleColor, this.visuals.finishedBubbleColor());
        return this;
    }

    public FlexStockpotRecipeBuilder setFinishedBubbleColor(int finishedBubbleColor) {
        this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), this.visuals.finishedTexture(),
                this.visuals.cookingBubbleColor(), finishedBubbleColor);
        return this;
    }

    public FlexStockpotRecipeBuilder setBubbleColors(int cookingBubbleColor, int finishedBubbleColor) {
        this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), this.visuals.finishedTexture(),
                cookingBubbleColor, finishedBubbleColor);
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
        ResourceLocation filePath = ResourceLocation.fromNamespaceAndPath(
                KaleidoscopeCookery.MOD_ID, NAME + "/" + path
        );
        this.save(output, filePath);
    }

    @Override
    public void save(RecipeOutput output, String recipeId) {
        ResourceLocation filePath = ResourceLocation.fromNamespaceAndPath(
                KaleidoscopeCookery.MOD_ID, NAME + "/" + recipeId
        );
        this.save(output, filePath);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        FlexStockpotRecipe recipe = new FlexStockpotRecipe(
                this.ingredients, this.soupBase, this.result,
                this.time, this.carrier, this.visuals
        );
        recipeOutput.accept(id, recipe, null);
    }
}
