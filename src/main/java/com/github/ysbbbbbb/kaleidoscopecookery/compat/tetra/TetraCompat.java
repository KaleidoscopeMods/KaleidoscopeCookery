package com.github.ysbbbbbb.kaleidoscopecookery.compat.tetra;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

public class TetraCompat {
    public static final String TETRA_ID = "tetra";
    public static boolean IS_LOADED = false;

    public static void init() {
        IS_LOADED = ModList.get().isLoaded(TETRA_ID);
    }

    public static boolean isModularItem(ItemStack stack) {
        if (!IS_LOADED) {
            return false;
        }
        return TetraCompatInner.isModularItem(stack);
    }
}
