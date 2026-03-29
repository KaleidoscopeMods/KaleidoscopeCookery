package com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.Map;

public class TeaTypeManager {
    private static final Map<ResourceLocation, ITeaType> ALL_TEA_TYPES = Maps.newLinkedHashMap();
    private static final Map<ResourceLocation, FluidType> BOUND_FLUID_TYPES = Maps.newLinkedHashMap();
    private static final Map<FluidType, ResourceLocation> FLUID_TYPE_TO_NAME = Maps.newLinkedHashMap();

    public static void registerTeaType(ITeaType teaType) {
        if (ALL_TEA_TYPES.containsKey(teaType.getName())) {
            throw new IllegalArgumentException("Tea type with name " + teaType.getName() + " already exists!");
        }
        ALL_TEA_TYPES.put(teaType.getName(), teaType);
    }

    public static void bindFluid(ResourceLocation name, FluidType fluidType) {
        if (BOUND_FLUID_TYPES.containsKey(name)) {
            throw new IllegalArgumentException("Tea type with name " + name + " is already bound to another fluid!");
        }
        BOUND_FLUID_TYPES.put(name, fluidType);
        if (FLUID_TYPE_TO_NAME.containsKey(fluidType)) {
            throw new IllegalArgumentException("fluid type with name " + fluidType.getDescriptionId() + " is already bound to another tea type!");
        }
        FLUID_TYPE_TO_NAME.put(fluidType, name);
    }

    public static ITeaType getTeaType(ResourceLocation name) { return ALL_TEA_TYPES.get(name); }

    @Nullable
    public static FluidType getBoundFluid(ResourceLocation name) { return BOUND_FLUID_TYPES.getOrDefault(name, null); }

    @Nullable
    public static ResourceLocation getBoundTeaTypeId(FluidType fluidType) { return FLUID_TYPE_TO_NAME.getOrDefault(fluidType, null); }

    public static boolean containsTeaType(ResourceLocation name) { return ALL_TEA_TYPES.containsKey(name); }

    public static Map<ResourceLocation, ITeaType> getAllTeaTypes() { return ALL_TEA_TYPES; }

    public static Map<ResourceLocation, FluidType> getBoundFluidTypes() { return BOUND_FLUID_TYPES; }
}
