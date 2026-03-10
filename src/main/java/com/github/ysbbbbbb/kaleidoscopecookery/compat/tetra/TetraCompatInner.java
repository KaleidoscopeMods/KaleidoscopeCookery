package com.github.ysbbbbbb.kaleidoscopecookery.compat.tetra;

import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.IModularItem;

public class TetraCompatInner {
    static boolean isModularItem(ItemStack stack) {
        return stack.getItem() instanceof IModularItem;
    }
}
