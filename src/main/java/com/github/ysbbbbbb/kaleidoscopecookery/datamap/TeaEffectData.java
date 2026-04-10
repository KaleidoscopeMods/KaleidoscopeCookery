package com.github.ysbbbbbb.kaleidoscopecookery.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public record TeaEffectData(Item item, List<Entry> effects) {
    private static final Codec<Item> ITEM_CODEC = ResourceLocation.CODEC.comapFlatMap(id -> {
        Item value = ForgeRegistries.ITEMS.getValue(id);
        return value != null ? DataResult.success(value) : DataResult.error(() -> "Unknown item: " + id);
    }, ForgeRegistries.ITEMS::getKey);

    public static final Codec<TeaEffectData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ITEM_CODEC.fieldOf("item").forGetter(TeaEffectData::item),
            Codec.list(Entry.ENTRY_CODEC).fieldOf("effects").forGetter(TeaEffectData::effects)
    ).apply(instance, TeaEffectData::new));

    /**
     * 具体每个效果条目
     *
     * @param effect      药水效果
     * @param duration    持续时间，单位是秒
     * @param amplifier   效果等级，0 表示一级，1 表示二级，以此类推
     * @param probability 效果发生的概率，范围是 0.0 到 1.0，1.0 表示 100% 发生，0.5 表示 50% 发生，以此类推
     */
    public record Entry(MobEffect effect, int duration, int amplifier, float probability) {
        private static final Codec<MobEffect> MOB_EFFECT_CODEC = ResourceLocation.CODEC.comapFlatMap(id -> {
            MobEffect value = ForgeRegistries.MOB_EFFECTS.getValue(id);
            return value != null ? DataResult.success(value) : DataResult.error(() -> "Unknown mob effect: " + id);
        }, ForgeRegistries.MOB_EFFECTS::getKey);

        private static final Codec<Entry> ENTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                MOB_EFFECT_CODEC.fieldOf("effect").forGetter(Entry::effect),
                Codec.INT.fieldOf("duration").forGetter(Entry::duration),
                Codec.INT.fieldOf("amplifier").forGetter(Entry::amplifier),
                Codec.FLOAT.fieldOf("probability").forGetter(Entry::probability)
        ).apply(instance, Entry::new));
    }
}