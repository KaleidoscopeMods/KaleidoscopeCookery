package com.github.ysbbbbbb.kaleidoscopecookery.crafting.container;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class TeapotContainer extends SimpleContainer {
    private final ResourceLocation teaFluid;

    public TeapotContainer(ItemStack itemStack, ResourceLocation teaFluid) {
        super(1);
        this.setItem(0, itemStack);
        this.teaFluid = teaFluid;
    }

    public ResourceLocation getTeaFluid() { return this.teaFluid; }

    public ItemStack getItemStack() { return this.getItem(0); }
}
