package com.github.ysbbbbbb.kaleidoscopecookery.client.resources;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

public record ItemRenderReplacer(Map<ResourceLocation, ResourceLocation> pot,
                                 Map<ResourceLocation, ResourceLocation> stockpotCooking,
                                 Map<ResourceLocation, ResourceLocation> stockpotFinished,
                                 Map<ResourceLocation, ResourceLocation> millstone,
                                 Map<ResourceLocation, ResourceLocation> steamer) {
    public static final Codec<ResourceLocation> RL_CODEC = Codec.STRING.comapFlatMap(ItemRenderReplacer::parseModelLocation, ResourceLocation::toString).stable();
    public static final Codec<ItemRenderReplacer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(ResourceLocation.CODEC, RL_CODEC).fieldOf("pot").forGetter(ItemRenderReplacer::pot),
            Codec.unboundedMap(ResourceLocation.CODEC, RL_CODEC).fieldOf("stockpot_cooking").forGetter(ItemRenderReplacer::stockpotCooking),
            Codec.unboundedMap(ResourceLocation.CODEC, RL_CODEC).fieldOf("stockpot_finished").forGetter(ItemRenderReplacer::stockpotFinished),
            Codec.unboundedMap(ResourceLocation.CODEC, RL_CODEC).fieldOf("millstone").forGetter(ItemRenderReplacer::millstone),
            Codec.unboundedMap(ResourceLocation.CODEC, RL_CODEC).fieldOf("steamer").forGetter(ItemRenderReplacer::steamer)
    ).apply(instance, ItemRenderReplacer::new));

    private static Function<ResourceLocation, BakedModel> CACHE = createNewCache();

    public ItemRenderReplacer() {
        this(Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap());
    }

    private static Function<ResourceLocation, BakedModel> createNewCache() {
        return Util.memoize(id -> {
            ModelManager modelManager = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager();
            if (id instanceof ModelResourceLocation modelRl) {
                return modelManager.getModel(modelRl);
            }
            return modelManager.getModel(id);
        });
    }

    public static void resetCache() {
        CACHE = createNewCache();
    }

    public static BakedModel getModel(@Nullable Level level, ItemStack stack,
                                      Map<ResourceLocation, ResourceLocation> models) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        @Nullable ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (key == null) {
            return itemRenderer.getModel(stack, level, null, 0);
        }
        @Nullable ResourceLocation location = models.get(key);
        if (location == null) {
            return itemRenderer.getModel(stack, level, null, 0);
        }
        return CACHE.apply(location);
    }

    private static DataResult<ResourceLocation> parseModelLocation(String input) {
        String[] split = input.split("#");
        if (split.length > 1) {
            return DataResult.success(new ModelResourceLocation(new ResourceLocation(split[0]), split[1]));
        }
        return DataResult.success(new ResourceLocation(input));
    }

    public void addAll(ItemRenderReplacer other) {
        this.pot.putAll(other.pot);
        this.stockpotCooking.putAll(other.stockpotCooking);
        this.stockpotFinished.putAll(other.stockpotFinished);
        this.millstone.putAll(other.millstone);
        this.steamer.putAll(other.steamer);
    }
}
