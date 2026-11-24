package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.KitchenShelfBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.KitchenShelfItemHandler;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;
import java.util.Set;

import static com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.KitchenShelfItemHandler.CAPACITY;

public class KitchenShelfBlockEntity extends BaseBlockEntity {
    private static final int RESTOCK_INTERVAL = 83;
    private final KitchenShelfItemHandler inventory = new KitchenShelfItemHandler(this);
    private boolean wasPowered;

    public KitchenShelfBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.KITCHEN_SHELF_BE.get(), pos, state);
    }

    public ItemStack getStoredStack() {
        return inventory.getStackInSlot(0);
    }

    public boolean interact(LivingEntity living, boolean all) {
        ItemStack held = living.getMainHandItem();
        ItemStack stored = getStoredStack();

        // 空手取出时直接向厨架正面丢出
        if (held.isEmpty()) {
            if (stored.isEmpty()) {
                return false;
            }
            int amount = all ? stored.getCount() : 1;
            ItemStack taken = inventory.extractItem(0, amount, false);
            this.dropToFront(taken);
            return true;
        }

        // 存入
        if (!stored.isEmpty() && !ItemStack.isSameItemSameComponents(stored, held)) {
            // 如果是不可堆叠的，只能存入一个
            return false;
        }

        int amount = all ? Math.min(held.getCount(), CAPACITY - stored.getCount()) : 1;
        if (amount <= 0) {
            return false;
        }
        ItemStack inserted = held.copyWithCount(amount);
        int remainder = inventory.insertItem(0, inserted, false).getCount();
        if (!(living instanceof Player player) || !player.isCreative()) {
            held.shrink(amount - remainder);
        }
        return remainder < amount;
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, KitchenShelfBlockEntity shelf) {
        long offsetTime = level.getGameTime() + pos.hashCode();
        if (Math.floorMod(offsetTime, RESTOCK_INTERVAL) == 0) {
            shelf.restock(state);
        }
    }

    public boolean updatePower(boolean powered) {
        if (powered == this.wasPowered) {
            return false;
        }

        this.wasPowered = powered;
        this.setChanged();
        return powered;
    }

    private void restock(BlockState state) {
        if (level == null || getStoredStack().getCount() >= CAPACITY) {
            return;
        }
        Direction front = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        for (Direction direction : Direction.values()) {
            if (direction == front) {
                continue;
            }
            BlockEntity source = level.getBlockEntity(worldPosition.relative(direction));
            if (source == null || source instanceof KitchenShelfBlockEntity) {
                continue;
            }
            IItemHandler handler = level.getCapability(
                    Capabilities.ItemHandler.BLOCK, source.getBlockPos(), direction.getOpposite()
            );
            if (handler != null) {
                this.restockFromHandler(handler);
            }
            if (getStoredStack().getCount() >= CAPACITY) {
                break;
            }
        }
    }

    private void restockFromHandler(IItemHandler handler) {
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack candidate = handler.getStackInSlot(slot);
            ItemStack storedStack = getStoredStack();
            if (candidate.isEmpty() || (!storedStack.isEmpty() && !ItemStack.isSameItemSameComponents(storedStack, candidate))) {
                continue;
            }
            int needCount = Math.min(16, CAPACITY - storedStack.getCount());
            ItemStack extracted = handler.extractItem(slot, needCount, false);
            if (!extracted.isEmpty()) {
                inventory.insertItem(0, extracted, false);
            }
            if (storedStack.getCount() >= CAPACITY) {
                return;
            }
        }
    }

    public void dispense(BlockState state) {
        if (!(level instanceof ServerLevel) || this.getStoredStack().isEmpty()) {
            return;
        }

        ItemStack stack = inventory.extractItem(0, 1, false);
        this.dispenseStack(state, stack);
    }

    public boolean dispenseMatching(List<ItemStack> ingredients) {
        if (level == null) {
            return false;
        }

        // 先去除重复物品
        Set<Item> needed = Sets.newHashSet();
        for (ItemStack ingredient : ingredients) {
            if (!ingredient.isEmpty()) {
                needed.add(ingredient.getItem());
            }
        }

        // 按照需求，遍历链接的厨架，弹射出所需的物品
        boolean dispensed = false;
        for (Item item : needed) {
            for (BlockPos shelfPos : KitchenShelfBlock.getConnectedShelves(level, worldPosition)) {
                if (level.getBlockEntity(shelfPos) instanceof KitchenShelfBlockEntity shelf
                    && shelf.dispenseOneIfMatches(item)) {
                    dispensed = true;
                    break;
                }
            }
        }
        return dispensed;
    }

    private boolean dispenseOneIfMatches(Item item) {
        if (!this.getStoredStack().is(item)) {
            return false;
        }
        ItemStack stack = inventory.extractItem(0, 1, false);
        this.dispenseStack(this.getBlockState(), stack);
        return true;
    }

    /**
     * 从厨架前方弹射物品
     */
    private void dispenseStack(BlockState state, ItemStack stack) {
        if (!(level instanceof ServerLevel serverLevel) || stack.isEmpty()) {
            return;
        }

        Direction front = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        double x = worldPosition.getX() + 0.5 + front.getStepX() * 0.65;
        double y = worldPosition.getY() + 0.45;
        double z = worldPosition.getZ() + 0.5 + front.getStepZ() * 0.65;

        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.setDeltaMovement(front.getStepX() * 0.2, 0.15, front.getStepZ() * 0.2);
        level.addFreshEntity(entity);

        level.playSound(null, worldPosition, ModSounds.BLOCK_KITCHEN_SHELF.get(), SoundSource.BLOCKS, 1, 1);
        serverLevel.sendParticles(ModParticles.COOKING.get(),
                x, y, z, 6,
                0.12, 0.08, 0.12,
                0.01
        );
    }

    /**
     * 从厨架前方掉落物品
     */
    private void dropToFront(ItemStack stack) {
        if (level == null || stack.isEmpty()) {
            return;
        }

        Direction front = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        double x = worldPosition.getX() + 0.5 + front.getStepX() * 0.65;
        double y = worldPosition.getY() + 0.45;
        double z = worldPosition.getZ() + 0.5 + front.getStepZ() * 0.65;

        int remaining = stack.getCount();
        int maxStackSize = stack.getMaxStackSize();
        while (remaining > 0) {
            int count = Math.min(remaining, maxStackSize);
            ItemEntity entity = new ItemEntity(level, x, y, z, stack.copyWithCount(count));
            entity.setDeltaMovement(front.getStepX() * 0.2, 0.15, front.getStepZ() * 0.2);
            level.addFreshEntity(entity);
            remaining -= maxStackSize;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putBoolean("WasPowered", wasPowered);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        wasPowered = tag.getBoolean("WasPowered");
    }

    public IItemHandler getItemHandler() {
        return inventory;
    }
}
