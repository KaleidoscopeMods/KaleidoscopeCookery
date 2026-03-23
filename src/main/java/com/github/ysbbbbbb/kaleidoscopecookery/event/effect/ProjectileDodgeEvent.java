package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class ProjectileDodgeEvent {
    // 闪避消耗的持续时间 (tick)
    private static final int DODGE_COST = 200;

    @SubscribeEvent
    public static void onProjectileHit(ProjectileImpactEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        HitResult hit = event.getRayTraceResult();
        if (hit.getType().equals(HitResult.Type.ENTITY) && ((EntityHitResult) hit).getEntity() instanceof LivingEntity living) {
            if (living.hasEffect(ModEffects.PROJECTILE_DODGE.get())) {
                // 取消弹射物碰撞并随机传送
                event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
                randomTeleport(living.level(), living, 3, 16);

                // 消耗持续时间
                MobEffectInstance instance = living.getEffect(ModEffects.PROJECTILE_DODGE.get());
                if (instance != null) {
                    if (instance.getDuration() <= DODGE_COST) {
                        living.removeEffect(ModEffects.PROJECTILE_DODGE.get());
                    } else {
                        instance.duration -= DODGE_COST;
                        living.forceAddEffect(instance, null);
                    }
                }
            }
        }
    }

    /**
     * 随机传送目标
     *
     * @param level       目标所在的世界
     * @param living      目标
     * @param range       传送半径
     * @param maxAttempts 最大尝试次数
     */
    public static void randomTeleport(Level level, LivingEntity living, double range, int maxAttempts) {
        if (!level.isClientSide) {
            double d0 = living.getX();
            double d1 = living.getY();
            double d2 = living.getZ();

            for(int i = 0; i < maxAttempts; ++i) {
                double d3 = living.getX() + (living.getRandom().nextDouble() - 0.5F) * range;
                double d4 = Mth.clamp(living.getY() + (living.getRandom().nextDouble() - 0.5D) * range, level.getMinBuildHeight(), (level.getMinBuildHeight() + ((ServerLevel)level).getLogicalHeight() - 1));
                double d5 = living.getZ() + (living.getRandom().nextDouble() - 0.5D) * range;
                if (living.isPassenger()) {
                    living.stopRiding();
                }

                Vec3 vec3 = living.position();
                level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(living));
                if (living.randomTeleport(d3, d4, d5, true)) {
                    SoundEvent soundevent = SoundEvents.ENDERMAN_TELEPORT;
                    level.playSound(null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    living.playSound(soundevent, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }
}
