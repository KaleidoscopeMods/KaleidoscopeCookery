package com.github.ysbbbbbb.kaleidoscopecookery.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ThrowableBaoziEntity extends ThrowableItemProjectile {
    public static final EntityType<ThrowableBaoziEntity> TYPE = EntityType.Builder
            .<ThrowableBaoziEntity>of(ThrowableBaoziEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build("throwable_baozi");

    public ThrowableBaoziEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThrowableBaoziEntity(EntityType<? extends ThrowableItemProjectile> entityType, double x, double y, double z, Level level) {
        super(entityType, x, y, z, level);
    }

    public ThrowableBaoziEntity(EntityType<? extends ThrowableItemProjectile> entityType, LivingEntity shooter, Level level) {
        super(entityType, shooter, level);
    }

    public ThrowableBaoziEntity(Level level, LivingEntity shooter) {
        super(TYPE, shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BAOZI.get();
    }

    @Override
    public void handleEntityEvent(byte id) {
        ItemStack entityStack = new ItemStack(this.getDefaultItem());
        if (id == EntityEvent.DEATH) {
            ParticleOptions option = new ItemParticleOption(ParticleTypes.ITEM, entityStack);
            for (int i = 0; i < 12; i++) {
                this.level().addParticle(option, this.getX(), this.getY(), this.getZ(),
                        (this.random.nextFloat() * 2 - 1) * 0.1,
                        (this.random.nextFloat() * 2 - 1) * 0.1 + 0.1,
                        (this.random.nextFloat() * 2 - 1) * 0.1);
            }
        }

        if (id == EntityEvent.LOVE_HEARTS) {
            for (int i = 0; i < 7; i++) {
                double offsetX = (this.random.nextDouble() - 0.5) * 0.5;
                double offsetY = this.random.nextDouble() * 0.5 + 0.5;
                double offsetZ = (this.random.nextDouble() - 0.5) * 0.5;
                this.level().addParticle(ParticleTypes.HEART,
                        this.getX() + offsetX,
                        this.getY() + offsetY,
                        this.getZ() + offsetZ,
                        0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity hitEntity = entityHitResult.getEntity();
        hitEntity.hurt(this.damageSources().thrown(this, this.getOwner()), 0);
        this.playSound(SoundEvents.SNOW_HIT, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

        // 如果是狗，那么直接回满狗的血
        if (hitEntity instanceof Wolf wolf) {
            wolf.heal(wolf.getMaxHealth());
            // 生成爱心粒子
            this.level().broadcastEntityEvent(this, EntityEvent.LOVE_HEARTS);
            // 触发成就
            if (this.getOwner() instanceof ServerPlayer player) {
                ModTrigger.EVENT.trigger(player, ModEventTriggerType.MEAT_BUNS_BEAT_DOGS);
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.playSound(SoundEvents.SNOW_HIT, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.discard();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
