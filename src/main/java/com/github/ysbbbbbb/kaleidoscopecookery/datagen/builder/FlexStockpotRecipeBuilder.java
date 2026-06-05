package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
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
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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
            } else if (ingredient instanceof RegistryObject) {
                this.ingredients.add(Ingredient.of(((RegistryObject<Item>) ingredient).get()));
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
        this.result = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(result)));
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
        recipeOutput.accept(new FlexStockpotFinishedRecipe(id, this.ingredients, this.soupBase, this.result,
                this.time, this.carrier, this.visuals));
    }

    public static class FlexStockpotFinishedRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final List<Ingredient> ingredients;
        private final ResourceLocation soupBase;
        private final ItemStack result;
        private final int time;
        private final Ingredient carrier;
        private final StockpotVisuals visuals;

        public FlexStockpotFinishedRecipe(ResourceLocation id, List<Ingredient> ingredients, ResourceLocation soupBase, ItemStack result,
                                          int time, Ingredient carrier, StockpotVisuals visuals) {
            this.id = id;
            this.ingredients = ingredients;
            this.soupBase = soupBase;
            this.result = result;
            this.time = time;
            this.carrier = carrier;
            this.visuals = visuals;
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
            if (!this.carrier.isEmpty()) {
                json.add("carrier", this.carrier.toJson());
            }
            json.addProperty("cooking_texture", this.visuals.cookingTexture().toString());
            json.addProperty("finished_texture", this.visuals.finishedTexture().toString());
            json.addProperty("cooking_bubble_color", this.visuals.cookingBubbleColor());
            json.addProperty("finished_bubble_color", this.visuals.finishedBubbleColor());
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.FLEX_STOCKPOT_SERIALIZER.get();
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
