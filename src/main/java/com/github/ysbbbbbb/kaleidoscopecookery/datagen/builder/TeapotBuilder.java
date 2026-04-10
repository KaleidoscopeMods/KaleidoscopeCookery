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
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TeapotBuilder implements RecipeBuilder {
    private static final String NAME = "teapot";
    private Ingredient ingredient = Ingredient.EMPTY;
    private int ingredientCount = TeapotRecipeSerializer.DEFAULT_INGREDIENT_COUNT;
    private ResourceLocation baseTeaFluid = TeapotRecipeSerializer.DEFAULT_BASE_TEA_FLUID;
    private ResourceLocation resultTeaFluid = TeapotRecipeSerializer.DEFAULT_RESULT_TEA_FLUID;
    private int time = TeapotRecipeSerializer.DEFAULT_TIME;

    public static TeapotBuilder builder() { return new TeapotBuilder(); }

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
        }
        return this;
    }

    public TeapotBuilder setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
        return this;
    }

    public TeapotBuilder setBaseTeaFluid(ResourceLocation baseTeaFluid) {
        this.baseTeaFluid = baseTeaFluid;
        return this;
    }

    public TeapotBuilder setResultTeaFluid(ResourceLocation resultTeaFluid) {
        this.resultTeaFluid = resultTeaFluid;
        return this;
    }

    public TeapotBuilder setTime(int time) {
        this.time = time;
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
        return ItemStack.EMPTY.getItem();
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
        recipeOutput.accept(new TeapotFinishedRecipe(id, ingredient, ingredientCount, baseTeaFluid, resultTeaFluid, time));
    }

    public static class TeapotFinishedRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final int ingredientCount;
        private final ResourceLocation baseTeaFluid;
        private final ResourceLocation resultTeaFluid;
        private final int time;

        public TeapotFinishedRecipe(ResourceLocation id, Ingredient ingredient, int ingredientCount,
                                    ResourceLocation baseTeaFluid, ResourceLocation resultTeaFluid, int time) {
            this.id = id;
            this.ingredient = ingredient;
            this.ingredientCount = ingredientCount;
            this.baseTeaFluid = baseTeaFluid;
            this.resultTeaFluid = resultTeaFluid;
            this.time = time;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", ingredient.toJson());
            json.addProperty("ingredient_count", ingredientCount);
            json.addProperty("base_tea_fluid", baseTeaFluid.toString());
            json.addProperty("result_tea_fluid", resultTeaFluid.toString());
            json.addProperty("time", time);
        }

        @Override
        public ResourceLocation getId() { return id; }

        @Override
        public RecipeSerializer<?> getType() { return ModRecipes.TEAPOT_SERIALIZER.get(); }

        @Override
        public @Nullable JsonObject serializeAdvancement() { return null; }

        @Override
        public @Nullable ResourceLocation getAdvancementId() { return null; }
    }
}
