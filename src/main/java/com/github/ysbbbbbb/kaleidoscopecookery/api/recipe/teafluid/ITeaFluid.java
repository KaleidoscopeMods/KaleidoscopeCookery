package com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teafluid;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTeaFluids;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public interface ITeaFluid {
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
     * 装有该茶水的茶壶方块在非沸腾时是否生成粒子效果
     */
    default boolean spawnParticles() {
        return false;
    }

    /**
     * 在玩家拿着物品右击茶壶时调用，用来判断手持物是否是对应茶水
     *
     * @param stack 手持物
     * @return 如果是则返回 true，否则返回 false
     */
    default boolean isTeaFluid(ItemStack stack) {
        return false;
    }

    /**
     * 在玩家拿着茶壶右击方块时调用，用以判断向目标方块倒茶时是瞬间完成，还是要播放一段动画
     *
     * @param context UseOn上下文
     */
    default boolean instantPouring(UseOnContext context) { return false; }

    /**
     * 该种液体是否能在茶壶中作为煮茶的基底
     */
    boolean isTeaBase();

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

    default boolean isEmpty() {
        return getName().equals(ModTeaFluids.EMPTY);
    }
}
