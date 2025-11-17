package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.google.common.collect.Lists;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PotRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "pot";
    private int time = 200;
    private int stirFryCount = 3;
    private Ingredient carrier = Ingredient.EMPTY;
    private List<Ingredient> ingredients = Lists.newArrayList();
    private ItemStack result = ItemStack.EMPTY;

    public static PotRecipeBuilder builder() {
        return new PotRecipeBuilder();
    }

    public PotRecipeBuilder setTime(int time) {
        this.time = time;
        return this;
    }

    public PotRecipeBuilder setStirFryCount(int stirFryCount) {
        this.stirFryCount = stirFryCount;
        return this;
    }

    public PotRecipeBuilder setCarrier(Ingredient ingredient) {
        this.carrier = ingredient;
        return this;
    }

    public PotRecipeBuilder setCarrier(TagKey<Item> tagKey) {
        this.carrier = Ingredient.of(tagKey);
        return this;
    }

    public PotRecipeBuilder setCarrier(ItemLike itemLike) {
        this.carrier = Ingredient.of(itemLike);
        return this;
    }

    public PotRecipeBuilder setBowlCarrier() {
        this.carrier = Ingredient.of(Items.BOWL);
        return this;
    }

    @SuppressWarnings({"varargs", "all"})
    public PotRecipeBuilder addInput(Object... ingredients) {
        for (Object ingredient : ingredients) {
            if (ingredient instanceof ItemLike itemLike) {
                this.ingredients.add(Ingredient.of(itemLike));
            } else if (ingredient instanceof ItemStack stack) {
                this.ingredients.add(Ingredient.of(stack));
            } else if (ingredient instanceof TagKey tagKey) {
                this.ingredients.add(Ingredient.of(tagKey));
            } else if (ingredient instanceof Ingredient ingredientObj) {
                this.ingredients.add(ingredientObj);
            } else if (ingredient instanceof DeferredItem<?> deferredItem) {
                this.ingredients.add(Ingredient.of(deferredItem.get()));
            }
        }
        return this;
    }

    public PotRecipeBuilder setResult(Item result) {
        this.result = new ItemStack(result);
        return this;
    }

    public PotRecipeBuilder setResult(ResourceLocation result) {
        this.result = new ItemStack(Objects.requireNonNull(BuiltInRegistries.ITEM.get(result)));
        return this;
    }

    public PotRecipeBuilder setResult(Item result, int count) {
        this.result = new ItemStack(result, count);
        return this;
    }

    public PotRecipeBuilder setResult(ResourceLocation result, int count) {
        this.result = new ItemStack(Objects.requireNonNull(BuiltInRegistries.ITEM.get(result)), count);
        return this;
    }

    public PotRecipeBuilder setResult(ItemStack result) {
        this.result = result;
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
        recipeOutput.accept(id, new PotRecipe(this.time, this.stirFryCount, this.carrier, this.ingredients, this.result), null);
    }
}
