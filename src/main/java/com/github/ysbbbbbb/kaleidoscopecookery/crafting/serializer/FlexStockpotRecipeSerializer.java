package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
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

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.*;

public class FlexStockpotRecipeSerializer implements RecipeSerializer<FlexStockpotRecipe> {
    public static final MapCodec<FlexStockpotRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").xmap(
                    list -> list,
                    list -> list.stream().filter(i -> !i.isEmpty()).toList()
            ).forGetter(FlexStockpotRecipe::getIngredients),
            ResourceLocation.CODEC.optionalFieldOf("soup_base", DEFAULT_SOUP_BASE).forGetter(FlexStockpotRecipe::soupBase),
            ItemStack.CODEC.fieldOf("result").forGetter(FlexStockpotRecipe::result),
            Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(FlexStockpotRecipe::time),
            Ingredient.CODEC.optionalFieldOf("carrier", DEFAULT_CARRIER).forGetter(FlexStockpotRecipe::carrier),
            StockpotVisuals.CODEC.forGetter(FlexStockpotRecipe::visuals)
    ).apply(instance, FlexStockpotRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlexStockpotRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), FlexStockpotRecipe::getIngredients,
            ResourceLocation.STREAM_CODEC, FlexStockpotRecipe::soupBase,
            ItemStack.STREAM_CODEC, FlexStockpotRecipe::result,
            ByteBufCodecs.INT, FlexStockpotRecipe::time,
            Ingredient.CONTENTS_STREAM_CODEC, FlexStockpotRecipe::carrier,
            StockpotVisuals.STREAM_CODEC, FlexStockpotRecipe::visuals,
            FlexStockpotRecipe::new
    );

    @Override
    public MapCodec<FlexStockpotRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FlexStockpotRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
