package com.github.ysbbbbbb.kaleidoscopecookery.client.tooltip;


import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.RecipeItemTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ClientRecipeItemTooltip implements ClientTooltipComponent {
    private final RecipeItem.RecipeRecord recipeRecord;
    private final MutableComponent ingredientsText;
    private final MutableComponent outputText;

    public ClientRecipeItemTooltip(RecipeItemTooltip containerTooltip) {
        this.recipeRecord = containerTooltip.record();
        this.ingredientsText = Component.translatable("tooltip.kaleidoscope_cookery.recipe_item.ingredient");
        this.outputText = Component.translatable("tooltip.kaleidoscope_cookery.recipe_item.output");
    }

    @Override
    public int getHeight() {
        return 28;
    }

    @Override
    public int getWidth(Font font) {
        int ingredientsSize = recipeRecord.input().size() * 12 + font.width(ingredientsText) + 2;
        int outputSize = font.width(outputText) + 20;
        return Math.max(ingredientsSize, outputSize);
    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
        int ingredientsWidth = font.width(ingredientsText);
        int outputWidth = font.width(outputText);

        guiGraphics.drawString(font, ingredientsText, pX, pY + 4, ChatFormatting.GRAY.getColor());
        int i = 0;
        for (ItemStack stack : recipeRecord.input()) {
            int xOffset = pX + ingredientsWidth + i * 12;
            guiGraphics.renderFakeItem(stack, xOffset, pY);
            i++;
        }

        int xOffset = pX + outputWidth;
        int yOffset = pY + 12;
        guiGraphics.drawString(font, outputText, pX, yOffset + 4, ChatFormatting.GRAY.getColor());
        ItemStack stack = recipeRecord.output();
        guiGraphics.renderFakeItem(stack, xOffset, yOffset);
        guiGraphics.renderItemDecorations(font, stack, xOffset, yOffset);
    }
}
