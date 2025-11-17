package com.github.ysbbbbbb.kaleidoscopecookery.crafting.container;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class StockpotContainer extends SimpleContainer {
    private final ResourceLocation soupBase;

    public StockpotContainer(List<ItemStack> items, ResourceLocation soupBase) {
        super(items.size());
        for (int i = 0; i < items.size(); i++) {
            this.setItem(i, items.get(i));
        }
        this.soupBase = soupBase;
    }

    public ResourceLocation getSoupBase() {
        return soupBase;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
