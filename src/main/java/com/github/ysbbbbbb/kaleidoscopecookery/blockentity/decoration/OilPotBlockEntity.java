package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.OilPotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.OilPotHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

import static com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.OilPotBlock.HAS_OIL;

public class OilPotBlockEntity extends BaseBlockEntity {
    public static final int MAX_OIL_COUNT = 256;
    private static final String OIL_COUNT = "OilCount";
    private final OilPotHandler invHandler = new OilPotHandler(this);
    private int oilCount = 0;

    public OilPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.OIL_POT_BE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(OIL_COUNT, oilCount);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(OIL_COUNT)) {
            this.oilCount = tag.getInt(OIL_COUNT);
            this.updateCap();
        }
    }

    public int getOilCount() {
        return oilCount;
    }

    public void updateCap() {
        this.invHandler.setOilCount(this.oilCount);
    }

    /**
     * 仅给 OilPotHandler 使用，避免循环调用
     */
    public void setOilCountWithoutCapUpdate(int oilCount) {
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

    /**
     * 普通的设置油量方法，还会顺带更新 cap
     */
    public void setOilCount(int oilCount) {
        this.setOilCountWithoutCapUpdate(oilCount);
        this.updateCap();
    }

    @Nullable
    public IItemHandler createHandler() {
        BlockState state = this.getBlockState();
        if (state.getBlock() instanceof OilPotBlock) {
            return this.invHandler;
        }
        return null;
    }
}
