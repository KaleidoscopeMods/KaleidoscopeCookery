package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

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
        this.teaFluid = ForgeRegistries.FLUIDS.getKey(fluid);
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
        } else if (ingredient instanceof RegistryObject) {
            this.ingredient = Ingredient.of(((RegistryObject<Item>) ingredient).get());
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
        recipeOutput.accept(new TeapotFinishedRecipe(id, this.teaFluid, this.ingredient, this.ingredientCount, this.time, this.result));
    }

    public static class TeapotFinishedRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final ResourceLocation teaFluid;
        private final Ingredient ingredient;
        private final int ingredientCount;
        private final int time;
        private final ItemStack result;

        public TeapotFinishedRecipe(ResourceLocation id, ResourceLocation teaFluid, Ingredient ingredient,
                                    int ingredientCount, int time, ItemStack result) {
            this.id = id;
            this.teaFluid = teaFluid;
            this.ingredient = ingredient;
            this.ingredientCount = ingredientCount;
            this.time = time;
            this.result = result;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", ingredient.toJson());
            json.addProperty("ingredient_count", ingredientCount);
            json.addProperty("tea_fluid", teaFluid.toString());
            json.addProperty("time", time);

            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result.getItem())).toString());
            if (this.result.getCount() > 1) {
                itemJson.addProperty("count", this.result.getCount());
            }
            json.add("result", itemJson);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.TEAPOT_SERIALIZER.get();
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }
}