package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.util.neo.ItemStackHandler;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class TransmutationLunchBagItem extends Item {
    public static final ResourceLocation HAS_ITEMS_PROPERTY = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "has_items");
    public static final int NO_ITEMS = 0;
    public static final int HAS_ITEMS = 1;

    private static final int MAX_SIZE = 16;
    private static final String TAG_ITEMS = "Items";

    public TransmutationLunchBagItem() {
        super((new Properties()).stacksTo(1));
    }

    @Environment(EnvType.CLIENT)
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
    public @NotNull InteractionResult useOn(UseOnContext context) {
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
        ItemStackHandler fruitBasketItems = new ItemStackHandler(fruitBasket.getItems());

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
                    ItemStack remaining = ItemUtils.insertItemStacked(fruitBasketItems, stack, false);
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
                ItemStack remaining = ItemUtils.insertItemStacked(bagItems, stack, false);
                fruitBasketItems.extractItem(i, stack.getCount() - remaining.getCount(), false);
            }
        }
        TransmutationLunchBagItem.setItems(bag, bagItems);
        fruitBasket.refresh();
        playDropContentsSound(player);
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        // 潜行右键使用，取出物品
        if (player.isSecondaryUseActive() && dropContents(itemInHand, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemInHand, level.isClientSide());
        }

        // 非潜行状态，并且里面有物品
        if (!player.isSecondaryUseActive() && hasItems(itemInHand)) {
            boolean hasFood = false;
            ItemStackHandler items = getItems(itemInHand);
            for (int i = 0; i < items.getSlots(); i++) {
                ItemStack stackInSlot = items.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    hasFood = true;
                    break;
                }
            }
            if (hasFood) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(itemInHand);
            }
        }

        return InteractionResultHolder.fail(itemInHand);
    }

    @Override
    @SuppressWarnings("all")
    public ItemStack finishUsingItem(ItemStack bag, Level level, LivingEntity entity) {
        if (!hasItems(bag)) {
            return bag;
        }
        ItemStack food = ItemStack.EMPTY;
        List<FoodProperties.PossibleEffect> effects = Lists.newArrayList();

        ItemStackHandler items = getItems(bag);
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stackInSlot = items.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                continue;
            }

            // 先检查是不是食物
            FoodProperties properties = stackInSlot.get(DataComponents.FOOD);
            if (properties != null) {
                // 第一个食物的效果不加入其中，避免重复
                if (!food.isEmpty()) {
                    effects.addAll(properties.effects());
                } else {
                    food = items.extractItem(i, 1, false);
                }
                continue;
            }

            // 其次检查是不是药水
            PotionContents potionContents = stackInSlot.get(DataComponents.POTION_CONTENTS);
            if (potionContents != null) {
                // 第一个药水的效果不加入其中，避免重复
                if (!food.isEmpty()) {
                    potionContents.customEffects().stream()
                            .forEach(e -> effects.add(new FoodProperties.PossibleEffect(e, 1F)));
                } else {
                    food = items.extractItem(i, 1, false);
                }
            }
        }

        if (!food.isEmpty()) {
            // 消耗物品
            ItemStack returnStack = food.finishUsingItem(level, entity);
            Item containerItem = ItemUtils.getContainerItem(food);

            // 返还容器
            if (!returnStack.isEmpty()) {
                // 排除创造模式玩家
                if (!(entity instanceof Player player) || !player.getAbilities().instabuild) {
                    ItemUtils.getItemToLivingEntity(entity, returnStack);
                }
            } else if (containerItem != Items.AIR) {
                ItemUtils.getItemToLivingEntity(entity, containerItem.getDefaultInstance());
            }

            // 处理效果
            for (var effect : effects) {
                if (level.isClientSide || effect.probability() <= 0.0F || level.random.nextFloat() >= effect.probability()) {
                    continue;
                }
                entity.addEffect(new MobEffectInstance(effect.effect()));
            }

            // 给予成就
            if (entity instanceof ServerPlayer player) {
                ModTrigger.EVENT.trigger(player, ModEventTriggerType.USE_TRANSMUTATION_LUNCH_BAG);
            }

            // 更新饭袋数据
            setItems(bag, items);
            return bag;
        }

        return bag;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return hasItems(stack) ? UseAnim.EAT : UseAnim.NONE;
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
            int addCount = add(bag, clickItem);
            if (addCount > 0) {
                slot.safeTake(clickItem.getCount(), addCount, player);
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
        if (food.isEmpty() || !food.getItem().canFitInsideContainerItems() || !canAdd(food)) {
            return 0;
        }
        int totalCount = food.getCount();

        ItemStackHandler items = getItems(bag);
        ItemStack remaining = ItemUtils.insertItemStacked(items, food, false);

        int addCount = totalCount - (remaining.isEmpty() ? 0 : remaining.getCount());
        if (addCount > 0) {
            setItems(bag, items);
            return addCount;
        }
        return 0;
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
            ItemUtils.giveItemToPlayer(player, stack);
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
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (!hasItems(stack)) {
            return Optional.empty();
        }
        ItemStackHandler items = getItems(stack);
        return Optional.of(new ItemContainerTooltip(items.getStacks()));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.transmutation_lunch_bag").withStyle(ChatFormatting.GRAY));
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
                    return new ItemContainer(handler);
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

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemContainer> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public @NotNull ItemContainer decode(RegistryFriendlyByteBuf buffer) {
                CompoundTag compoundTag = buffer.readNbt();
                ItemStackHandler handler = new ItemStackHandler(MAX_SIZE);
                if (compoundTag != null) {
                    handler.deserializeNBT(buffer.registryAccess(), compoundTag);
                }
                return new ItemContainer(handler);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, ItemContainer value) {
                CompoundTag compoundTag = value.items().serializeNBT(buffer.registryAccess());
                buffer.writeNbt(compoundTag);
            }
        };
    }
}
