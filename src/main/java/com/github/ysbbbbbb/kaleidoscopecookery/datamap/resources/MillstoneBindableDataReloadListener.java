package com.github.ysbbbbbb.kaleidoscopecookery.datamap.resources;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.datamap.MillstoneBindableData;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.EntityType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MillstoneBindableDataReloadListener implements ResourceManagerReloadListener {
    public static final Map<EntityType<?>, MillstoneBindableData> INSTANCE = Maps.newHashMap();
    private static final ResourceLocation FILE_PATH = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "datamap/millstone_bindable_data.json");

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        resourceManager.listPacks().forEach((packResources) -> {
            IoSupplier<InputStream> resource = packResources.getResource(PackType.SERVER_DATA, FILE_PATH);
            if (resource == null) {
                return;
            }
            try (InputStream inputStream = resource.get(); InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                var result = MillstoneBindableData.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                if (result.result().isPresent()) {
                    INSTANCE.putAll(result.result().get());
                    KaleidoscopeCookery.LOGGER.info("Successfully loaded millstone bindable data");
                } else if (result.error().isPresent()) {
                    KaleidoscopeCookery.LOGGER.error("Failed to parse millstone bindable data: {}", result.error().get().message());
                }
            } catch (Exception e) {
                KaleidoscopeCookery.LOGGER.error("Failed to load millstone bindable data", e);
            }
        });
    }
}
