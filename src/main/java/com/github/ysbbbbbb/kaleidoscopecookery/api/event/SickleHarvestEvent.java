package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * 镰刀收割事件，在使用镰刀尝试检查方块是否可破坏时触发
 * <p>
 * 可以取消，取消后当前方块不会执行镰刀默认的收割行为
 */
public class SickleHarvestEvent extends PlayerEvent implements ICancellableEvent {
    private final ItemStack sickle;
    private final BlockPos harvestPos;
    private final BlockState harvestState;
    private boolean costDurability = false;

    public SickleHarvestEvent(Player player, ItemStack sickle, BlockPos harvestPos, BlockState harvestState) {
        super(player);
        this.sickle = sickle;
        this.harvestPos = harvestPos;
        this.harvestState = harvestState;
    }

    public ItemStack getSickle() {
        return sickle;
    }

    public BlockPos getHarvestPos() {
        return harvestPos;
    }

    public BlockState getHarvestState() {
        return harvestState;
    }

    public boolean isCostDurability() {
        return costDurability;
    }

    /**
     * 此次收割是否消耗镰刀耐久度，默认不消耗
     */
    public void setCostDurability(boolean costDurability) {
        this.costDurability = costDurability;
    }
}
