package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
    /**
     * 闪避消耗的持续时间 (tick)
     */
    private static final int DODGE_COST = 200;

    @SubscribeEvent
    public static void onProjectileHit(ProjectileImpactEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        HitResult hit = event.getRayTraceResult();
        if (hit instanceof EntityHitResult hitResult
            && hitResult.getEntity() instanceof LivingEntity living
            && living.hasEffect(ModEffects.PROJECTILE_DODGE.get())
        ) {
            // 取消弹射物碰撞并随机传送
            event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
            randomTeleport(living.level(), living, 0.5, 2, 16);

            // 消耗持续时间
            MobEffectInstance instance = living.getEffect(ModEffects.PROJECTILE_DODGE.get());
            if (instance != null) {
                // 如果是无限时间，不扣除
                if (instance.isInfiniteDuration()){
                    return;
                }
                if (instance.getDuration() <= DODGE_COST) {
                    living.removeEffect(ModEffects.PROJECTILE_DODGE.get());
                } else {
                    instance.duration -= DODGE_COST;
                    living.forceAddEffect(instance, null);
                }
            }
        }
    }

    /**
     * 随机传送目标
     *
     * @param level       目标所在的世界
     * @param living      目标
     * @param minDistance 最小传送距离
     * @param maxDistance 最大传送距离
     * @param maxAttempts 最大尝试次数
     */
    public static void randomTeleport(Level level, LivingEntity living, double minDistance, double maxDistance, int maxAttempts) {
        if (level.isClientSide) {
            return;
        }

        double x = living.getX();
        double y = living.getY();
        double z = living.getZ();
        int minH = level.getMinBuildHeight();
        int maxH = ((ServerLevel) level).getLogicalHeight();

        for (int i = 0; i < maxAttempts; ++i) {
            double targetX = x + randomBetween(living.getRandom(), minDistance, maxDistance);
            double targetY = Mth.clamp(y + randomBetween(living.getRandom(), minDistance, maxDistance), minH, minH + maxH - 1);
            double targetZ = z + randomBetween(living.getRandom(), minDistance, maxDistance);

            if (living.isPassenger()) {
                living.stopRiding();
            }

            Vec3 previousPos = living.position();
            level.gameEvent(GameEvent.TELEPORT, previousPos, GameEvent.Context.of(living));
            if (living.randomTeleport(targetX, targetY, targetZ, true)) {
                SoundEvent soundEvent = SoundEvents.ENDERMAN_TELEPORT;
                level.playSound(null, x, y, z, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
                living.playSound(soundEvent, 1.0F, 1.0F);
                break;
            }
        }
    }

    private static double randomBetween(RandomSource random, double min, double max) {
        double value = (random.nextDouble() - 0.5) * (max - min);
        value = min * Mth.sign(value) + value;
        return value;
    }
}
