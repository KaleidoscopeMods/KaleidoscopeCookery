package com.github.ysbbbbbb.kaleidoscopecookery.datamap.resources;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.datamap.TeaEffectData;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TeaEffectDataReloadListener extends SimpleJsonResourceReloadListener {
    public static final Map<Item, TeaEffectData> INSTANCE = Maps.newHashMap();
    private static final Gson GSON = new GsonBuilder().create();

    public TeaEffectDataReloadListener() {
        super(GSON, "datamap/tea_effect");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        INSTANCE.clear();
        for (var entry : resources.entrySet()) {
            var result = TeaEffectData.CODEC.parse(JsonOps.INSTANCE, entry.getValue());
            if (result.result().isPresent()) {
                TeaEffectData data = result.result().get();
                INSTANCE.put(data.item(), data);
            } else if (result.error().isPresent()) {
                KaleidoscopeCookery.LOGGER.error("Failed to parse tea effect data from '{}': {}", entry.getKey(), result.error().get().message());
            }
        }
        KaleidoscopeCookery.LOGGER.info("Successfully loaded tea effect data with {} entries", INSTANCE.size());
    }
}
