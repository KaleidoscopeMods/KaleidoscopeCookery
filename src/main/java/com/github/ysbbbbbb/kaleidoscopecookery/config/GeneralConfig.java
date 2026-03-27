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

    /**
     * 为饱腹代偿提供更一些精细的配置项。
     */
    // 玩家处于饥饿状态效果时，饱腹代偿是否失效。
    public static ForgeConfigSpec.BooleanValue IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT;
    // 触发饱腹代偿效果所需的最小食物等级。
    public static ForgeConfigSpec.IntValue SATIATED_SHIELD_MIN_FOOD_LEVEL;
    // 饱腹代偿每吸收一点伤害所需的基础 Exhaustion 数值。
    public static ForgeConfigSpec.DoubleValue SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE;
    // 饱腹代偿效果的伤害减免百分比。
    public static ForgeConfigSpec.DoubleValue SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT;
    // 饱腹代偿效果的最大伤害减免量。
    public static ForgeConfigSpec.DoubleValue SATIATED_SHIELD_MAX_DAMAGE_REDUCTION;
    // 饱腹代偿效果下可造成的最小伤害。
    public static ForgeConfigSpec.DoubleValue SATIATED_SHIELD_MIN_DAMAGE;
    // 当玩家受到饱腹代偿弱点伤害时，每点伤害增加的疲劳值的乘算倍率。
    public static ForgeConfigSpec.DoubleValue SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER;

    private static void general(ForgeConfigSpec.Builder builder) {
        builder.push("cookery");

        builder.comment("Whether enabling the Satiated Shield effect.");
        SATIATED_SHIELD_ABSORB_ENABLED = builder.define("SatiatedShieldAbsorbEnabled", true);

        builder.comment("Whether the Satiated Shield effect should absorb excess damage beyond its capacity.");
        SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE = builder.define("SatiatedShieldAbsorbExcessDamage", true);

        builder.comment("If true, the Satiated Shield effect will not apply while the player has the Hunger effect.");
        IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT = builder.define("IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT", true);

        builder.comment("Minimum Hunger Value required for the Satiated Shield to apply (int).");
        SATIATED_SHIELD_MIN_FOOD_LEVEL = builder.defineInRange("SATIATED_SHIELD_MIN_FOOD_LEVEL", 1, 1, 20);

        // 由于在 1 游戏刻中玩家最多积累 40 点疲劳值，因此该配置项大于 40 就没有意义了。
        builder.comment("The exhaustion added each time the player takes damage.");
        SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE = builder.defineInRange("SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE", 2.0, 0.0, 40.0);

        builder.comment("The damage reduction percentage of the Satiated Shield effect.");
        SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT = builder.defineInRange("SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT", 1.0, 0.0, 1.0);

        builder.comment("The maximum damage reduction amount of the Satiated Shield effect.");
        SATIATED_SHIELD_MAX_DAMAGE_REDUCTION = builder.defineInRange("SATIATED_SHIELD_MAX_DAMAGE_REDUCTION", 64.0, 0.0, Integer.MAX_VALUE);

        builder.comment("The minimum damage that can be got in the Satiated Shield effect.");
        SATIATED_SHIELD_MIN_DAMAGE = builder.defineInRange("SATIATED_SHIELD_MIN_DAMAGE", 16.0, 0.0, Integer.MAX_VALUE);

        builder.comment("The multiplier for the exhaustion added per point of Satiated Shield Weakness Damage.");
        SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER = builder.defineInRange("SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER", 2.0, 1.0, Integer.MAX_VALUE);

        builder.pop();
    }
}
