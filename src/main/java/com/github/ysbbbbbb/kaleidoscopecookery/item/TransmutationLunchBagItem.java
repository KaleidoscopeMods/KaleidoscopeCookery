package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class TransmutationLunchBagItem extends Item {
    public static final ResourceLocation HAS_ITEMS_PROPERTY = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "has_items");
    public static final int NO_ITEMS = 0;
    public static final int HAS_ITEMS = 1;

    private static final int MAX_SIZE = 24;

    public TransmutationLunchBagItem() {
        super((new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @OnlyIn(Dist.CLIENT)
    public static float getTexture(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (!hasItems(stack)) {
            return NO_ITEMS;
        }
        return HAS_ITEMS;
    }

    public static boolean hasItems(ItemStack bag) {
        return bag.has(ModDataComponents.TRANSMUTATION_LUNCH_BAG_ITEMS);
    }

    public static ItemStackHandler getItems(ItemStack bag) {
        ItemContainer container = bag.get(ModDataComponents.TRANSMUTATION_LUNCH_BAG_ITEMS);
        if (container != null) {
            return container.items();
        }
        return new ItemStackHandler(MAX_SIZE);
    }

    public static void setItems(ItemStack bag, ItemStackHandler items) {
        // 先判断是否全空
        boolean allEmpty = true;
        for (int i = 0; i < items.getSlots(); i++) {
            if (!items.getStackInSlot(i).isEmpty()) {
                allEmpty = false;
                break;
            }
        }
        if (allEmpty) {
            bag.remove(ModDataComponents.TRANSMUTATION_LUNCH_BAG_ITEMS);
        } else {
            bag.set(ModDataComponents.TRANSMUTATION_LUNCH_BAG_ITEMS, ItemContainer.of(items));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if (!(blockEntity instanceof FruitBasketBlockEntity fruitBasket)) {
            return super.useOn(context);
        }
        Player player = context.getPlayer();
        if (player == null) {
            return super.useOn(context);
        }
        ItemStack bag = context.getItemInHand();
        ItemStackHandler bagItems = TransmutationLunchBagItem.getItems(bag);
        ItemStackHandler fruitBasketItems = fruitBasket.getItems();

        // 先检查果篮是否为空
        boolean basketEmpty = true;
        for (int i = 0; i < fruitBasketItems.getSlots(); i++) {
            if (!fruitBasketItems.getStackInSlot(i).isEmpty()) {
                basketEmpty = false;
                break;
            }
        }

        // 果篮空了，那么尝试放入物品
        if (hasItems(bag) && basketEmpty) {
            for (int i = 0; i < bagItems.getSlots(); i++) {
                ItemStack stack = bagItems.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem().canFitInsideContainerItems()) {
                    ItemStack remaining = ItemHandlerHelper.insertItemStacked(fruitBasketItems, stack, false);
                    bagItems.extractItem(i, stack.getCount() - remaining.getCount(), false);
                }
            }
            TransmutationLunchBagItem.setItems(bag, bagItems);
            fruitBasket.refresh();
            playRemoveOneSound(player);
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
        }

        // 果篮不为空，尝试取出物品
        for (int i = 0; i < fruitBasketItems.getSlots(); i++) {
            ItemStack stack = fruitBasketItems.getStackInSlot(i);
            if (!stack.isEmpty() && canAdd(stack)) {
                ItemStack remaining = ItemHandlerHelper.insertItemStacked(bagItems, stack, false);
                fruitBasketItems.extractItem(i, stack.getCount() - remaining.getCount(), false);
            }
        }
        TransmutationLunchBagItem.setItems(bag, bagItems);
        fruitBasket.refresh();
        playDropContentsSound(player);
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Player player && player.isSecondaryUseActive()) {
            // 摆动动作时，取出物品
            if (dropContents(stack, player)) {
                this.playDropContentsSound(player);
                return true;
            }
        }
        return super.onEntitySwing(stack, entity, hand);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        // 里面有物品
        if (hasItems(itemInHand)) {
            ItemStackHandler items = getItems(itemInHand);
            for (int i = 0; i < items.getSlots(); i++) {
                if (canConsume(items.getStackInSlot(i))) {
                    player.startUsingItem(hand);
                    return InteractionResultHolder.consume(itemInHand);
                }
            }
        }

        return InteractionResultHolder.fail(itemInHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack bag, Level level, LivingEntity entity) {
        if (!hasItems(bag)) {
            return bag;
        }
        ItemStackHandler items = getItems(bag);
        boolean consumedAny = false;
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stackInSlot = items.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                continue;
            }

            FoodProperties foodProperties = stackInSlot.getItem().getFoodProperties(stackInSlot, null);
            if (foodProperties != null) {
                while (!items.getStackInSlot(i).isEmpty() && entity instanceof Player player
                       && player.getFoodData().getFoodLevel() < 20) {
                    ItemStack consumed = items.extractItem(i, 1, false);
                    consumeOne(consumed, level, entity);
                    consumedAny = true;
                }
            } else if (stackInSlot.is(Items.POTION)) {
                while (!items.getStackInSlot(i).isEmpty()) {
                    ItemStack consumed = items.extractItem(i, 1, false);
                    consumeOne(consumed, level, entity);
                    consumedAny = true;
                }
            }
        }

        if (consumedAny && entity instanceof ServerPlayer player) {
            ModTrigger.EVENT.get().trigger(player, ModEventTriggerType.USE_TRANSMUTATION_LUNCH_BAG);
        }
        setItems(bag, items);
        return bag;
    }

    private static boolean canConsume(ItemStack stack) {
        return !stack.isEmpty() && (stack.has(DataComponents.FOOD) || stack.is(Items.POTION));
    }

    private static void consumeOne(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack returnStack = stack.finishUsingItem(level, entity);
        Item containerItem = ItemUtils.getContainerItem(stack);
        if (returnStack.isEmpty() && containerItem != Items.AIR) {
            returnStack = containerItem.getDefaultInstance();
        }
        if (!returnStack.isEmpty() && (!(entity instanceof Player player) || !player.getAbilities().instabuild)) {
            ItemUtils.getItemToLivingEntity(entity, returnStack);
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        if (!hasItems(stack)) {
            return UseAnim.NONE;
        }
        ItemStackHandler items = getItems(stack);
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stored = items.getStackInSlot(i);
            if (stored.has(DataComponents.FOOD)) {
                return UseAnim.EAT;
            }
            if (stored.is(Items.POTION)) {
                return UseAnim.DRINK;
            }
        }
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack bag, Slot slot, ClickAction action, Player player) {
        if (bag.getCount() != 1 || action != ClickAction.SECONDARY) {
            return false;
        }
        ItemStack clickItem = slot.getItem();
        if (clickItem.isEmpty()) {
            // 当点击的地方为空，那么取出物品
            this.playRemoveOneSound(player);
            removeOne(bag).ifPresent(stack -> add(bag, slot.safeInsert(stack)));
        } else if (clickItem.getItem().canFitInsideContainerItems() && canAdd(clickItem)) {
            // 否则，放入食物
            int addCount = add(bag, clickItem, true);
            if (addCount > 0) {
                ItemStack takeout = slot.safeTake(clickItem.getCount(), addCount, player);
                if (!takeout.isEmpty()) {
                    add(bag, takeout);
                }
                this.playInsertSound(player);
            }
        }
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack bag, ItemStack other, Slot slot, ClickAction action, Player
            player, SlotAccess access) {
        if (bag.getCount() != 1) {
            return false;
        }
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) {
            return false;
        }
        if (other.isEmpty()) {
            removeOne(bag).ifPresent(stack -> {
                this.playRemoveOneSound(player);
                access.set(stack);
            });
        } else {
            int added = add(bag, other);
            if (added > 0) {
                this.playInsertSound(player);
                other.shrink(added);
            }
        }
        return true;
    }

    public static boolean canAdd(ItemStack food) {
        if (food.isEmpty()) {
            return false;
        }
        if (!food.getItem().canFitInsideContainerItems()) {
            return false;
        }
        return food.has(DataComponents.FOOD) || food.has(DataComponents.POTION_CONTENTS);
    }

    private static Optional<ItemStack> removeOne(ItemStack bag) {
        if (!hasItems(bag)) {
            return Optional.empty();
        }
        ItemStackHandler items = getItems(bag);
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack extractItem = items.extractItem(i, items.getSlotLimit(i), false);
            if (!extractItem.isEmpty()) {
                setItems(bag, items);
                return Optional.of(extractItem);
            }
        }
        return Optional.empty();
    }

    private static int add(ItemStack bag, ItemStack food) {
        return add(bag, food, false);
    }

    private static int add(ItemStack bag, ItemStack food, boolean simulate) {
        if (food.isEmpty() || !food.getItem().canFitInsideContainerItems() || !canAdd(food)) {
            return 0;
        }
        int totalCount = food.getCount();

        ItemStackHandler items = getItems(bag);
        ItemStack remaining = ItemHandlerHelper.insertItemStacked(items, food, simulate);

        int addCount = totalCount - (remaining.isEmpty() ? 0 : remaining.getCount());
        if (!simulate && addCount > 0) {
            setItems(bag, items);
        }
        return addCount;
    }

    private static boolean dropContents(ItemStack bag, Player player) {
        if (!hasItems(bag)) {
            return false;
        }
        boolean result = false;
        ItemStackHandler items = getItems(bag);
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stack = items.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            ItemHandlerHelper.giveItemToPlayer(player, stack);
            result = true;
        }
        if (result) {
            items = new ItemStackHandler(MAX_SIZE);
            setItems(bag, items);
        }
        return result;
    }

    private void playRemoveOneSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (!hasItems(stack)) {
            return Optional.empty();
        }
        ItemStackHandler items = getItems(stack);
        return Optional.of(new ItemContainerTooltip(items));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.transmutation_lunch_bag.line1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.transmutation_lunch_bag.line2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.transmutation_lunch_bag.line3").withStyle(ChatFormatting.GRAY));
    }

    public record ItemContainer(ItemStackHandler items) {
        public static ItemContainer of(ItemStackHandler items) {
            ItemStackHandler copy = new ItemStackHandler(MAX_SIZE);
            for (int i = 0; i < Math.min(items.getSlots(), copy.getSlots()); i++) {
                copy.setStackInSlot(i, items.getStackInSlot(i).copy());
            }
            return new ItemContainer(copy);
        }

        public static final Codec<ItemContainer> CODEC = ItemStack.OPTIONAL_CODEC.listOf().xmap(
                list -> {
                    ItemStackHandler handler = new ItemStackHandler(MAX_SIZE);
                    for (int i = 0; i < Math.min(list.size(), handler.getSlots()); i++) {
                        handler.setStackInSlot(i, list.get(i));
                    }
                    return new TransmutationLunchBagItem.ItemContainer(handler);
                },
                container -> {
                    ItemStackHandler handler = container.items();
                    List<ItemStack> output = Lists.newArrayList();
                    for (int i = 0; i < handler.getSlots(); i++) {
                        output.add(handler.getStackInSlot(i));
                    }
                    return output;
                }
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, TransmutationLunchBagItem.ItemContainer> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public TransmutationLunchBagItem.ItemContainer decode(RegistryFriendlyByteBuf buffer) {
                CompoundTag compoundTag = buffer.readNbt();
                ItemStackHandler handler = new ItemStackHandler(MAX_SIZE);
                if (compoundTag != null) {
                    handler.deserializeNBT(buffer.registryAccess(), compoundTag);
                }
                return new TransmutationLunchBagItem.ItemContainer(handler);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, TransmutationLunchBagItem.ItemContainer value) {
                CompoundTag compoundTag = value.items().serializeNBT(buffer.registryAccess());
                buffer.writeNbt(compoundTag);
            }
        };
    }
}
