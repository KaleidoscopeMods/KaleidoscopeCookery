package com.github.ysbbbbbb.kaleidoscopecookery.util;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.util.neo.IItemHandler;
import com.github.ysbbbbbb.kaleidoscopecookery.util.neo.PlayerMainInvWrapper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

public class ItemUtils {
    public static void getItemToLivingEntity(LivingEntity entity, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        if (entity.getMainHandItem().isEmpty()) {
            RandomSource random = entity.level().random;
            entity.setItemInHand(InteractionHand.MAIN_HAND, stack);
            entity.playSound(SoundEvents.ITEM_PICKUP, 0.2F, ((random.nextFloat() - random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        } else if (entity instanceof Player player) {
            player.getInventory().placeItemBackInInventory(stack);
        } else {
            // 否则直接在实体所处位置生成物品
            ItemEntity dropItem = entity.spawnAtLocation(stack);
            if (dropItem != null) {
                dropItem.setPickUpDelay(0);
            }
        }
    }

    public static void getItemToLivingEntity(LivingEntity entity, ItemStack stack, int preferredSlot) {
        if (stack.isEmpty()) {
            return;
        }
        if (entity.getMainHandItem().isEmpty()) {
            RandomSource random = entity.level().random;
            entity.setItemInHand(InteractionHand.MAIN_HAND, stack);
            entity.playSound(SoundEvents.ITEM_PICKUP, 0.2F, ((random.nextFloat() - random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        } else if (entity instanceof Player player) {
            giveItemToPlayer(player, stack, preferredSlot);
        } else {
            // 否则直接在实体所处位置生成物品
            ItemEntity dropItem = entity.spawnAtLocation(stack);
            if (dropItem != null) {
                dropItem.setPickUpDelay(0);
            }
        }
    }

    public static Pair<Integer, ItemStack> getLastStack(NonNullList<ItemStack> itemList) {
        for (int i = itemList.size(); i > 0; i--) {
            int index = i - 1;
            ItemStack stack = itemList.get(index);
            if (!stack.isEmpty()) {
                return Pair.of(index, stack);
            }
        }
        return Pair.of(0, ItemStack.EMPTY);
    }

    public static void giveItemToPlayer(Player player, ItemStack stack) {
        giveItemToPlayer(player, stack, -1);
    }

    public static void giveItemToPlayer(Player player, ItemStack stack, int preferredSlot) {
        if (!stack.isEmpty()) {
            IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());
            Level level = player.level();
            ItemStack remainder = stack;
            if (preferredSlot >= 0 && preferredSlot < inventory.getSlots()) {
                remainder = inventory.insertItem(preferredSlot, stack, false);
            }

            if (!remainder.isEmpty()) {
                remainder = insertItemStacked(inventory, remainder, false);
            }

            if (remainder.isEmpty() || remainder.getCount() != stack.getCount()) {
                level.playSound(null, player.getX(), player.getY() + (double)0.5F, player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }

            if (!remainder.isEmpty() && !level.isClientSide) {
                ItemEntity entityItem = new ItemEntity(level, player.getX(), player.getY() + (double)0.5F, player.getZ(), remainder);
                entityItem.setPickUpDelay(40);
                entityItem.setDeltaMovement(entityItem.getDeltaMovement().multiply(0.0F, 1.0F, 0.0F));
                level.addFreshEntity(entityItem);
            }

        }
    }

    public static ItemStack insertItem(IItemHandler dest, ItemStack stack, boolean simulate) {
        if (dest != null && !stack.isEmpty()) {
            for(int i = 0; i < dest.getSlots(); ++i) {
                stack = dest.insertItem(i, stack, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }

        }
        return stack;
    }

    public static ItemStack insertItemStacked(IItemHandler inventory, ItemStack stack, boolean simulate) {
        if (inventory != null && !stack.isEmpty()) {
            if (!stack.isStackable()) {
                return insertItem(inventory, stack, simulate);
            } else {
                int sizeInventory = inventory.getSlots();

                for(int i = 0; i < sizeInventory; ++i) {
                    ItemStack slot = inventory.getStackInSlot(i);
                    if (ItemStack.isSameItemSameComponents(slot, stack)) {
                        stack = inventory.insertItem(i, stack, simulate);
                        if (stack.isEmpty()) {
                            break;
                        }
                    }
                }

                if (!stack.isEmpty()) {
                    for(int i = 0; i < sizeInventory; ++i) {
                        if (inventory.getStackInSlot(i).isEmpty()) {
                            stack = inventory.insertItem(i, stack, simulate);
                            if (stack.isEmpty()) {
                                break;
                            }
                        }
                    }
                }

                return stack;
            }
        } else {
            return stack;
        }
    }

    public static Item getContainerItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return Items.AIR;
        }
        FoodProperties foodProperties = stack.get(DataComponents.FOOD);
        if (foodProperties != null) {
            return foodProperties.usingConvertsTo()
                    .map(ItemStack::getItem)
                    .orElse(Items.AIR);
        }
        Item item = stack.getItem();
        ItemStack remainingItem;
        if (item.hasCraftingRemainingItem()) {
            assert item.getCraftingRemainingItem() != null;
            remainingItem = new ItemStack(item.getCraftingRemainingItem());
        } else {
            remainingItem = ItemStack.EMPTY;
        }
        if (!remainingItem.isEmpty()) {
            return remainingItem.getItem();
        }
        if (item instanceof IHasContainer hasContainer) {
            return hasContainer.getContainerItem();
        } else if (stack.is(TagMod.BOWL_CONTAINER)) {
            return Items.BOWL;
        } else if (stack.is(TagMod.GLASS_BOTTLE_CONTAINER)) {
            return Items.GLASS_BOTTLE;
        } else if (stack.is(TagMod.BUCKET_CONTAINER)) {
            return Items.BUCKET;
        } else if (stack.is(Items.POTION)) {
            return Items.GLASS_BOTTLE;
        }
        return Items.AIR;
    }
}
