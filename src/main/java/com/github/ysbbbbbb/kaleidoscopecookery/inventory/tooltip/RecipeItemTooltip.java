package com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip;

import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record RecipeItemTooltip(RecipeItem.RecipeRecord record) implements TooltipComponent {
}
