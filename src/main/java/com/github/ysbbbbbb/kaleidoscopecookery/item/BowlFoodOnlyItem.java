package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class BowlFoodOnlyItem extends FoodWithEffectsItem implements IHasContainer {
    public BowlFoodOnlyItem(FoodProperties properties) {
        super(new FoodProperties(
                properties.nutrition(),
                properties.saturation(),
                properties.canAlwaysEat(),
                properties.eatSeconds(),
                Optional.of(Items.BOWL.getDefaultInstance()),
                properties.effects())
        );
    }

    public BowlFoodOnlyItem(FoodProperties properties, Item craftingItem) {
        super(properties, craftingItem);
    }

    @Override
    public Item getContainerItem() {
        return Items.BOWL;
    }
}
