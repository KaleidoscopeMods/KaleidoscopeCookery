package com.github.ysbbbbbb.kaleidoscopecookery.api.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;

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

    /**
     * 物品被使用完毕后，将容器归还给实体。
     * <p>
     * 若实体为玩家，则直接将容器放入其背包；否则在实体所在位置生成一个掉落物实体。
     * 若传入的 {@code stack} 已为空（数量为 0），则不执行归还操作，直接返回容器的 ItemStack。
     *
     * @param stack  当前正在被消耗的物品栈（shrink 之后传入）
     * @param level  当前所在世界
     * @param entity 消耗物品的实体
     * @return 容器物品的 ItemStack
     */
    default ItemStack returnContainerToEntity(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack carried = this.getContainerItem().getDefaultInstance();
        if (stack.isEmpty()) {
            return carried;
        }
        if (entity instanceof Player player) {
            ItemHandlerHelper.giveItemToPlayer(player, carried);
        } else {
            ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), carried);
            level.addFreshEntity(itemEntity);
        }
        return stack;
    }
}
