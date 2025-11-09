package com.github.ysbbbbbb.kaleidoscopecookery.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        general(builder);
        return builder.build();
    }

    public static ForgeConfigSpec.BooleanValue SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE;

    private static void general(ForgeConfigSpec.Builder builder) {
        builder.push("cookery");

        builder.comment("Whether the Satiated Shield effect should absorb excess damage beyond its capacity.");
        SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE = builder.define("SatiatedShieldAbsorbExcessDamage", true);

        builder.pop();
    }
}
