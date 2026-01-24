package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class KitchenBlockPonderTag {
    public static final ResourceLocation KITCHEN_BLOCKS = new ResourceLocation(KaleidoscopeCookery.MOD_ID,"kitchen_blocks");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper){
        helper.registerTag(KITCHEN_BLOCKS).addToIndex().item((ItemLike) ModItems.KITCHEN_SHOVEL.get(),true,false).title("").description("").register();
        helper.addToTag(KITCHEN_BLOCKS).add(ModItems.STOCKPOT.getId()).add(ModItems.POT.getId()).add(ModItems.RECIPE_ITEM.getId()).add(ModItems.STEAMER.getId()).add(ModItems.ENAMEL_BASIN.getId()).add(ModItems.MILLSTONE.getId()).add(ModItems.SHAWARMA_SPIT.getId());
    }
}
