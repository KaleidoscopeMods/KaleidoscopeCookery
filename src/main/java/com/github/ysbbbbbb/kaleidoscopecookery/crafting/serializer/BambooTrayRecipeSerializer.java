package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BambooTrayRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BambooTrayRecipeSerializer implements RecipeSerializer<BambooTrayRecipe> {
    public static final int DEFAULT_DURATION = 60 * 20;

    public static final MapCodec<BambooTrayRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(BambooTrayRecipe::getIngredient),
            ItemStack.CODEC.fieldOf("result").forGetter(BambooTrayRecipe::getResult),
            BambooTrayRecipe.Subtype.CODEC.fieldOf("subtype").forGetter(BambooTrayRecipe::getSubtype),
            Codec.INT.optionalFieldOf("duration", DEFAULT_DURATION).forGetter(BambooTrayRecipe::getDuration)
    ).apply(instance, BambooTrayRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BambooTrayRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, BambooTrayRecipe::getIngredient,
            ItemStack.STREAM_CODEC, BambooTrayRecipe::getResult,
            BambooTrayRecipe.Subtype.STREAM_CODEC, BambooTrayRecipe::getSubtype,
            ByteBufCodecs.VAR_INT, BambooTrayRecipe::getDuration,
            BambooTrayRecipe::new);

    @Override
    public MapCodec<BambooTrayRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BambooTrayRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
