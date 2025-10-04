package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISteamer {
    /**
     * 检查锅下方是否有热源
     *
     * @param level 使用者所处的 level
     * @return 如果有热源则返回 true，否则返回 false
     */
    boolean hasHeatSource(Level level);

    /**
     * 更新火力等级
     *
     * @param level 使用者所处的 level
     */
    void updateLitLevel(Level level);

    /**
     * 取出食物
     *
     * @param level 使用者所处的 level
     * @param user  使用者
     * @return 如果取出成功则返回 true，否则返回 false
     */
    boolean takeFood(Level level, LivingEntity user);

    /**
     * 放入食物
     *
     * @param level 使用者所处的 level
     * @param user  使用者
     * @param food  要放入的食物
     * @return 如果放入成功则返回 true，否则返回 false
     */
    boolean placeFood(Level level, LivingEntity user, ItemStack food);
}
