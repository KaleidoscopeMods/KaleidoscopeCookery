package com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip;

import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import javax.annotation.Nullable;

public record RecipeItemTooltip(RecipeItem.RecipeRecord record, @Nullable Quality quality) implements TooltipComponent {
}
