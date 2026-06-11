package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexPotRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FlexPotRecipeSerializer implements RecipeSerializer<FlexPotRecipe> {
    public static final MapCodec<FlexPotRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("time", 200).forGetter(FlexPotRecipe::time),
                    Codec.INT.optionalFieldOf("stir_fry_count", 3).forGetter(FlexPotRecipe::stirFryCount),
                    Ingredient.CODEC.optionalFieldOf("carrier", Ingredient.EMPTY).forGetter(FlexPotRecipe::carrier),
                    Ingredient.CODEC.listOf().fieldOf("ingredients").xmap(
                            list -> list,
                            list -> list.stream().filter(i -> !i.isEmpty()).toList()
                    ).forGetter(recipe -> recipe.ingredients().stream().toList()),
                    ItemStack.CODEC.fieldOf("result").forGetter(FlexPotRecipe::result)
            ).apply(instance, FlexPotRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FlexPotRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FlexPotRecipe::time,
            ByteBufCodecs.INT, FlexPotRecipe::stirFryCount,
            Ingredient.CONTENTS_STREAM_CODEC, FlexPotRecipe::carrier,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), FlexPotRecipe::ingredients,
            ItemStack.STREAM_CODEC, FlexPotRecipe::result,
            FlexPotRecipe::new);

    @Override
    public MapCodec<FlexPotRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FlexPotRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
