package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FlourItem extends Item {
    public FlourItem() {
        super(new Item.Properties());
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        // 每 10 tick 检查一次，如果在水中，就把自己变成同数量的面团
        if (entity.tickCount % 10 == 0) {
            if (entity.isInWater()) {
                ItemStack doughStack = new ItemStack(ModItems.RAW_DOUGH.get(), stack.getCount());
                entity.setItem(doughStack);
                return true;
            }
        }
        return super.onEntityItemUpdate(stack, entity);
    }
}
