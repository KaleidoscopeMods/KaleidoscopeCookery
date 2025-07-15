package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.google.common.collect.Lists;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class StockpotRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "stockpot";
    private List<Ingredient> ingredients = Lists.newArrayList();
    private ItemStack result = ItemStack.EMPTY;
    private int time = StockpotRecipeSerializer.DEFAULT_TIME;
    private ResourceLocation soupBase = StockpotRecipeSerializer.DEFAULT_SOUP_BASE;
    private ResourceLocation cookingTexture = StockpotRecipeSerializer.DEFAULT_COOKING_TEXTURE;
    private ResourceLocation finishedTexture = StockpotRecipeSerializer.DEFAULT_FINISHED_TEXTURE;
    private int cookingBubbleColor = StockpotRecipeSerializer.DEFAULT_COOKING_BUBBLE_COLOR;
    private int finishedBubbleColor = StockpotRecipeSerializer.DEFAULT_FINISHED_BUBBLE_COLOR;

    public static StockpotRecipeBuilder builder() {
        return new StockpotRecipeBuilder();
    }

    public StockpotRecipeBuilder addInput(ItemLike... itemLikes) {
        for (ItemLike itemLike : itemLikes) {
            this.ingredients.add(Ingredient.of(itemLike));
        }
        return this;
    }

    @SafeVarargs
    public final StockpotRecipeBuilder addInput(TagKey<Item>... ingredients) {
        for (TagKey<Item> tagKey : ingredients) {
            this.ingredients.add(Ingredient.of(tagKey));
        }
        return this;
    }

    public StockpotRecipeBuilder addInput(Ingredient... ingredients) {
        this.ingredients.addAll(Arrays.asList(ingredients));
        return this;
    }

    public StockpotRecipeBuilder setSoupBase(ResourceLocation soupBase) {
        this.soupBase = soupBase;
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
        this.result = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(result)));
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
        recipeOutput.accept(new StockpotFinishedRecipe(id, this.ingredients, this.soupBase, this.result,
                this.time, this.cookingTexture, this.finishedTexture, this.cookingBubbleColor, this.finishedBubbleColor));
    }

    public static class StockpotFinishedRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final List<Ingredient> ingredients;
        private final ResourceLocation soupBase;
        private final ItemStack result;
        private final int time;
        private final ResourceLocation cookingTexture;
        private final ResourceLocation finishedTexture;
        private final int cookingBubbleColor;
        private final int finishedBubbleColor;

        public StockpotFinishedRecipe(ResourceLocation id, List<Ingredient> ingredients, ResourceLocation soupBase, ItemStack result,
                                      int time, ResourceLocation cookingTexture, ResourceLocation finishedTexture,
                                      int cookingBubbleColor, int finishedBubbleColor) {
            this.id = id;
            this.ingredients = ingredients;
            this.soupBase = soupBase;
            this.result = result;
            this.time = time;
            this.cookingTexture = cookingTexture;
            this.finishedTexture = finishedTexture;
            this.cookingBubbleColor = cookingBubbleColor;
            this.finishedBubbleColor = finishedBubbleColor;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray ingredientsJson = new JsonArray();
            this.ingredients.stream().filter(i -> i != Ingredient.EMPTY).forEach(i -> ingredientsJson.add(i.toJson()));
            json.add("ingredients", ingredientsJson);
            json.addProperty("soup_base", this.soupBase.toString());
            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result.getItem())).toString());
            if (this.result.getCount() > 1) {
                itemJson.addProperty("count", this.result.getCount());
            }
            json.add("result", itemJson);

            json.addProperty("time", this.time);
            json.addProperty("cooking_texture", this.cookingTexture.toString());
            json.addProperty("finished_texture", this.finishedTexture.toString());
            json.addProperty("cooking_bubble_color", this.cookingBubbleColor);
            json.addProperty("finished_bubble_color", this.finishedBubbleColor);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.STOCKPOT_SERIALIZER.get();
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
