package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class TeapotRecipeSerializer implements RecipeSerializer<TeapotRecipe> {
    public static final int DEFAULT_TIME = 2400;
    public static final int DEFAULT_INGREDIENT_COUNT = 12;

    public static final ResourceLocation EMPTY_TEA_FLUID = ResourceLocation.withDefaultNamespace("empty");

    private static final MapCodec<TeapotRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.optionalFieldOf("tea_fluid", EMPTY_TEA_FLUID).forGetter(TeapotRecipe::teaFluid),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(TeapotRecipe::ingredient),
            Codec.INT.optionalFieldOf("ingredient_count", DEFAULT_INGREDIENT_COUNT).forGetter(TeapotRecipe::ingredientCount),
            Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(TeapotRecipe::time),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(TeapotRecipe::result)
    ).apply(inst, TeapotRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, TeapotRecipe> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, TeapotRecipe::teaFluid,
            Ingredient.CONTENTS_STREAM_CODEC, TeapotRecipe::ingredient,
            ByteBufCodecs.VAR_INT, TeapotRecipe::ingredientCount,
            ByteBufCodecs.VAR_INT, TeapotRecipe::time,
            ItemStack.STREAM_CODEC, TeapotRecipe::result,
            TeapotRecipe::new
    );

    @Override
    public MapCodec<TeapotRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TeapotRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}