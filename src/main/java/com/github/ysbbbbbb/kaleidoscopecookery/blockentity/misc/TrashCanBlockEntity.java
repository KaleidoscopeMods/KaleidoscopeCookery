package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc;

import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.TrashCanBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class TrashCanBlockEntity extends BaseBlockEntity {
    private final ItemStackHandler storage = new ItemStackHandler(3);

    public AnimationState putState = new AnimationState();
    public AnimationState withdrawState = new AnimationState();

    public TrashCanBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.TRASH_CAN_BE.get(), pos, blockState);
    }

    public void tick(Level level) {
        // 每 41 tick 检查一次
        long offset = level.getGameTime() + worldPosition.hashCode();
        if (Math.floorMod(offset, 41) == 0) {
            // 必须处于充能状态才会销毁物品
            if (!this.getBlockState().getValue(TrashCanBlock.POWERED)) {
                return;
            }
            // 先检查区域内的物品
            AABB area = new AABB(worldPosition.above());
            List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, area, e -> hasItem(e.getItem()));
            // 然后销毁
            for (ItemEntity entity : entities) {
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.CLOUD,
                            entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ(),
                            3, 0.1, 0.1, 0.1, 0.01);
                }
                entity.discard();
            }
        }
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
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Storage", storage.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.storage.deserializeNBT(tag.getCompound("Storage"));
    }

    private boolean hasItem(ItemStack itemStack) {
        // 先尝试塞入
        for (int i = 0; i < storage.getSlots(); i++) {
            if (storage.getStackInSlot(i).is(itemStack.getItem())) {
                storage.insertItem(i, itemStack, false);
                return true;
            }
        }
        return false;
    }

    public ItemStackHandler getStorage() {
        return storage;
    }
}
