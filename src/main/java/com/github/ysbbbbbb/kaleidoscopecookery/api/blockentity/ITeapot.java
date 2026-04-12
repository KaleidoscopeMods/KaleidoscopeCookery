package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ITeapot {
    /**
     * 放入原料阶段，此时可以取出素材
     */
    int PUT_INGREDIENT = 0;
    /**
     * 此时无法取出素材，强制破坏会丢失所有内容
     */
    int PROCESSING = 1;
    /**
     * 可以正常取下，包含最终成品
     */
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

    /**
     * 向茶壶内手动添加流体
     *
     * @param level     使用者所处的 level
     * @param user      使用者
     * @param itemStack 必须是存储流体的容器
     * @return 是否成功投入流体
     */
    boolean addTeaFluid(Level level, LivingEntity user, ItemStack itemStack);

    /**
     * 向茶壶内手动添加原料
     *
     * @param level     使用者所处的 level
     * @param user      使用者
     * @param itemStack 原料
     * @return 是否成功投料
     */
    boolean addIngredient(Level level, LivingEntity user, ItemStack itemStack);

    /**
     * 取出原料
     *
     * @param level 使用者所处的 level
     * @param user  使用者
     * @return 是否成功取出
     */
    boolean removeIngredient(Level level, LivingEntity user);

    /**
     * 将茶壶直接取下，变成物品形态
     * <p>
     * 当茶壶处于完成状态时，才能取下，其他状态返回 false
     *
     * @param level 使用者所处的 level
     * @param user  使用者
     * @return 成功取下返回 true
     */
    boolean takeTeapot(Level level, LivingEntity user);
}
