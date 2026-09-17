package com.github.ysbbbbbb.kaleidoscopecookery.compat.everycomp;

import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;

public class EveryCompatCompat {
    public static void init() {
        EveryCompatAPI.registerModule(new KaleidoscopeCookeryModule());
    }
}
