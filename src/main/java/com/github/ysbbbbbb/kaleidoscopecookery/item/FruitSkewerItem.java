package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class FruitSkewerItem extends Item implements IHasContainer {
    public FruitSkewerItem(FoodProperties properties) {
        super(new Item.Properties().food(properties));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack itemStack = super.finishUsingItem(stack, level, entity);
        return this.returnContainerToEntity(itemStack, level, entity);
    }

    @Override
    public Item getContainerItem() {
        return Items.STICK;
    }
}
