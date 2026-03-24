package com.github.ysbbbbbb.kaleidoscopecookery.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        client(builder);
        return builder.build();
    }

    public static ForgeConfigSpec.BooleanValue SHOW_FOOD_EFFECT_TOOLTIPS;

    private static void client(ForgeConfigSpec.Builder builder) {
        builder.push("client");

        builder.comment("Whether to show food effect tooltips when hovering over food items.");
        SHOW_FOOD_EFFECT_TOOLTIPS = builder.define("ShowFoodEffectTooltips", true);

        builder.pop();
    }
}
