package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.RecipeItemEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.FruitBasketItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class SpecialRecipeItemEvent {
    @SubscribeEvent
    public static void onCheckItemEvent(RecipeItemEvent.CheckItem event) {
        ItemStack stack = event.getStack();

        // 果篮和饭袋，懒得写 cap 了
        if (stack.is(ModItems.FRUIT_BASKET.get())) {
            ItemStackHandler items = FruitBasketItem.getItems(stack);
            addItems(event, items);
            FruitBasketItem.saveItems(stack, items);
            return;
        }
        if (stack.is(ModItems.TRANSMUTATION_LUNCH_BAG.get())) {
            ItemStackHandler items = TransmutationLunchBagItem.getItems(stack);
            addItems(event, items);
            TransmutationLunchBagItem.setItems(stack, items);
            return;
        }

        // 最后检查 cap
        stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> addItems(event, handler));
    }

    @SubscribeEvent
    public static void onDeductItemEvent(RecipeItemEvent.DeductItem event) {
        ItemStack stack = event.getStack();

        // 果篮和饭袋，懒得写 cap 了
        if (stack.is(ModItems.FRUIT_BASKET.get())) {
            ItemStackHandler items = FruitBasketItem.getItems(stack);
            deductItems(event, items);
            FruitBasketItem.saveItems(stack, items);
            return;
        }
        if (stack.is(ModItems.TRANSMUTATION_LUNCH_BAG.get())) {
            ItemStackHandler items = TransmutationLunchBagItem.getItems(stack);
            deductItems(event, items);
            TransmutationLunchBagItem.setItems(stack, items);
            return;
        }

        // 最后检查 cap
        stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> deductItems(event, handler));
    }

    private static void addItems(RecipeItemEvent.CheckItem event, IItemHandler items) {
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack slotStack = items.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                event.addItem(slotStack.getItem(), slotStack.getCount());
            }
        }
    }

    private static void deductItems(RecipeItemEvent.DeductItem event, IItemHandler items) {
        Item needItem = event.getNeedItem();
        for (int i = 0; i < items.getSlots(); i++) {
            int needCount = event.getNeedCount();
            if (needCount <= 0) {
                return;
            }
            ItemStack slotStack = items.getStackInSlot(i);
            if (slotStack.is(needItem)) {
                ItemStack extractItem = items.extractItem(i, needCount, false);
                event.deduct(extractItem.getCount());
            }
        }
    }
}
