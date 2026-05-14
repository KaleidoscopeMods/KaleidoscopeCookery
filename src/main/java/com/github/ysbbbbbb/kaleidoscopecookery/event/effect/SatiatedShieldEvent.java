package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.ysbbbbbb.kaleidoscopecookery.config.GeneralConfig.*;

@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class SatiatedShieldEvent {
    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (event.getEntity() instanceof Player player && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            // 如果没有开启饱腹代偿，直接返回
            if (!GeneralConfig.SATIATED_SHIELD_ABSORB_ENABLED.get()) {
                return;
            }
            // 如果不满足饱腹代偿的触发条件，直接返回
            if (!isSatiatedShieldApply(player)) {
                return;
            }

            // 获取原始伤害
            float originalDamage = event.getAmount();
            // 计算最终伤害，同时将疲劳值增加到玩家身上
            float finalDamage = calculateFinalDamage(player, source, originalDamage);
            // 应用最终伤害
            event.setAmount(finalDamage);
        }
    }

    // 判断玩家是否满足触发饱腹代偿效果的条件
    public static boolean isSatiatedShieldApply(Player player) {
        // 如果玩家处于饥饿状态且配置项启用，则不触发饱腹代偿效果
        if (player.hasEffect(MobEffects.HUNGER) && IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT.get()) {
            return false;
        }
        // 只有当玩家的食物等级达到配置项设定的最低值且玩家具有饱腹代偿效果时，才触发饱腹代偿效果
        return player.getFoodData().getFoodLevel() >= SATIATED_SHIELD_MIN_FOOD_LEVEL.get() && player.hasEffect(ModEffects.SATIATED_SHIELD.get());
    }

    // 计算最终伤害并增加疲劳值
    public static float calculateFinalDamage(Player player, DamageSource source, float originalDamage) {
        // 计算伤害减免量
        float reducedDamage = (float) (originalDamage * SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT.get());
        // 确保伤害减免量不超过最大值
        if (reducedDamage > SATIATED_SHIELD_MAX_DAMAGE_REDUCTION.get()) {
            reducedDamage = (float) (SATIATED_SHIELD_MAX_DAMAGE_REDUCTION.get() * 1f);
        }
        // 初步计算最终伤害
        float finalDamage = originalDamage - reducedDamage;
        // 当原始伤害高于可造成的最低伤害时，确保最终伤害不低于最小值
        if (originalDamage > SATIATED_SHIELD_MIN_DAMAGE.get()) {
            finalDamage = (float) Math.max(finalDamage, SATIATED_SHIELD_MIN_DAMAGE.get());
            reducedDamage = originalDamage - finalDamage;
        }
        // 计算疲劳增加量
        int exhaustionAmount = Math.toIntExact(Math.round(reducedDamage * SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE.get()));
        // 如果伤害来源具有饱腹代偿弱点标签，增加疲劳增加量
        if (source.is(TagMod.SATIATED_SHIELD_WEAKNESS)) {
            exhaustionAmount *= SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER.get();
        }
        // 如果没有开启吸收过量伤害，直接返回最终伤害，否则计算吸收过量伤害后的最终伤害
        if (!SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE.get()) {
            // foodLevel * 4 = Exhaustion，Exhaustion = Damage * 每点伤害的消耗量
            float absorbedDamage = (float) (player.getFoodData().getFoodLevel() * 4 /  SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT.get());
            // 如果伤害来源具有饱腹代偿弱点标签，说明增加了更多的Exhaustion，因此除以弱点倍率来计算实际吸收的伤害。
            if (source.is(TagMod.SATIATED_SHIELD_WEAKNESS)) {
                absorbedDamage /= SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER.get();
            }
            // 过量伤害 = 减免伤害 - 吸收伤害，最终伤害应加上此处的过量伤害
            finalDamage += Math.max(0, reducedDamage - absorbedDamage);
        }
        // 将疲劳增加量应用到玩家身上
        float exhaustion = Math.max(0, exhaustionAmount);
        player.causeFoodExhaustion(exhaustion);
        // 返回最终伤害
        return finalDamage;
    }
}
