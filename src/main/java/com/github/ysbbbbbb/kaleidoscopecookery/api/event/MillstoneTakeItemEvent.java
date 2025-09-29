package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class MillstoneTakeItemEvent extends Event implements ICancellableEvent {
    private final LivingEntity user;
    private final ItemStack heldItem;
    private final MillstoneBlockEntity millstone;

    private boolean result = true;

    public MillstoneTakeItemEvent(LivingEntity user, ItemStack heldItem, MillstoneBlockEntity millstone) {
        this.user = user;
        this.heldItem = heldItem;
        this.millstone = millstone;
    }

    public LivingEntity getUser() {
        return user;
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public MillstoneBlockEntity getMillstone() {
        return millstone;
    }

    public boolean isSuccess() {
        return result;
    }

    /**
     * 设置返回值，如果为 true 表示取出成功，会在客户端有一个右键使用的动画。
     * 返回为 false 则没有
     */
    public void setResult(boolean result) {
        this.result = result;
    }
}
