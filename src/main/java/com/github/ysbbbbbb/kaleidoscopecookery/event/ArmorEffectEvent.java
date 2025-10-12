//package com.github.ysbbbbbb.kaleidoscopecookery.event;
//
//import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
//import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraftforge.event.entity.living.LivingEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
//public class ArmorEffectEvent {
//    @SubscribeEvent
//    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
//        LivingEntity entity = event.getEntity();
//        if (entity.tickCount % 20 != 0) {
//            return;
//        }
//        // 仅在水中生效
//        if (!entity.isInWater()) {
//            return;
//        }
//        for (EquipmentSlot slot : EquipmentSlot.values()) {
//            // 只要护甲不符合的，就直接返回
//            if (slot.isArmor() && !entity.getItemBySlot(slot).is(TagMod.FARMER_ARMOR)) {
//                return;
//            }
//        }
//        entity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 25, 0, true, true, false));
//    }
//}
