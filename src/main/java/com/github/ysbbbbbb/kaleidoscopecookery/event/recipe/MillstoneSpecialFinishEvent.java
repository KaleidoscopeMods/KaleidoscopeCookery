package com.github.ysbbbbbb.kaleidoscopecookery.event.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.OilPotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEvents;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MillstoneSpecialFinishEvent {

    public static void onMillstoneTakeItem() {
        ModEvents.MILLSTONE_FINISH.register(event -> {
            MillstoneBlockEntity millstone = event.getMillstone();
            ItemStack output = millstone.getOutput();
            Level level = millstone.getLevel();
            if (level == null) {
                return;
            }
            // 如果是种子油且下方是油壶，那么自动化输出
            if (output.is(ModItems.OIL_POT)) {
                onGetOilPot(millstone, level, output);
                return;
            }
            // 如果是面粉且下方是含水炼药锅，那么直接在炼药锅底部掉落吸水的面团
            if (output.is(ModItems.FLOUR)) {
                onGetRawDough(millstone, level, output);
            }

            //最后考虑可能底下有漏斗，那么尝试把物品塞进漏斗
            handleWithHopper(millstone, level, output);
        });

    }

    private static void onGetRawDough(MillstoneBlockEntity millstone, Level level, ItemStack output) {
        BlockPos below = millstone.getBlockPos().below();
        BlockState blockState = level.getBlockState(below);
        if (!blockState.is(Blocks.WATER_CAULDRON)) {
            return;
        }
        ItemStack outputs = ModItems.RAW_DOUGH.getDefaultInstance();
        outputs.setCount(output.getCount());
        ItemEntity entity = new ItemEntity(level,
                below.getX() + 0.5f,
                below.getY() + 0.2f,
                below.getZ() + 0.5f,
                outputs);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
        millstone.resetWhenTakeout();
    }

    private static void onGetOilPot(MillstoneBlockEntity millstone, Level level, ItemStack output) {
        // 如果下方是油壶，那么自动化输出
        BlockPos below = millstone.getBlockPos().below();
        BlockState blockState = level.getBlockState(below);
        if (!blockState.is(ModBlocks.OIL_POT)) {
            return;
        }
        // 如果油壶满了，那么不输出
        int oilCount = blockState.getValue(OilPotBlock.OIL_COUNT);
        if (oilCount >= OilPotBlock.MAX_OIL_COUNT) {
            return;
        }
        // 不足 8 个时，概率产出
        RandomSource random = level.getRandom();
        if (random.nextInt(8) < output.getCount()) {
            level.setBlockAndUpdate(below, blockState.setValue(OilPotBlock.OIL_COUNT, oilCount + 1));
        }
        millstone.resetWhenTakeout();
    }

    private static void handleWithHopper(MillstoneBlockEntity millstone, Level level, ItemStack output) {
        BlockPos below = millstone.getBlockPos().below();
        BlockState blockState = level.getBlockState(below);
        if (!blockState.is(Blocks.HOPPER)) {
            return;
        }
        ItemEntity entity = new ItemEntity(level,
                below.getX() + 0.5f,
                below.getY() + 0.35f,
                below.getZ() + 0.5f,
                output);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
        millstone.resetWhenTakeout();
    }
}
