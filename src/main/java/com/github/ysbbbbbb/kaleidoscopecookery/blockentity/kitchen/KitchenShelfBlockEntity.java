package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.KitchenShelfItemHandler;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.KitchenShelfItemHandler.CAPACITY;

public class KitchenShelfBlockEntity extends BaseBlockEntity {
    private static final int RESTOCK_INTERVAL = 83;
    private final KitchenShelfItemHandler inventory = new KitchenShelfItemHandler(this);
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inventory);
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

        // 空手取出
        if (held.isEmpty()) {
            if (stored.isEmpty()) {
                return false;
            }
            int amount = all ? Math.min(stored.getCount(), stored.getMaxStackSize()) : 1;
            ItemStack taken = inventory.extractItem(0, amount, false);
            ItemUtils.getItemToLivingEntity(living, taken);
            return true;
        }

        // 存入
        if (!stored.isEmpty() && !ItemHandlerHelper.canItemStacksStack(stored, held)) {
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
            source.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite())
                    .ifPresent(this::restockFromHandler);
            if (getStoredStack().getCount() >= CAPACITY) {
                break;
            }
        }
    }

    private void restockFromHandler(IItemHandler handler) {
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack candidate = handler.getStackInSlot(slot);
            ItemStack storedStack = getStoredStack();
            if (candidate.isEmpty() || (!storedStack.isEmpty() && !ItemHandlerHelper.canItemStacksStack(storedStack, candidate))) {
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
        if (!(level instanceof ServerLevel serverLevel) || getStoredStack().isEmpty()) {
            return;
        }

        ItemStack stack = inventory.extractItem(0, 1, false);
        if (stack.isEmpty()) {
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

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putBoolean("WasPowered", wasPowered);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        wasPowered = tag.getBoolean("WasPowered");
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandler = LazyOptional.of(() -> inventory);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (!remove && capability == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }
        return super.getCapability(capability, side);
    }
}
