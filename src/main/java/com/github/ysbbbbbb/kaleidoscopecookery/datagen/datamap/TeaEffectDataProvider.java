package com.github.ysbbbbbb.kaleidoscopecookery.datagen.datamap;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.datamap.TeaEffectData;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.ToIntFunction;

public class TeaEffectDataProvider implements DataProvider {
    private static final ToIntFunction<String> ORDER_FIELDS = Util.make(new Object2IntOpenHashMap<>(), map -> {
        map.put("item", 0);
        map.defaultReturnValue(1);
    });

    private final Map<String, TeaEffectData> data = Maps.newLinkedHashMap();
    private final PackOutput output;

    public TeaEffectDataProvider(PackOutput output) {
        this.output = output;
    }

    private void addEntry() {
        add(ModItems.TIEGUANYIN, List.of(
                new TeaEffectData.Entry(ModEffects.INSTANT_SMELTING.get(), 120, 0, 1)
        ));

        add(ModItems.FLOWER_TEA, List.of(
                new TeaEffectData.Entry(MobEffects.REGENERATION, 20, 0, 1)
        ));
    }

    /**
     * 使用物品注册名的 path 部分作为文件名
     */
    public final void add(RegistryObject<Item> key, List<TeaEffectData.Entry> entries) {
        var itemKey = ForgeRegistries.ITEMS.getKey(key.get());
        if (itemKey == null) {
            throw new IllegalArgumentException("Item not registered: " + key.getId());
        }
        this.add(itemKey.getPath(), key, entries);
    }

    /**
     * 使用自定义文件名
     */
    public final void add(String fileName, RegistryObject<Item> key, List<TeaEffectData.Entry> entries) {
        this.add(fileName, new TeaEffectData(key.get(), entries));
    }

    public void add(String fileName, TeaEffectData value) {
        this.data.put(fileName, value);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addEntry();

        List<CompletableFuture<?>> futures = Lists.newArrayList();
        var pathProvider = this.output.createPathProvider(PackOutput.Target.DATA_PACK, "datamap/tea_effect");

        for (var entry : data.entrySet()) {
            TeaEffectData.CODEC
                    .encodeStart(JsonOps.INSTANCE, entry.getValue())
                    .resultOrPartial(KaleidoscopeCookery.LOGGER::error)
                    .ifPresent(json -> {
                        var filePath = pathProvider.json(new ResourceLocation(KaleidoscopeCookery.MOD_ID, entry.getKey()));
                        var future = this.saveStable(cache, json, filePath);
                        futures.add(future);
                    });
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @SuppressWarnings("all")
    private CompletableFuture<?> saveStable(CachedOutput output, JsonElement json, Path path) {
        return CompletableFuture.runAsync(() -> {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                HashingOutputStream hashing = new HashingOutputStream(Hashing.sha1(), stream);

                try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(hashing, StandardCharsets.UTF_8))) {
                    writer.setSerializeNulls(false);
                    writer.setIndent("  ");
                    GsonHelper.writeValue(writer, json, Comparator.comparingInt(ORDER_FIELDS));
                }

                output.writeIfNeeded(path, stream.toByteArray(), hashing.hash());
            } catch (IOException ioexception) {
                LOGGER.error("Failed to save file to {}", path, ioexception);
            }
        }, Util.backgroundExecutor());
    }

    @Override
    public String getName() {
        return "Tea Effect Data";
    }
}
