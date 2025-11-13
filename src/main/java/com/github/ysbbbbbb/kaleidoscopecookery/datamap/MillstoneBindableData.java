package com.github.ysbbbbbb.kaleidoscopecookery.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public record MillstoneBindableData(int rotSpeedTick, float liftAngle, Vec3 offset) {
    private static final Codec<MillstoneBindableData> DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("rot_speed_tick").forGetter(MillstoneBindableData::rotSpeedTick),
            Codec.FLOAT.fieldOf("lift_angle").forGetter(MillstoneBindableData::liftAngle),
            Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(MillstoneBindableData::offset)
    ).apply(instance, MillstoneBindableData::new));

    private static final Codec<EntityType<?>> ENTITY_TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(id -> {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(id);
        return DataResult.success(type);
    }, BuiltInRegistries.ENTITY_TYPE::getKey);

    public static final Codec<Map<EntityType<?>, MillstoneBindableData>> CODEC = Codec.unboundedMap(ENTITY_TYPE_CODEC, MillstoneBindableData.DATA_CODEC);
    public static final MillstoneBindableData DEFAULT = new MillstoneBindableData(10 * 20, 5f, Vec3.ZERO);
}
