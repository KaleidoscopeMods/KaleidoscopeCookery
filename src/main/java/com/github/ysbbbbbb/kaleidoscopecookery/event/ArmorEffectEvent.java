package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import static net.minecraft.world.entity.EquipmentSlot.Type.HUMANOID_ARMOR;

@EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class ArmorEffectEvent {
    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }
        if (entity.tickCount % 20 != 0) {
            return;
        }
        // 仅在水中生效
        if (!entity.isInWater()) {
            return;
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            // 只要护甲不符合的，就直接返回
            if (slot.getType() == HUMANOID_ARMOR && !entity.getItemBySlot(slot).is(TagMod.FARMER_ARMOR)) {
                return;
            }
        }
        entity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 25, 0, true, true, false));
    }
}
