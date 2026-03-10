package com.github.ysbbbbbb.kaleidoscopecookery.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class OilItem extends WithTooltipsItem {
    public OilItem() {
        super(new Item.Properties(), "oil");
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        // 热值同煤炭
        return 1600;
    }
}
