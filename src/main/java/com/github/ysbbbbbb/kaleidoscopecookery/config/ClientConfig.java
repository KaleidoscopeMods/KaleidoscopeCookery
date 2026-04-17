package com.github.ysbbbbbb.kaleidoscopecookery.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static ModConfigSpec init() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        client(builder);
        return builder.build();
    }

    public static ModConfigSpec.BooleanValue SHOW_FOOD_EFFECT_TOOLTIPS;

    private static void client(ModConfigSpec.Builder builder) {
        builder.push("cookery");

        builder.comment("Whether to show food effect tooltips when hovering over food items.");
        SHOW_FOOD_EFFECT_TOOLTIPS = builder.define("ShowFoodEffectTooltips", true);

        builder.pop();
    }
}
