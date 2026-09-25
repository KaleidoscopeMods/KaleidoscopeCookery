package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.gson.JsonArray;
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
import java.util.List;
import java.util.function.Consumer;

public class ChoppingBoardBuilder implements RecipeBuilder {
    private static final String NAME = "chopping_board";

    private Ingredient ingredient = Ingredient.EMPTY;
    private List<ItemStack> results = List.of();
    private int cutCount = 3;
    private ResourceLocation modelId;

    public static ChoppingBoardBuilder builder() {
        return new ChoppingBoardBuilder();
    }

    public ChoppingBoardBuilder setIngredient(ItemLike itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public ChoppingBoardBuilder setIngredient(TagKey<Item> itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public ChoppingBoardBuilder setResult(ItemStack stack) {
        this.results = List.of(stack);
        return this;
    }

    public ChoppingBoardBuilder setResult(ItemLike itemLike) {
        this.results = List.of(new ItemStack(itemLike));
        return this;
    }

    public ChoppingBoardBuilder setResult(ItemLike itemLike, int count) {
        this.results = List.of(new ItemStack(itemLike, count));
        return this;
    }

    public ChoppingBoardBuilder setResults(ItemStack... stacks) {
        this.results = List.of(stacks);
        return this;
    }

    public ChoppingBoardBuilder setCutCount(int cutCount) {
        this.cutCount = Math.max(cutCount, 1);
        return this;
    }

    public ChoppingBoardBuilder setModelId(ResourceLocation modelId) {
        this.modelId = modelId;
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
        return this.results.get(0).getItem();
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
        recipeOutput.accept(new ChoppingBoardRecipe(id, this.ingredient, this.results, this.cutCount, this.modelId));
    }

    public static class ChoppingBoardRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final List<ItemStack> results;
        private final int cutCount;
        private final ResourceLocation modelId;

        public ChoppingBoardRecipe(ResourceLocation id, Ingredient ingredient, List<ItemStack> results, int cutCount, ResourceLocation modelId) {
            this.id = id;
            this.ingredient = ingredient;
            this.results = results;
            this.cutCount = cutCount;
            this.modelId = modelId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", this.ingredient.toJson());
            JsonArray resultArray = new JsonArray();
            for (ItemStack result : this.results) {
                JsonObject itemJson = new JsonObject();
                itemJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.getItem())).toString());
                if (result.getCount() > 1) {
                    itemJson.addProperty("count", result.getCount());
                }
                resultArray.add(itemJson);
            }
            json.add("result", resultArray);
            json.addProperty("cut_count", this.cutCount);
            json.addProperty("model_id", this.modelId.toString());
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.CHOPPING_BOARD_SERIALIZER.get();
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
