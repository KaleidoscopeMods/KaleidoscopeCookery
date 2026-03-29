package com.github.ysbbbbbb.kaleidoscopecookery.crafting.container;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class TeapotContainer extends SimpleContainer {
    private final ResourceLocation teaType;

    public TeapotContainer(ItemStack itemStack, ResourceLocation teaType) {
        super(1);
        this.setItem(0, itemStack);
        this.teaType = teaType;
    }

    public ResourceLocation getTeaType() { return this.teaType; }

    public ItemStack getItemStack() { return this.getItem(0); }
}
