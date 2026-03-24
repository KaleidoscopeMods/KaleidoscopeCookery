package com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public interface ITeaType {
    /**
     * 注册 ID，不能重复
     *
     * @return 注册 ID
     */
    ResourceLocation getName();

    /**
     * 装有该茶水的茶壶在物品栏显示的耐久度条颜色
     *
     * @return RGB 颜色值
     */
    int getBarColor();

    /**
     * 主要用于 JEI 显示，表示该茶对应的物品
     */
    ItemStack getDisplayStack();


    /**
     * 装有该茶水的茶壶方块在非煮茶时是否生成粒子效果
     */
    default boolean doSpawnParticles() {
        return false;
    }

    /**
     * 在玩家向方块倒茶时调用
     *
     * @param level  当前世界
     * @param hit    方块命中结果
     * @param user   使用者
     * @param teapot 茶壶物品
     * @return 消耗的茶水份数,0表示操作失败
     */
    int onPouredOnBlock(Level level, BlockHitResult hit, @Nullable LivingEntity user, ItemStack teapot);

    /**
     * 在玩家向实体倒茶时调用
     *
     * @param level  当前世界
     * @param entity 目标实体
     * @param user   使用者
     * @param teapot 茶壶
     * @return 消耗的茶水份数,0表示操作失败
     */
    int onPouredOnEntity(Level level, LivingEntity entity, @Nullable LivingEntity user, ItemStack teapot);
}
