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
            Registries.PROCESSOR_LIST, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "crop_replace"));

    private static final ResourceLocation PLAINS = ResourceLocation.parse("minecraft:village/plains/houses");
    private static final ResourceLocation SNOWY = ResourceLocation.parse("minecraft:village/snowy/houses");
    private static final ResourceLocation SAVANNA = ResourceLocation.parse("minecraft:village/savanna/houses");
    private static final ResourceLocation DESERT = ResourceLocation.parse("minecraft:village/desert/houses");
    private static final ResourceLocation TAIGA = ResourceLocation.parse("minecraft:village/taiga/houses");

    // 标记是否已成功初始化，避免重复尝试
    private static boolean initializationSuccessful = false;
    private static boolean initializationAttempted = false;

    public static void register() {
        ServerTickEvents.START_WORLD_TICK.register(minecraftServer -> {
            var registryAccess = minecraftServer.registryAccess();

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
        // 如果之前已经失败，直接返回
        if (initializationAttempted && !initializationSuccessful) {
            return;
        }

        try {
            var templatePools = registryAccess.registry(Registries.TEMPLATE_POOL);
            if (templatePools.isEmpty()) {
                KaleidoscopeCookery.LOGGER.warn("Template pools registry is empty for pool: {}", poolId);
                return;
            }
            var processorLists = registryAccess.registry(Registries.PROCESSOR_LIST);
            if (processorLists.isEmpty()) {
                KaleidoscopeCookery.LOGGER.warn("Processor lists registry is empty for pool: {}", poolId);
                return;
            }
            StructureTemplatePool pool = templatePools.get().get(poolId);
            if (pool == null) {
                KaleidoscopeCookery.LOGGER.warn("Structure pool not found: {}", poolId);
                return;
            }
            Holder<StructureProcessorList> holder = processorLists.get().getHolderOrThrow(CROP_REPLACE_PROCESSOR_LIST_KEY);
            ResourceLocation structLocation = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, structId);
            SinglePoolElement piece = SinglePoolElement.legacy(structLocation.toString(), holder).apply(StructureTemplatePool.Projection.RIGID);

            // 添加到 templates 列表
            for (int i = 0; i < weight; i++) {
                pool.templates.add(piece);
            }

            // 尝试使用反射更新 rawTemplates 字段，使用多个可能的字段名
            List<Pair<StructurePoolElement, Integer>> newRawTemplates = Lists.newArrayList(pool.rawTemplates);
            newRawTemplates.add(Pair.of(piece, weight));

            String[] possibleFieldNames = {"rawTemplates", "field_16864", "elementCounts"};
            boolean fieldFound = false;

            for (String fieldName : possibleFieldNames) {
                try {
                    Field rawTemplatesField = StructureTemplatePool.class.getDeclaredField(fieldName);
                    rawTemplatesField.setAccessible(true);
                    rawTemplatesField.set(pool, newRawTemplates);
                    fieldFound = true;
                    KaleidoscopeCookery.LOGGER.debug("Successfully updated field '{}' for pool: {}", fieldName, poolId);
                    break;
                } catch (NoSuchFieldException e) {
                    KaleidoscopeCookery.LOGGER.debug("Field '{}' not found, trying next possible field name", fieldName);
                } catch (Exception e) {
                    KaleidoscopeCookery.LOGGER.warn("Failed to set field '{}' for pool {}: {}", fieldName, poolId, e.getMessage());
                }
            }

            if (!fieldFound) {
                KaleidoscopeCookery.LOGGER.error("Failed to find any valid field for rawTemplates in StructureTemplatePool. Tried: {}", String.join(", ", possibleFieldNames));
                throw new NoSuchFieldException("Could not find rawTemplates field in StructureTemplatePool");
            }

        } catch (Exception e) {
            KaleidoscopeCookery.LOGGER.error("Failed to add village structure to pool {}: {}", poolId, e.getMessage());
            throw new RuntimeException("Failed to add village structure", e);
        }
    }
}
