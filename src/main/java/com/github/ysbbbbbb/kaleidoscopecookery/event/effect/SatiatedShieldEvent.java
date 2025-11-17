package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static com.github.ysbbbbbb.kaleidoscopecookery.config.GeneralConfig.SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE;

@EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class SatiatedShieldEvent {
    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent.Pre event) {
        // 1 伤害扣 2 Exhaustion
        int amount = Math.round(event.getContainer().getNewDamage()) * 2;
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

                if (SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE.get()) {
                    event.getContainer().setNewDamage(0);
                } else {
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
                        float remainingDamage = event.getNewDamage() - extraDamage;
                        event.getContainer().setNewDamage(Math.max(0, remainingDamage));
                    } else {
                        event.getContainer().setNewDamage(0);
                    }
                }
            }
        }
    }
}
