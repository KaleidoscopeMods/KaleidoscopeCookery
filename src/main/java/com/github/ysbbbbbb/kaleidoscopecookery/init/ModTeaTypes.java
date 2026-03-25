package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.TeaDrinkBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

public class ModTeaTypes {
    public static final ResourceLocation EMPTY = new ResourceLocation("minecraft", "air");
    public static final ResourceLocation WATER = new ResourceLocation("minecraft", "water");
    public static final ResourceLocation LAVA = new ResourceLocation("minecraft", "lava");
    public static final ResourceLocation TIEGUANYIN = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "tieguanyin");

    public static void registerAll() {
        TeaTypeManager.registerTeaType(new SimpleTeaType(EMPTY, 0, ItemStack.EMPTY, SimpleTeaType.simpleBlockFunc(0), SimpleTeaType.simpleEntityFunc(0)));
        TeaTypeManager.registerTeaType(new WaterTeaType(WATER, 0x9DF7FF));
        TeaTypeManager.registerTeaType(new LavaTeaType(LAVA, 0xE2610E));
        TeaTypeManager.registerTeaType(new DrinkTeaType(TIEGUANYIN, 0xDBFFB8, (TeaDrinkBlock) ModBlocks.TIEGUANYIN.get()));

        TeaTypeManager.bindFluid(WATER, Fluids.WATER.getFluidType());
        TeaTypeManager.bindFluid(LAVA, Fluids.LAVA.getFluidType());
    }
}
