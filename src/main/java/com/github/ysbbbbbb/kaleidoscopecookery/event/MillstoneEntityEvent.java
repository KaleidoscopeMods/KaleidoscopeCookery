package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class MillstoneEntityEvent {
    @SubscribeEvent
    public static void onLivingAttack(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) {
            return;
        }
        BlockPos origin = entity.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-5, -2, -5), origin.offset(5, 2, 5))) {
            if (entity.level().getBlockEntity(pos) instanceof MillstoneBlockEntity millstone
                    && millstone.isBoundTo(entity)) {
                millstone.unbindEntity();
                millstone.setCacheRot(millstone.getRotation(entity.level(), 0));
                millstone.refresh();
                return;
            }
        }
    }
}
