package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class ArmorEffectEvent {

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var player : server.getPlayerList().getPlayers()) {
                onLivingTick(player);
            }
        });
    }

    public static void onLivingTick(LivingEntity entity) {
        if (entity.tickCount % 20 != 0) {
            return;
        }
        // 仅在水中生效
        if (!entity.isInWater()) {
            return;
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            // 只要护甲不符合的，就直接返回
            if (slot.isArmor() && !entity.getItemBySlot(slot).is(TagMod.FARMER_ARMOR)) {
                return;
            }
        }
        entity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 25, 0, true, true, false));
    }
}
