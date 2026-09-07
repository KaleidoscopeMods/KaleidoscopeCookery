package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BambooTrayRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.BambooTrayRecipeSerializer;
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

public class BambooTrayRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "bamboo_tray";

    private final BambooTrayRecipe.Subtype subtype;

    private Ingredient ingredient = Ingredient.EMPTY;
    private ItemStack result = ItemStack.EMPTY;
    private int duration = BambooTrayRecipeSerializer.DEFAULT_DURATION;

    private BambooTrayRecipeBuilder(BambooTrayRecipe.Subtype subtype) {
        this.subtype = subtype;
    }

    public static BambooTrayRecipeBuilder wetting() {
        return new BambooTrayRecipeBuilder(BambooTrayRecipe.Subtype.WETTING);
    }

    public static BambooTrayRecipeBuilder drying() {
        return new BambooTrayRecipeBuilder(BambooTrayRecipe.Subtype.DRYING);
    }

    public BambooTrayRecipeBuilder setIngredient(ItemLike itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public BambooTrayRecipeBuilder setIngredient(TagKey<Item> tag) {
        this.ingredient = Ingredient.of(tag);
        return this;
    }

    public BambooTrayRecipeBuilder setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public BambooTrayRecipeBuilder setResult(ItemLike itemLike) {
        this.result = new ItemStack(itemLike);
        return this;
    }

    public BambooTrayRecipeBuilder setResult(ItemLike itemLike, int count) {
        this.result = new ItemStack(itemLike, count);
        return this;
    }

    public BambooTrayRecipeBuilder setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public BambooTrayRecipeBuilder setDuration(int duration) {
        this.duration = Math.max(duration, 1);
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
        ResourceLocation id = new ResourceLocation(KaleidoscopeCookery.MOD_ID,
                NAME + "/" + this.subtype.getSerializedName() + "/" + path);
        this.save(output, id);
    }

    @Override
    public void save(Consumer<FinishedRecipe> output, String recipeId) {
        ResourceLocation id = new ResourceLocation(KaleidoscopeCookery.MOD_ID,
                NAME + "/" + this.subtype.getSerializedName() + "/" + recipeId);
        this.save(output, id);
    }

    @Override
    public void save(Consumer<FinishedRecipe> output, ResourceLocation id) {
        output.accept(new Result(id, this.ingredient, this.result, this.subtype, this.duration));
    }

    private record Result(ResourceLocation id, Ingredient ingredient, ItemStack result,
                          BambooTrayRecipe.Subtype subtype, int duration) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", this.ingredient.toJson());
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result.getItem())).toString());
            if (this.result.getCount() > 1) {
                resultJson.addProperty("count", this.result.getCount());
            }
            json.add("result", resultJson);
            json.addProperty("subtype", this.subtype.getSerializedName());
            json.addProperty("duration", this.duration);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.BAMBOO_TRAY_SERIALIZER.get();
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
