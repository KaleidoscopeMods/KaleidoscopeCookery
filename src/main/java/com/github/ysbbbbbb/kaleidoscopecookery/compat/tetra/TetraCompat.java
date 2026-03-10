package com.github.ysbbbbbb.kaleidoscopecookery.compat.tetra;


import net.minecraft.world.item.ItemStack;

public class TetraCompat {
    public static final String TETRA_ID = "tetra";
    public static boolean IS_LOADED = false;

    public static void init() {
        // Tetra 目前还没有 1.21.1 版本，所以先不加载兼容
    }

    public static boolean isModularItem(ItemStack stack) {
        return false;
    }
}
