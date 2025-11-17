package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingDamageEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SatiatedShieldEvent {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(SatiatedShieldEvent::onPlayerHurt);
    }

    public static boolean onPlayerHurt(LivingEntity entity, DamageSource sources, float amounts) {
        // 1 伤害扣 2 Exhaustion
        LivingDamageEvent event = new LivingDamageEvent(entity, sources, amounts);
        // 1 伤害扣 2 Exhaustion
        int amount = Math.round(event.getAmount()) * 2;
        DamageSource source = event.getSource();
        if (event.getEntity() instanceof Player player && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (player.getFoodData().getFoodLevel() > 0 && player.hasEffect(ModEffects.SATIATED_SHIELD)) {
                // 部分特殊伤害，扣除的 Exhaustion 翻倍
                if (source.is(TagMod.SATIATED_SHIELD_WEAKNESS)) {
                    amount *= 2;
                }
                // 原版是 4 点 Exhaustion 对应 1 点 Food Level
                float exhaustionLevel = Math.max(0, amount / 4f);
                float playerFoodLevel = player.getFoodData().getFoodLevel();
                player.causeFoodExhaustion(exhaustionLevel);

                    // 判断是否超出了玩家当前的 Food Level
                    // 原版是 4 点 Exhaustion 对应 1 点 Food Level
                    float consumedFoodLevel = exhaustionLevel / 4;
                    if (consumedFoodLevel >= playerFoodLevel) {
                        // 扣光了，施加额外伤害
                        float extraDamage;
                        if (source.is(TagMod.SATIATED_SHIELD_WEAKNESS)) {
                            extraDamage = (consumedFoodLevel - playerFoodLevel) * 2;
                        } else {
                            extraDamage = (consumedFoodLevel - playerFoodLevel) * 4;
                        }
                        float remainingDamage = event.getAmount() - extraDamage;
                        event.setAmount(Math.max(0, remainingDamage));
                    } else {
                        event.setAmount(0);
                    }
            }
        }
        event.sendEvent();
        return event.getAmount() > 0;
    }
}
