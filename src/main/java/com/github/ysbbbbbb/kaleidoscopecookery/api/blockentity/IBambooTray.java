package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 竹匾交互接口，方便其他模组中的实体放入、取出物品。
 */
public interface IBambooTray {
    /**
     * 将物品放入指定位置。
     *
     * @param level 使用者所处的 level
     * @param user  使用者
     * @param stack 要放入的物品
     * @param slot  交互位置，范围为 0-3
     * @return 如果成功放入物品则返回 true，否则返回 false
     */
    boolean onPutItem(Level level, LivingEntity user, ItemStack stack, int slot);

    /**
     * 从指定位置取出物品。
     *
     * @param level   使用者所处的 level
     * @param user    使用者
     * @param slot    交互位置，范围为 0-3
     * @param takeAll 是否整组取出
     * @return 如果成功取出物品则返回 true，否则返回 false
     */
    boolean onTakeOut(Level level, LivingEntity user, int slot, boolean takeAll);

    /**
     * 获取指定位置的物品。
     *
     * @param slot 交互位置，范围为 0-3
     * @return 指定位置的物品
     */
    ItemStack getItem(int slot);
}
