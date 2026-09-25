package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 把切菜板单独拆分出一个接口类，方便其他模组操纵，比如女仆
 */
public interface IChoppingBoard {
    /**
     * 放置物品到切菜板上
     *
     * @param level     使用者所在的世界
     * @param putOnItem 打算放置的物品
     * @return 放置是否成功
     */
    boolean onPutItem(Level level, @Nullable LivingEntity user, ItemStack putOnItem);

    /**
     * 从切菜板上取出物品，注意，最终的成品取出是在 onCutItem 方法内执行的
     *
     * @param level 使用者所在的世界
     * @param user  使用者
     * @return 取出是否成功
     */
    boolean onTakeOut(Level level, LivingEntity user);

    /**
     * 切菜逻辑
     *
     * @param level      使用者所在的世界
     * @param user       使用者
     * @param cutterItem 切菜工具
     * @return 切菜是否成功
     */
    boolean onCutItem(Level level, @Nullable LivingEntity user, ItemStack cutterItem);

    /**
     * 播放切菜板时的音效和粒子
     */
    void playParticlesSound();
}
