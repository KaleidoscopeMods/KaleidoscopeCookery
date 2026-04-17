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

    /**
     * 为饱腹代偿提供更一些精细的配置项。
     * 实际体验的效果为：
     * 伤害不超过 最大伤害减免量 / 伤害减免百分比 时有 伤害减免百分比 的抗伤能力；
     * 伤害不超过 最小伤害 时可以完全抗伤，只受到 最终伤害 * ( 1 - 伤害减免百分比 ) 的伤害，反之至少受到 最小伤害 的伤害；
     * 伤害超过 最小伤害 一定区间时抗伤能力开始下降，最终伤害会逐渐趋近于 原始伤害 * ( 1 - 伤害减免百分比 )，但不会低于 最小伤害；
     * 伤害超过 最大伤害减免量 / 伤害减免百分比 时抗伤能力显著下降。
     */
    // 玩家处于饥饿状态效果时，饱腹代偿是否失效。
    public static ModConfigSpec.BooleanValue IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT;
    // 触发饱腹代偿效果所需的最小食物等级，越高说明饱腹代偿的触发条件越苛刻。
    public static ModConfigSpec.IntValue SATIATED_SHIELD_MIN_FOOD_LEVEL;
    // 饱腹代偿每吸收一点伤害所需的基础 Exhaustion 数值。
    public static ModConfigSpec.DoubleValue SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE;
    // 饱腹代偿效果的伤害减免百分比，不满 1 则必定会受到一部分伤害，越高说明饱腹代偿的抗伤能力越强。
    public static ModConfigSpec.DoubleValue SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT;
    // 饱腹代偿效果的最大伤害减免量，影响面对伤害较高时的抗伤表现，越高说明饱腹代偿的抗伤能力越强。
    public static ModConfigSpec.DoubleValue SATIATED_SHIELD_MAX_DAMAGE_REDUCTION;
    /**
     * 饱腹代偿效果下可造成的最小伤害；
     * 原始伤害高于该值一定区间，会使得抗伤能力显著下降；
     * 该值越高则抗伤下降的阈值越高，同时也会让超过该阈值的惩罚越严重。
     * 因此该值越高说明饱腹代偿的抗伤能力越强，但过高可能会导致玩家在面对高伤害时受到过于严厉的惩罚。
     * 该值为 0 则说明没有最小伤害限制，饱腹代偿的抗伤能力不会因为伤害过高而下降。
     */
    public static ModConfigSpec.DoubleValue SATIATED_SHIELD_MIN_DAMAGE;
    // 当玩家受到饱腹代偿弱点伤害时，每点伤害增加的疲劳值的乘算倍率。
    public static ModConfigSpec.DoubleValue SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER;

    private static void general(ModConfigSpec.Builder builder) {
        builder.push("cookery");

        builder.comment("Whether enabling the Satiated Shield effect.");
        SATIATED_SHIELD_ABSORB_ENABLED = builder.define("SatiatedShieldAbsorbEnabled", true);

        builder.comment("Whether the Satiated Shield effect should absorb excess damage beyond its capacity.");
        SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE = builder.define("SatiatedShieldAbsorbExcessDamage", true);

        builder.comment("If true, the Satiated Shield effect will not apply while the player has the Hunger effect.");
        IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT = builder.define("IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT", true);

        builder.comment("Minimum Hunger Value required for the Satiated Shield to apply (int).");
        SATIATED_SHIELD_MIN_FOOD_LEVEL = builder.defineInRange("SATIATED_SHIELD_MIN_FOOD_LEVEL", 4, 1, 20);

        // 由于在 1 游戏刻中玩家最多积累 40 点疲劳值，因此该配置项大于 40 就没有意义了。
        builder.comment("The exhaustion added each time the player takes damage.");
        SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE = builder.defineInRange("SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE", 2.0, 0.0, 40.0);

        builder.comment("The damage reduction percentage of the Satiated Shield effect.");
        SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT = builder.defineInRange("SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT", 1.0, 0.0, 1.0);

        builder.comment("The maximum damage reduction amount of the Satiated Shield effect.");
        SATIATED_SHIELD_MAX_DAMAGE_REDUCTION = builder.defineInRange("SATIATED_SHIELD_MAX_DAMAGE_REDUCTION", 64.0, 0.0, Integer.MAX_VALUE);

        builder.comment("The minimum damage that can be got in the Satiated Shield effect.");
        SATIATED_SHIELD_MIN_DAMAGE = builder.defineInRange("SATIATED_SHIELD_MIN_DAMAGE", 0.0, 0.0, Integer.MAX_VALUE);

        builder.comment("The multiplier for the exhaustion added per point of Satiated Shield Weakness Damage.");
        SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER = builder.defineInRange("SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER", 2.0, 1.0, Integer.MAX_VALUE);

        builder.pop();
    }
}
