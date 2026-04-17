package com.github.ysbbbbbb.kaleidoscopecookery.crafting.container;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TeapotInput extends SimpleInput {
    private final ResourceLocation teaFluid;

    public TeapotInput(ItemStack itemStack, ResourceLocation teaFluid) {
        super(List.of(itemStack));
        this.teaFluid = teaFluid;
    }

    public ResourceLocation getTeaFluid() {
        return this.teaFluid;
    }

    public ItemStack getItemStack() {
        return this.getItem(0);
    }
}