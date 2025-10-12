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
//import net.minecraftforge.event.entity.living.LivingDamageEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;

public class SatiatedShieldEvent {
    public static void register() {
//        ServerLivingEntityEvents.ALLOW_DAMAGE.register(SatiatedShieldEvent::onPlayerHurt);
    }

//    public static void onPlayerHurt(LivingDamageEvent event) {
//        // 1 伤害扣 2 Exhaustion
//        int amount = Math.round(event.getAmount()) * 2;
//        DamageSource source = event.getSource();
//        if (event.getEntity() instanceof Player player && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
//            if (player.getFoodData().getFoodLevel() > 0 && player.hasEffect(ModEffects.SATIATED_SHIELD.get())) {
//                // 部分特殊伤害，扣除的 Exhaustion 翻倍
//                if (source.is(TagMod.SATIATED_SHIELD_WEAKNESS)) {
//                    amount *= 2;
//                }
//                // 原版是 4 点 Exhaustion 对应 1 点 Food Level
//                player.causeFoodExhaustion(Math.max(0, amount / 4f));
//                event.setCanceled(true);
//            }
//            player.invulnerableTime = 20;
//            return false;
//        }
//        return true;
//    }
}
