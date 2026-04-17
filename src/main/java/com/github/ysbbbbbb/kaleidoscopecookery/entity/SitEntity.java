package com.github.ysbbbbbb.kaleidoscopecookery.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class SitEntity extends Entity implements IEntityWithComplexSpawn {
    public static final EntityType<SitEntity> TYPE = EntityType.Builder.<SitEntity>of(SitEntity::new, MobCategory.MISC)
            .sized(0.5f, 0.1f)
            .clientTrackingRange(10)
            .noSummon()
            .build("sit");

    public static final int DEFAULT = 0;
    public static final int TRASH_CAN = 1;

    private int passengerTick = 0;
    /**
     * 座位类型，用来处理不同方块生成的实体，从而做出特殊的内容
     */
    private int sitType = DEFAULT;

    public SitEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public SitEntity(Level worldIn, BlockPos pos) {
        this(TYPE, worldIn);
        this.setPos(pos.getX() + 0.5, pos.getY() + 0.4375, pos.getZ() + 0.5);
    }

    public SitEntity(Level worldIn, BlockPos pos, double y) {
        this(TYPE, worldIn);
        this.setPos(pos.getX() + 0.5, pos.getY() + y, pos.getZ() + 0.5);
    }

    public SitEntity(Level worldIn, BlockPos pos, double y, int sitType) {
        this(worldIn, pos, y);
        this.sitType = sitType;
    }

    public SitEntity(Level worldIn, BlockPos pos, int sitType) {
        this(worldIn, pos);
        this.sitType = sitType;
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity entity) {
        return super.getPassengerRidingPosition(entity).add(0, -0.0625, 0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.sitType = tag.getInt("SitType");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("SitType", this.sitType);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            this.checkBelowWorld();
            this.checkPassengers();

            // 每秒检查一次所处位置是否有方块，没有就删除实体
            if (this.tickCount % 20 == 0) {
                BlockState blockState = this.level().getBlockState(this.blockPosition());
                if (!blockState.is(TagMod.SITTABLE)) {
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        // 玩家脱离骑乘垃圾桶实体，此时停止动画，并播放声音
        if (this.getSitType() == SitEntity.TRASH_CAN && passenger instanceof Player player) {
            // 获取垃圾桶
            BlockPos blockPos = this.blockPosition();
            if (level().getBlockEntity(blockPos) instanceof TrashCanBlockEntity trashCan) {
                trashCan.player1State.stop();
                trashCan.player2State.stop();
                player.playSound(ModSounds.TRASH_CAN.get());
            }
        }

        super.removePassenger(passenger);
    }

    private void checkPassengers() {
        if (this.getPassengers().isEmpty()) {
            passengerTick++;
        } else {
            passengerTick = 0;
        }
        if (passengerTick > 10) {
            this.discard();
        }
    }

    @Override
    public boolean skipAttackInteraction(Entity targetEntity) {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damageAmount) {
        return false;
    }

    @Override
    public void move(MoverType moverType, Vec3 movement) {
    }

    @Override
    public void push(Entity pushedEntity) {
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }

    @Override
    public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {
    }

    @Override
    public void refreshDimensions() {
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeVarInt(this.sitType);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        this.sitType = additionalData.readVarInt();
    }

    public int getSitType() {
        return sitType;
    }
}
