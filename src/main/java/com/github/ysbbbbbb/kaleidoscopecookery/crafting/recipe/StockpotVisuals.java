package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.resources.ResourceLocation;

public record StockpotVisuals(
        ResourceLocation cookingTexture,
        ResourceLocation finishedTexture,
        int cookingBubbleColor,
        int finishedBubbleColor
) {
    public static final ResourceLocation DEFAULT_COOKING_TEXTURE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot/default_cooking");
    public static final ResourceLocation DEFAULT_FINISHED_TEXTURE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot/default_finished");

    public static final int DEFAULT_COOKING_BUBBLE_COLOR = 0xFFECC3;
    public static final int DEFAULT_FINISHED_BUBBLE_COLOR = 0xF4AA8B;

    public static final StockpotVisuals DEFAULT = new StockpotVisuals(
            DEFAULT_COOKING_TEXTURE, DEFAULT_FINISHED_TEXTURE,
            DEFAULT_COOKING_BUBBLE_COLOR, DEFAULT_FINISHED_BUBBLE_COLOR
    );
}
