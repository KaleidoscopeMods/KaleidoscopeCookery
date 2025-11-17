package com.github.ysbbbbbb.kaleidoscopecookery.api.item;

import net.minecraft.world.item.Item;

/**
 * 标记会返还容器的物品
 */
public interface IHasContainer {
    /**
     * 获取容器物品
     *
     * @return 容器物品
     */
    Item getContainerItem();
}
