package com.github.ysbbbbbb.kaleidoscopecookery.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GeneralConfig {
    public static ModConfigSpec init() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        general(builder);
        return builder.build();
    }

    public static ModConfigSpec.BooleanValue SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE;

    private static void general(ModConfigSpec.Builder builder) {
        builder.push("cookery");

        builder.comment("Whether the Satiated Shield effect should absorb excess damage beyond its capacity.");
        SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE = builder.define("SatiatedShieldAbsorbExcessDamage", true);

        builder.pop();
    }
}
