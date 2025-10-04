package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.RecipeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ProgressArrowElement;

public enum RecipeBlockComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        BlockEntity blockEntity = accessor.getBlockEntity();
        if (!(blockEntity instanceof RecipeBlockEntity recipeBlock)) {
            return;
        }
        ItemStack stackInSlot = recipeBlock.getItems().getStackInSlot(0);
        if (stackInSlot.isEmpty()) {
            return;
        }
        RecipeItem.RecipeRecord recipe = RecipeItem.getRecipe(stackInSlot);
        if (recipe == null) {
            return;
        }
        ItemStack output = recipe.output();
        tooltip.add(output.getHoverName());

        boolean isFirst = true;
        for (ItemStack stack : recipe.input()) {
            if (isFirst) {
                tooltip.add(IElementHelper.get().item(stack));
            } else {
                tooltip.append(IElementHelper.get().item(stack));
            }
            isFirst = false;
        }
        tooltip.append(new ProgressArrowElement(1));
        tooltip.append(IElementHelper.get().item(output));
    }

    @Override
    public ResourceLocation getUid() {
        return ModPlugin.RECIPE_BLOCK;
    }
}
