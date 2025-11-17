package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.OilPotHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.OilPotBlock.HAS_OIL;

public class OilPotBlockEntity extends BaseBlockEntity {
    public static final int MAX_OIL_COUNT = 256;
    private static final String OIL_COUNT = "OilCount";
    private LazyOptional<IItemHandler> invHandler;
    private int oilCount = 0;

    public OilPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.OIL_POT_BE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(OIL_COUNT, oilCount);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(OIL_COUNT)) {
            this.oilCount = tag.getInt(OIL_COUNT);
        }
    }

    public int getOilCount() {
        return oilCount;
    }

    public void setOilCount(int oilCount) {
        this.oilCount = oilCount;
        this.refresh();

        if (this.level == null) {
            return;
        }

        BlockState state = this.getBlockState();
        boolean hasOil = state.getValue(HAS_OIL);
        if (!hasOil && oilCount > 0) {
            level.setBlock(this.worldPosition, state.setValue(HAS_OIL, true), Block.UPDATE_ALL);
            return;
        }

        if (hasOil && oilCount <= 0) {
            level.setBlock(this.worldPosition, state.setValue(HAS_OIL, false), Block.UPDATE_ALL);
        }
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER && !this.remove) {
            if (this.invHandler == null) {
                this.invHandler = LazyOptional.of(() -> new OilPotHandler(this));
            }
            return this.invHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        super.setBlockState(blockState);
        if (this.invHandler != null) {
            LazyOptional<?> oldHandler = this.invHandler;
            this.invHandler = null;
            oldHandler.invalidate();
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (invHandler != null) {
            invHandler.invalidate();
            invHandler = null;
        }
    }
}
