package com.github.ysbbbbbb.kaleidoscopecookery.crafting.teafluid;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teafluid.ITeaFluid;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.Map;

public class TeaFluidManager {
    private static final Map<ResourceLocation, ITeaFluid> ALL_TEA_FLUIDS = Maps.newLinkedHashMap();
    private static final Map<ResourceLocation, FluidType> BOUND_FLUID_TYPES = Maps.newLinkedHashMap();

    public static void registerTeaFluid(ITeaFluid teaFluid) {
        if (ALL_TEA_FLUIDS.containsKey(teaFluid.getName())) {
            throw new IllegalArgumentException("Tea fluid with name " + teaFluid.getName() + " already exists!");
        }
        ALL_TEA_FLUIDS.put(teaFluid.getName(), teaFluid);
    }

    public static void bindFluid(ResourceLocation name, FluidType fluidType) {
        if (BOUND_FLUID_TYPES.containsKey(name)) {
            throw new IllegalArgumentException("Tea fluid with name " + name + " is already bound to another fluid!");
        }
        BOUND_FLUID_TYPES.put(name, fluidType);
    }

    public static ITeaFluid getTeaFluid(ResourceLocation name) { return ALL_TEA_FLUIDS.get(name); }

    @Nullable
    public static FluidType getBoundFluid(ResourceLocation name) { return BOUND_FLUID_TYPES.getOrDefault(name, null); }

    public static boolean contains(ResourceLocation name) { return ALL_TEA_FLUIDS.containsKey(name); }

    public static Map<ResourceLocation, ITeaFluid> getAllTeaFluids() { return ALL_TEA_FLUIDS; }

    public static Map<ResourceLocation, FluidType> getBoundFluidTypes() { return BOUND_FLUID_TYPES; }
}
