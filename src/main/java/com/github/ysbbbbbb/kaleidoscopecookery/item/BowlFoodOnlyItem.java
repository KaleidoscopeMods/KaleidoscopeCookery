package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BowlFoodOnlyItem extends FoodWithEffectsItem implements IHasContainer {
    public BowlFoodOnlyItem(FoodProperties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack itemStack = super.finishUsingItem(stack, level, entity);
        ItemStack bowl = new ItemStack(Items.BOWL);
        if (itemStack.isEmpty()) {
            return bowl;
        }
        if (entity instanceof Player player) {
            player.getInventory().placeItemBackInInventory(bowl);
        } else {
            ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), bowl);
            level.addFreshEntity(itemEntity);
        }
        return itemStack;
    }

    @Override
    public Item getContainerItem() {
        return Items.BOWL;
    }
}
