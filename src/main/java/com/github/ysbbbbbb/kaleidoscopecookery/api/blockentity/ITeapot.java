package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ITeapot {
    int PUT_INGREDIENT = 0;
    int BOILING = 1;
    int FINISHED = 2;

    /**
     * 获取当前状态
     *
     * @return 数字表示的状态
     */
    int getStatus();

    /**
     * 检查锅下方是否有热源
     *
     * @param level 使用者所处的 level
     * @return 如果有热源则返回 true，否则返回 false
     */
    boolean hasHeatSource(Level level);

    boolean addTeaFluid(Level level, LivingEntity user, ItemStack itemStack);

    boolean addIngredient(Level level, LivingEntity user, ItemStack itemStack);

    boolean removeIngredient(Level level, LivingEntity user);

    boolean takeTeapot(Level level, LivingEntity user);
}
