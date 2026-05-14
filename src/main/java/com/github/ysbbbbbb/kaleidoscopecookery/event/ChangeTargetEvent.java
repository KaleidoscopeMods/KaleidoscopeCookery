package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class ChangeTargetEvent {
    @SubscribeEvent
    public static void onTarget(LivingChangeTargetEvent event) {
        LivingEntity newTarget = event.getNewTarget();
        if (newTarget instanceof Player player && player.getVehicle() instanceof SitEntity sitEntity && sitEntity.getSitType() == SitEntity.TRASH_CAN) {
            // 怪物直接无视坐在垃圾桶里的人
            event.setCanceled(true);
        }
    }
}
