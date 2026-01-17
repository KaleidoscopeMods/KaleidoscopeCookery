package com.github.ysbbbbbb.kaleidoscopecookery.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GeneralConfig {
    public static ModConfigSpec init() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        general(builder);
        return builder.build();
    }

    /**
     * 饱腹代偿属性可能在某些整合里过于 OP，故提供一个开关来关闭它。
     */
    public static ModConfigSpec.BooleanValue SATIATED_SHIELD_ABSORB_ENABLED;
    public static ModConfigSpec.BooleanValue SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE;

    private static void general(ModConfigSpec.Builder builder) {
        builder.push("cookery");

        builder.comment("Whether enabling the Satiated Shield effect.");
        SATIATED_SHIELD_ABSORB_ENABLED = builder.define("SatiatedShieldAbsorbEnabled", true);

        builder.comment("Whether the Satiated Shield effect should absorb excess damage beyond its capacity.");
        SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE = builder.define("SatiatedShieldAbsorbExcessDamage", true);

        builder.pop();
    }
}
