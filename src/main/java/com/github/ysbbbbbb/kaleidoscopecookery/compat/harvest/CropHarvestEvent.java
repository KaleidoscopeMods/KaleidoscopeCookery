package com.github.ysbbbbbb.kaleidoscopecookery.compat.harvest;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import it.crystalnest.harvest_with_ease.api.event.HarvestEvents;
import net.minecraft.world.level.block.state.BlockState;

public class CropHarvestEvent {
    static void onHarvest(HarvestEvents.HarvestCheckEvent event) {
        // 本模组的辣椒和番茄有自己的一套收获逻辑，需要阻止
        // 水稻的收获逻辑很奇怪，该模组无法兼容，也阻止
        BlockState crop = event.getCrop();
        if (crop.is(ModBlocks.TOMATO_CROP) || crop.is(ModBlocks.CHILI_CROP) || crop.is(ModBlocks.RICE_CROP)) {
            event.preventHarvest();
        }
    }

}
