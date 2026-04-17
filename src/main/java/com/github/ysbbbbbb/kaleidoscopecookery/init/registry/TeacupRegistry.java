package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TeacupRegistry {
    public static final Map<ResourceLocation, TeacupData> TEACUP_DATA_MAP = Maps.newLinkedHashMap();

    public static ResourceLocation BARLEY_TEA;
    public static ResourceLocation TIEGUANYIN;
    public static ResourceLocation BILUOCHUN;
    public static ResourceLocation OOLONG;
    public static ResourceLocation SAKURA_FUBUKI;
    public static ResourceLocation FLOWER_TEA;

    public static void init() {
        TeacupRegistry registry = new TeacupRegistry();

        BARLEY_TEA = registry.registerTeacupData("barley_tea", TeacupData.create(4).addEffect(() ->
                new MobEffectInstance(ModEffects.VITALITY, 8 * 60 * 20))
        );

        TIEGUANYIN = registry.registerTeacupData("tieguanyin", TeacupData.create(4).addEffect(() ->
                new MobEffectInstance(ModEffects.INSTANT_SMELTING, 2 * 60 * 20))
        );

        BILUOCHUN = registry.registerTeacupData("biluochun", TeacupData.create(4).addEffect(() ->
                new MobEffectInstance(ModEffects.PROJECTILE_DODGE, 2 * 60 * 20))
        );

        OOLONG = registry.registerTeacupData("oolong", TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 6 * 60 * 20))
                .addEffect(() -> new MobEffectInstance(MobEffects.JUMP, 6 * 60 * 20))
        );

        SAKURA_FUBUKI = registry.registerTeacupData("sakura_fubuki", TeacupData.create(4).addEffect(() ->
                new MobEffectInstance(ModEffects.HINDER, 6 * 60 * 20))
        );

        FLOWER_TEA = registry.registerTeacupData("flower_tea", TeacupData.create(4).addEffect(() ->
                new MobEffectInstance(MobEffects.REGENERATION, 20 * 20))
        );
    }

    public ResourceLocation registerTeacupData(ResourceLocation id, TeacupData data) {
        TEACUP_DATA_MAP.put(id, data);
        return id;
    }

    public ResourceLocation registerTeacupData(String name, TeacupData data) {
        return registerTeacupData(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, name), data);
    }

    public static Item getItem(ResourceLocation name) {
        return BuiltInRegistries.ITEM.get(name);
    }

    public static Block getBlock(ResourceLocation name) {
        return BuiltInRegistries.BLOCK.get(name);
    }

    public static final class TeacupData {
        private final int maxCount;
        private final List<Pair<Supplier<MobEffectInstance>, Float>> effects = Lists.newArrayList();
        private @Nullable VoxelShape aabb = null;

        private TeacupData(int maxCount) {
            this.maxCount = maxCount;
        }

        public static TeacupData create(int maxCount) {
            return new TeacupData(maxCount);
        }

        public TeacupData addEffect(Supplier<MobEffectInstance> effect, float probability) {
            this.effects.add(Pair.of(effect, probability));
            return this;
        }

        public TeacupData addEffect(Supplier<MobEffectInstance> effect) {
            return addEffect(effect, 1.0f);
        }

        public TeacupData setAABB(VoxelShape aabb) {
            this.aabb = aabb;
            return this;
        }

        public int getMaxCount() {
            return maxCount;
        }

        @Nullable
        public VoxelShape getAABB() {
            return aabb;
        }

        public List<Pair<Supplier<MobEffectInstance>, Float>> getEffects() {
            return effects;
        }
    }
}
