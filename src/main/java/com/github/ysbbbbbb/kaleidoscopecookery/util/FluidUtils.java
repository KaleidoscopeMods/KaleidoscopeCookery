package com.github.ysbbbbbb.kaleidoscopecookery.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidUtils {
    /**
     * 类似于 forge 提供的 FluidUtil 里的方法，但不限于玩家实体
     * <p>
     * 该方法尝试将流体从物品容器转移到流体容器里，成功返回 true，失败返回 false
     *
     * @param user    任意实体，通常是玩家，但也可以是其他生物
     * @param bucket  物品形态的容器，一般是桶，但也可以是其他模组的容器
     * @param handler 流体容器的能力接口，可以是任何实现了 IFluidHandler 的对象，一般是方块实体
     * @param amount  要转移的流体量，单位为毫桶（mB）
     * @return 如果成功转移了流体，返回 true；如果没有转移任何流体，返回 false
     */
    public static boolean emptyItem(LivingEntity user, ItemStack bucket, IFluidHandler handler, int amount) {
        // 复制一份物品
        ItemStack copy = bucket.copyWithCount(1);
        // 开始把桶中的流体转移到流体容器中
        return FluidUtil.getFluidHandler(copy).map(stackFluid -> {
            // 转移
            FluidStack transfer = FluidUtil.tryFluidTransfer(handler, stackFluid, amount, true);
            // 如果成功转移
            if (!transfer.isEmpty()) {
                // 获取结果
                ItemStack result = stackFluid.getContainer();
                // 扣除实体物品
                if (!(user instanceof Player player) || !player.isCreative()) {
                    bucket.shrink(1);
                }
                // 给实体物品
                ItemUtils.getItemToLivingEntity(user, result);
                // 播放音效
                SoundEvent sound = transfer.getFluid().getFluidType().getSound(transfer, SoundActions.BUCKET_EMPTY);
                if (sound != null) {
                    user.playSound(sound);
                }
                return true;
            }
            return false;
        }).orElse(false);
    }

    /**
     * 类似于 forge 提供的 FluidUtil 里的方法，但不限于玩家实体
     * <p>
     * 该方法尝试将流体从流体容器转移到物品容器里，成功返回 true，失败返回 false
     *
     * @param user    任意实体，通常是玩家，但也可以是其他生物
     * @param bucket  物品形态的容器，一般是桶，但也可以是其他模组的容器
     * @param handler 流体容器的能力接口，可以是任何实现了 IFluidHandler 的对象，一般是方块实体
     * @param amount  要转移的流体量，单位为毫桶（mB）
     * @return 如果成功转移了流体，返回 true；如果没有转移任何流体，返回 false
     */
    public static boolean fillItem(LivingEntity user, ItemStack bucket, IFluidHandler handler, int amount) {
        // 复制一份物品
        ItemStack copy = bucket.copyWithCount(1);
        // 开始把流体容器中的流体转移到桶里
        return FluidUtil.getFluidHandler(copy).map(stackFluid -> {
            // 先模拟转移一次
            FluidStack transfer = FluidUtil.tryFluidTransfer(stackFluid, handler, amount, false);
            // 如果成功转移
            if (!transfer.isEmpty()) {
                // 真正转移液体
                FluidUtil.tryFluidTransfer(stackFluid, handler, amount, true);
                // 获取结果
                ItemStack result = stackFluid.getContainer();
                // 扣除玩家物品
                if (!(user instanceof Player player) || !player.isCreative()) {
                    bucket.shrink(1);
                }
                // 给实体物品
                ItemUtils.getItemToLivingEntity(user, result);
                // 播放音效
                SoundEvent sound = transfer.getFluid().getFluidType().getSound(transfer, SoundActions.BUCKET_FILL);
                if (sound != null) {
                    user.playSound(sound);
                }
                return true;
            }
            return false;
        }).orElse(false);
    }

    public static boolean hasFluid(ItemStack stack) {
        return FluidUtil.getFluidHandler(stack).map(handler -> {
            for (int i = 0; i < handler.getTanks(); i++) {
                if (!handler.getFluidInTank(i).isEmpty()) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }
}
