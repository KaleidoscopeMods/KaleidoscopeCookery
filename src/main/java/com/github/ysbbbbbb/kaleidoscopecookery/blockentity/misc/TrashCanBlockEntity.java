package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc;

import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.TrashCanBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class TrashCanBlockEntity extends BaseBlockEntity {
    private final ItemStackHandler storage = new ItemStackHandler(3);

    public AnimationState putState = new AnimationState();
    public AnimationState withdrawState = new AnimationState();
    public AnimationState player1State = new AnimationState();
    public AnimationState player2State = new AnimationState();
    public AnimationState enterState = new AnimationState();

    public TrashCanBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.TRASH_CAN_BE.get(), pos, blockState);
    }

    public void clientTick(Level level) {
        long offset = level.getGameTime() + worldPosition.hashCode();

        // 每 61 tick 播放刷新
        if (Math.floorMod(offset, 61) == 0) {
            List<SitEntity> sits = level.getEntitiesOfClass(SitEntity.class, new AABB(this.worldPosition));
            if (!sits.isEmpty()) {
                if (level.random.nextBoolean()) {
                    this.player2State.stop();
                    this.player1State.start((int) level.getGameTime());
                } else {
                    this.player1State.stop();
                    this.player2State.start((int) level.getGameTime());
                }
            }
        }

        // 每 5 tick 刷新一次停止
        if (Math.floorMod(offset, 5) == 0) {
            List<SitEntity> sits = level.getEntitiesOfClass(SitEntity.class, new AABB(this.worldPosition));
            if (sits.isEmpty()) {
                this.player1State.stop();
                this.player2State.stop();
            }
        }
    }

    public void entityInside(Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity itemEntity)) {
            return;
        }
        AABB entityBox = entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ());
        VoxelShape shape = Shapes.create(entityBox);
        if (!Shapes.joinIsNotEmpty(shape, TrashCanBlock.SUCK_ZONE, BooleanOp.AND)) {
            return;
        }
        if (!hasItem(itemEntity.getItem())) {
            return;
        }
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ(),
                    1, 0.1, 0.1, 0.1, 0.01);
        }
        entity.discard();
    }

    /**
     * 添加物品，如果满了的话，会把最开始的物品删除，而后塞入新的物品
     *
     * @param stack 准备塞入的物品
     */
    public void putItem(ItemStack stack) {
        ItemStack result = ItemHandlerHelper.insertItemStacked(storage, stack.copy(), false);
        // 如果物品没有减少，说明满了，需要重置垃圾桶，删除最开始的物品，并把后面的物品往前挪
        if (result.getCount() == stack.getCount()) {
            storage.setStackInSlot(0, storage.getStackInSlot(1).copy());
            storage.setStackInSlot(1, storage.getStackInSlot(2).copy());
            storage.setStackInSlot(2, stack.copy());
            // 扣除全部
            stack.shrink(stack.getCount());
        }
        // 如果物品变少了，那么正常扣除
        if (result.getCount() < stack.getCount()) {
            stack.shrink(stack.getCount() - result.getCount());
        }
        if (level instanceof ServerLevel serverLevel) {
            BlockPos pos = this.getBlockPos();
            serverLevel.sendParticles(ParticleTypes.SMOKE,
                    pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5,
                    3, 0.25, 0.05, 0.25, 0.01);
            serverLevel.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 1.0F, 0.5f);
        }
        if (level != null) {
            this.putState.start((int) level.getGameTime());
        }
        this.refresh();
    }

    /**
     * 倒序查询，找到一个能够返还的物品
     */
    public void withdrawItem(LivingEntity user) {
        for (int i = storage.getSlots() - 1; i >= 0; i--) {
            ItemStack stack = storage.getStackInSlot(i);
            if (!stack.isEmpty()) {
                user.setItemInHand(InteractionHand.MAIN_HAND, stack.copy());
                this.storage.setStackInSlot(i, ItemStack.EMPTY);
                if (level instanceof ServerLevel serverLevel) {
                    BlockPos pos = this.getBlockPos();
                    serverLevel.sendParticles(ParticleTypes.SMOKE,
                            pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5,
                            3, 0.25, 0.05, 0.25, 0.01);
                    serverLevel.playSound(null, pos, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 1.0F, 0.8F);
                }
                if (level != null) {
                    this.withdrawState.start((int) level.getGameTime());
                }
                this.refresh();
                return;
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Storage", storage.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.storage.deserializeNBT(registries, tag.getCompound("Storage"));
    }

    private boolean hasItem(ItemStack itemStack) {
        // 先尝试塞入
        for (int i = 0; i < storage.getSlots(); i++) {
            if (storage.getStackInSlot(i).is(itemStack.getItem())) {
                ItemStack result = storage.insertItem(i, itemStack.copy(), false);
                // 如果成功存入
                if (result.getCount() < itemStack.getCount()) {
                    this.refresh();
                }
                return true;
            }
        }
        return false;
    }

    public ItemStackHandler getStorage() {
        return storage;
    }
}
