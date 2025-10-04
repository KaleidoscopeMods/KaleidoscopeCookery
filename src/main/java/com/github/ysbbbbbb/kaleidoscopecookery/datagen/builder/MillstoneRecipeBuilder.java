package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class MillstoneRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "millstone";

    private Ingredient ingredient = Ingredient.EMPTY;
    private ItemStack result = ItemStack.EMPTY;
    private Ingredient carrier = Ingredient.EMPTY;

    public static MillstoneRecipeBuilder builder() {
        return new MillstoneRecipeBuilder();
    }

    public MillstoneRecipeBuilder setIngredient(ItemLike itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public MillstoneRecipeBuilder setIngredient(TagKey<Item> itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public MillstoneRecipeBuilder setResult(ItemStack stack) {
        this.result = stack;
        return this;
    }

    public MillstoneRecipeBuilder setResult(ItemLike itemLike) {
        this.result = new ItemStack(itemLike);
        return this;
    }

    public MillstoneRecipeBuilder setResult(ItemLike itemLike, int count) {
        this.result = new ItemStack(itemLike, count);
        return this;
    }

    public MillstoneRecipeBuilder setCarrier(ItemLike itemLike) {
        this.carrier = Ingredient.of(itemLike);
        return this;
    }

    public MillstoneRecipeBuilder setCarrier(TagKey<Item> itemLike) {
        this.carrier = Ingredient.of(itemLike);
        return this;
    }

    public MillstoneRecipeBuilder setCarrier(Ingredient ingredient) {
        this.carrier = ingredient;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
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
    public void save(Consumer<FinishedRecipe> output) {
        String path = RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath();
        ResourceLocation filePath = new ResourceLocation(KaleidoscopeCookery.MOD_ID, NAME + "/" + path);
        this.save(output, filePath);
    }

    @Override
    public void save(Consumer<FinishedRecipe> output, String recipeId) {
        ResourceLocation filePath = new ResourceLocation(KaleidoscopeCookery.MOD_ID, NAME + "/" + recipeId);
        this.save(output, filePath);
    }

    @Override
    public void save(Consumer<FinishedRecipe> recipeOutput, ResourceLocation id) {
        recipeOutput.accept(new MillstoneRecipe(id, this.ingredient, this.result, this.carrier));
    }

    public static class MillstoneRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final ItemStack result;
        private final Ingredient carrier;

        public MillstoneRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, Ingredient carrier) {
            this.id = id;
            this.ingredient = ingredient;
            this.result = result;
            this.carrier = carrier;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", this.ingredient.toJson());
            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result.getItem())).toString());
            if (this.result.getCount() > 1) {
                itemJson.addProperty("count", this.result.getCount());
            }
            json.add("result", itemJson);
            if (!this.carrier.isEmpty()) {
                json.add("carrier", this.carrier.toJson());
            }
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.MILLSTONE_SERIALIZER.get();
        }

        @Override
        @Nullable
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
