package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.TeaBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teafluid.*;
import com.mojang.datafixers.util.Function4;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class ModTeaFluids {
    public static final ResourceLocation EMPTY = new ResourceLocation("minecraft", "air");
    public static final ResourceLocation WATER = new ResourceLocation("minecraft", "water");
    public static final ResourceLocation LAVA = new ResourceLocation("minecraft", "lava");
    public static final ResourceLocation TIEGUANYIN = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "tieguanyin");
    public static final ResourceLocation FLOWER_TEA = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "flower_tea");

    public static void registerAll() {
        TeaFluidManager.registerTeaFluid(new SimpleTeaFluid(EMPTY, 0, ItemStack.EMPTY, (s) -> false, false, simpleBlockFunc(0), simpleEntityFunc(0)));
        TeaFluidManager.registerTeaFluid(new WaterTeaFluid(WATER, 0x9DF7FF));
        TeaFluidManager.registerTeaFluid(new LavaTeaFluid(LAVA, 0xE2610E));
        TeaFluidManager.registerTeaFluid(new DrinkTeaFluid(TIEGUANYIN, 0xDBFFB8, (TeaBlock) ModBlocks.TIEGUANYIN.get()));
        TeaFluidManager.registerTeaFluid(new DrinkTeaFluid(FLOWER_TEA, 0xFFC969, (TeaBlock) ModBlocks.FLOWER_TEA.get()));

        TeaFluidManager.bindFluid(WATER, Fluids.WATER.getFluidType());
        TeaFluidManager.bindFluid(LAVA, Fluids.LAVA.getFluidType());
    }

    private static Function4<Level, BlockHitResult, LivingEntity, ItemStack, Integer> simpleBlockFunc(int consumed) {
        return (l, h, u, i) -> consumed;
    }

    private static Function4<Level, LivingEntity, LivingEntity, ItemStack, Integer> simpleEntityFunc(int consumed) {
        return (l, e, u, i) -> consumed;
    }
}
