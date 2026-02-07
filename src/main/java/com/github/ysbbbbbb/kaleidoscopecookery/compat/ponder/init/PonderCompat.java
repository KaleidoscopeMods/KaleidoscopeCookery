package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.init;

import net.minecraftforge.fml.ModList;

public class PonderCompat {
    public static final String ID = "ponder";

    public static boolean PONDER_LOADED = false;

    public static void init() {
        if (ModList.get().isLoaded(ID)) {
            PONDER_LOADED = true;
            KitchenPonderPlugin.init();
        }
    }
}
