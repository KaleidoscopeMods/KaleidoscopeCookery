package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.lang.reflect.Field;
import java.util.List;


public class AddVillageStructuresEvent {
    private static final ResourceKey<StructureProcessorList> CROP_REPLACE_PROCESSOR_LIST_KEY = ResourceKey.create(
            Registries.PROCESSOR_LIST, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "crop_replace"));

    private static final ResourceLocation PLAINS = new ResourceLocation("minecraft:village/plains/houses");
    private static final ResourceLocation SNOWY = new ResourceLocation("minecraft:village/snowy/houses");
    private static final ResourceLocation SAVANNA = new ResourceLocation("minecraft:village/savanna/houses");
    private static final ResourceLocation DESERT = new ResourceLocation("minecraft:village/desert/houses");
    private static final ResourceLocation TAIGA = new ResourceLocation("minecraft:village/taiga/houses");


    public static void addVillageStructures() {
        ServerTickEvents.START_WORLD_TICK.register(minecraftServer -> {
            var registryAccess = minecraftServer.getServer().registryAccess();

            addBuildingToPool(registryAccess, PLAINS, "village/houses/plains_kitchen", 4);
            addBuildingToPool(registryAccess, SNOWY, "village/houses/snowy_kitchen", 4);
            addBuildingToPool(registryAccess, SAVANNA, "village/houses/savanna_kitchen", 4);
            addBuildingToPool(registryAccess, DESERT, "village/houses/desert_kitchen", 4);
            addBuildingToPool(registryAccess, TAIGA, "village/houses/taiga_kitchen", 4);
        });
    }

    /**
     * 参考自：<a href="https://gist.github.com/TelepathicGrunt/4fdbc445ebcbcbeb43ac748f4b18f342">GitHub TelepathicGrunt</a>
     */
    public static void addBuildingToPool(RegistryAccess registryAccess, ResourceLocation poolId, String structId, int weight) {
        var templatePools = registryAccess.registry(Registries.TEMPLATE_POOL);
        if (templatePools.isEmpty()) {
            return;
        }
        var processorLists = registryAccess.registry(Registries.PROCESSOR_LIST);
        if (processorLists.isEmpty()) {
            return;
        }
        StructureTemplatePool pool = templatePools.get().get(poolId);
        if (pool == null) {
            return;
        }
        Holder<StructureProcessorList> holder = processorLists.get().getHolderOrThrow(CROP_REPLACE_PROCESSOR_LIST_KEY);
        ResourceLocation structLocation = new ResourceLocation(KaleidoscopeCookery.MOD_ID, structId);
        SinglePoolElement piece = SinglePoolElement.legacy(structLocation.toString(), holder).apply(StructureTemplatePool.Projection.RIGID);
        for (int i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }
        List<Pair<StructurePoolElement, Integer>> newRawTemplates = Lists.newArrayList(pool.rawTemplates);
        newRawTemplates.add(Pair.of(piece, weight));
        try {
            Field rawTemplatesField = StructureTemplatePool.class.getDeclaredField("rawTemplates"); // Obfuscated name may vary
            rawTemplatesField.setAccessible(true);
            rawTemplatesField.set(pool, newRawTemplates);
        } catch (Exception e) {
            KaleidoscopeCookery.LOGGER.error("Failed to add village structure: ", e);
        }
    }
}
