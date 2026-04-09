package com.github.ysbbbbbb.kaleidoscopecookery.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        general(builder);
        return builder.build();
    }

    /**
     * 饱腹代偿属性可能在某些整合里过于 OP，故提供一个开关来关闭它。
     */
    public static ForgeConfigSpec.BooleanValue SATIATED_SHIELD_ABSORB_ENABLED;
    public static ForgeConfigSpec.BooleanValue SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE;

    private static void general(ForgeConfigSpec.Builder builder) {
        builder.push("cookery");

        builder.comment("Whether enabling the Satiated Shield effect.");
        SATIATED_SHIELD_ABSORB_ENABLED = builder.define("SatiatedShieldAbsorbEnabled", true);

        builder.comment("Whether the Satiated Shield effect should absorb excess damage beyond its capacity.");
        SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE = builder.define("SatiatedShieldAbsorbExcessDamage", true);

        builder.pop();
    }
}
