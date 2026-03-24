package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype.LavaTeaType;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype.SimpleTeaType;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype.TeaTypeManager;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype.WaterTeaType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

public class ModTeaTypes {
    public static final ResourceLocation EMPTY = new ResourceLocation("minecraft", "air");
    public static final ResourceLocation WATER = new ResourceLocation("minecraft", "water");
    public static final ResourceLocation LAVA = new ResourceLocation("minecraft", "lava");

    public static void registerAll() {
        TeaTypeManager.registerTeaType(new SimpleTeaType(EMPTY, 0, ItemStack.EMPTY, SimpleTeaType.simpleBlockFunc(0), SimpleTeaType.simpleEntityFunc(0)));
        TeaTypeManager.registerTeaType(new WaterTeaType(WATER, 0x9DF7FF));
        TeaTypeManager.registerTeaType(new LavaTeaType(LAVA, 0xE2610E));

        TeaTypeManager.bindFluid(WATER, Fluids.WATER.getFluidType());
        TeaTypeManager.bindFluid(LAVA, Fluids.LAVA.getFluidType());
    }
}
