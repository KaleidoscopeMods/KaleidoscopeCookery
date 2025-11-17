package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import dev.latvian.mods.kubejs.typings.Info;

public class ModKubeJSUtil {
    @Info("""
            Register a soup base, used for soup pot recipe. <br>
            注册一个汤底，用于汤锅合成。
            """)
    public static void registerSoupBase(SimpleSoupBaseBuilder soupBaseBuilder) {
        SoupBaseManager.registerSoupBase(soupBaseBuilder.build());
    }
}
