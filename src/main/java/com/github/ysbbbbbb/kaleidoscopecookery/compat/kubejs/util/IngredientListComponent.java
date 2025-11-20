package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record IngredientListComponent() implements RecipeComponent<List<Ingredient>> {
    public static final IngredientListComponent INSTANCE = new IngredientListComponent();
    public static final Codec<List<Ingredient>> CONTENT_CODEC = Ingredient.LIST_CODEC;
    public static final RecipeComponentType<List<Ingredient>> TYPE = RecipeComponentType.unit(
        ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "ingredient_list"),
        INSTANCE
    );

    @Override
    public RecipeComponentType<?> type() {
        return TYPE;
    }

    @Override
    public Codec<List<Ingredient>> codec() {
        return IngredientListComponent.CONTENT_CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(List.class);
    }

    @Override
    public @NotNull String toString() {
        return "ingredient_list";
    }
}
