package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.ysbbbbbb.kaleidoscopecookery.config.GeneralConfig.SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE;

@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class SatiatedShieldEvent {
    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent event) {
        // 1 伤害扣 2 Exhaustion
        int amount = Math.round(event.getAmount()) * 2;
        DamageSource source = event.getSource();
        if (event.getEntity() instanceof Player player && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            // 如果没有开启饱腹代偿，直接返回
            if (!GeneralConfig.SATIATED_SHIELD_ABSORB_ENABLED.get()) {
                return;
            }

            if (player.getFoodData().getFoodLevel() > 0 && player.hasEffect(ModEffects.SATIATED_SHIELD.get())) {
                // 部分特殊伤害，扣除的 Exhaustion 翻倍
                if (source.is(TagMod.SATIATED_SHIELD_WEAKNESS)) {
                    amount *= 2;
                }
                // 原版是 4 点 Exhaustion 对应 1 点 Food Level
                float exhaustionLevel = Math.max(0, amount / 4f);
                float playerFoodLevel = player.getFoodData().getFoodLevel();
                player.causeFoodExhaustion(exhaustionLevel);

                if (SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE.get()) {
                    event.setCanceled(true);
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
                        float remainingDamage = event.getAmount() - extraDamage;
                        event.setAmount(Math.max(0, remainingDamage));
                    } else {
                        event.setAmount(0);
                    }
                }
            }
        }
    }
}
