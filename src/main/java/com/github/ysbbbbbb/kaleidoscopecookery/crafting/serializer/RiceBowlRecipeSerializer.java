package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.RiceBowlRecipe;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RiceBowlRecipeSerializer implements RecipeSerializer<RiceBowlRecipe> {
    public static final MapCodec<RiceBowlRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(RiceBowlRecipe::category),
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(RiceBowlRecipe::getIngredient),
                    ItemStack.CODEC.fieldOf("result").forGetter(RiceBowlRecipe::getResult)
            ).apply(instance, RiceBowlRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RiceBowlRecipe> STREAM_CODEC = StreamCodec.composite(
            CraftingBookCategory.STREAM_CODEC, RiceBowlRecipe::category,
            Ingredient.CONTENTS_STREAM_CODEC, RiceBowlRecipe::getIngredient,
            ItemStack.STREAM_CODEC, RiceBowlRecipe::getResult,
            RiceBowlRecipe::new
    );

    @Override
    public MapCodec<RiceBowlRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RiceBowlRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
