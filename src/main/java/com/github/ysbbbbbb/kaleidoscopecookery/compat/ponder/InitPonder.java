package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder;

import net.neoforged.fml.ModList;

public class InitPonder {
    public static final boolean PONDER_LOADED = ModList.get().isLoaded("ponder");

    public static void Init(){
        if (PONDER_LOADED)
            KitchenPonderPlugin.Init();
    }
}
