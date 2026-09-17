package com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.Map;

public class SoupBaseManager {
    private static final Map<ResourceLocation, ISoupBase> ALL_SOUP_BASES = Maps.newLinkedHashMap();

    public static void registerSoupBase(ISoupBase soupBase) {
        if (ALL_SOUP_BASES.containsKey(soupBase.getName())) {
            throw new IllegalArgumentException("Soup base with name " + soupBase.getName() + " already exists!");
        }
        ALL_SOUP_BASES.put(soupBase.getName(), soupBase);
    }

    public static void registerFluidSoupBase(ResourceLocation name, Item bucketItem, int bubbleColor) {
        registerSoupBase(new FluidSoupBase(name, bucketItem, bubbleColor));
    }

    public static void registerFluidSoupBase(ResourceLocation name, Item bucketItem, Fluid fluid, int bubbleColor) {
        registerSoupBase(new FluidSoupBase(name, bucketItem, fluid, bubbleColor));
    }

    public static void registerMobSoupBase(ResourceLocation name, Item bucketItem, int bubbleColor) {
        registerSoupBase(new MobSoupBase(name, bucketItem, bubbleColor));
    }

    public static void registerMobSoupBase(ResourceLocation name, Item mobBucketItem) {
        registerSoupBase(new MobSoupBase(name, mobBucketItem));
    }

    public static ISoupBase getSoupBase(ResourceLocation name) {
        return ALL_SOUP_BASES.get(name);
    }

    public static boolean containsSoupBase(ResourceLocation name) {
        return ALL_SOUP_BASES.containsKey(name);
    }

    public static Map<ResourceLocation, ISoupBase> getAllSoupBases() {
        return ALL_SOUP_BASES;
    }
}
