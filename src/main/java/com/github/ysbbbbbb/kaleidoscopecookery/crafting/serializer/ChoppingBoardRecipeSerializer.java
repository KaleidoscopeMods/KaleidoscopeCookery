package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe;
import com.mojang.datafixers.util.Either;
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

import java.util.List;

public class ChoppingBoardRecipeSerializer implements RecipeSerializer<ChoppingBoardRecipe> {
    public static final ResourceLocation EMPTY = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "empty");

    public static final Codec<List<ItemStack>> ITEM_STACKS_CODEC = Codec.either(ItemStack.CODEC, ItemStack.CODEC.listOf()).xmap(
            either -> either.map(List::of, list -> list),
            list -> list.size() == 1 ? Either.left(list.getFirst()) : Either.right(list));

    public static final MapCodec<ChoppingBoardRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(ChoppingBoardRecipe::getIngredient),
                    ITEM_STACKS_CODEC.fieldOf("result").forGetter(ChoppingBoardRecipe::getResults),
                    Codec.INT.optionalFieldOf("cut_count", 3).forGetter(ChoppingBoardRecipe::getCutCount),
                    ResourceLocation.CODEC.optionalFieldOf("model_id", EMPTY).forGetter(ChoppingBoardRecipe::getModelId)
            ).apply(instance, ChoppingBoardRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ChoppingBoardRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, ChoppingBoardRecipe::getIngredient,
            ItemStack.LIST_STREAM_CODEC, ChoppingBoardRecipe::getResults,
            ByteBufCodecs.INT, ChoppingBoardRecipe::getCutCount,
            ResourceLocation.STREAM_CODEC, ChoppingBoardRecipe::getModelId,
            ChoppingBoardRecipe::new);

    @Override
    public MapCodec<ChoppingBoardRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ChoppingBoardRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
