package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record StockpotVisuals(
        ResourceLocation cookingTexture,
        ResourceLocation finishedTexture,
        int cookingBubbleColor,
        int finishedBubbleColor
) {
    public static final ResourceLocation DEFAULT_COOKING_TEXTURE = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "stockpot/default_cooking");
    public static final ResourceLocation DEFAULT_FINISHED_TEXTURE = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "stockpot/default_finished");

    public static final int DEFAULT_COOKING_BUBBLE_COLOR = 0xFFECC3;
    public static final int DEFAULT_FINISHED_BUBBLE_COLOR = 0xF4AA8B;

    public static final StockpotVisuals DEFAULT = new StockpotVisuals(
            DEFAULT_COOKING_TEXTURE, DEFAULT_FINISHED_TEXTURE,
            DEFAULT_COOKING_BUBBLE_COLOR, DEFAULT_FINISHED_BUBBLE_COLOR
    );

    public static final MapCodec<StockpotVisuals> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("cooking_texture", DEFAULT_COOKING_TEXTURE).forGetter(StockpotVisuals::cookingTexture),
            ResourceLocation.CODEC.optionalFieldOf("finished_texture", DEFAULT_FINISHED_TEXTURE).forGetter(StockpotVisuals::finishedTexture),
            Codec.INT.optionalFieldOf("cooking_bubble_color", DEFAULT_COOKING_BUBBLE_COLOR).forGetter(StockpotVisuals::cookingBubbleColor),
            Codec.INT.optionalFieldOf("finished_bubble_color", DEFAULT_FINISHED_BUBBLE_COLOR).forGetter(StockpotVisuals::finishedBubbleColor)
    ).apply(instance, StockpotVisuals::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StockpotVisuals> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, StockpotVisuals::cookingTexture,
            ResourceLocation.STREAM_CODEC, StockpotVisuals::finishedTexture,
            ByteBufCodecs.INT, StockpotVisuals::cookingBubbleColor,
            ByteBufCodecs.INT, StockpotVisuals::finishedBubbleColor,
            StockpotVisuals::new
    );
}
