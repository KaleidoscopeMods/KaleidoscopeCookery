package com.github.ysbbbbbb.kaleidoscopecookery.item.quality;

import com.google.common.collect.Lists;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class QualityUtils {
    public static final String CUISINE_QUALITY = "kaleidoscope_cookery:quality";

    public static void setQuality(ItemStack food, Quality quality) {
        food.getOrCreateTag().putInt(CUISINE_QUALITY, quality.getId());
    }

    public static Quality getQuality(ItemStack food) {
        if (food.hasTag() && food.getOrCreateTag().contains(CUISINE_QUALITY)) {
            int id = food.getOrCreateTag().getInt(CUISINE_QUALITY);
            return Quality.BY_ID.apply(id);
        }
        return Quality.STANDARD;
    }

    public static boolean hasQuality(ItemStack food) {
        return food.hasTag() && food.getOrCreateTag().contains(CUISINE_QUALITY);
    }

    public static List<MobEffectInstance> modifyEffects(List<MobEffectInstance> effectInstances, Quality quality) {
        List<MobEffectInstance> list = Lists.newArrayList();
        for (MobEffectInstance instance : effectInstances) {
            int duration = (int) Math.round(quality.getRatio() * instance.getDuration());
            if (duration > 0) {
                list.add(new MobEffectInstance(instance.getEffect(), duration, instance.getAmplifier()));
            }
        }
        return list;
    }

    public static FoodProperties modifyFoodProperties(FoodProperties raw, Quality quality) {
        double ratio = quality.getRatio();

        int nutrition = (int) Math.round(ratio * raw.getNutrition());
        float saturationMod = (float) ratio * raw.getSaturationModifier();

        FoodProperties.Builder builder = new FoodProperties.Builder();
        builder.nutrition(nutrition).saturationMod(saturationMod);

        raw.getEffects().forEach(effect -> {
            if (effect.getSecond() >= 1F) {
                MobEffectInstance instance = effect.getFirst();
                int duration = (int) Math.round(ratio * instance.getDuration());
                int amplifier = instance.getAmplifier();
                if (duration > 0) {
                    MobEffectInstance newEffect = new MobEffectInstance(instance.getEffect(), duration, amplifier);
                    builder.effect(() -> newEffect, effect.getSecond());
                }
            }
        });

        if (raw.isMeat()) {
            builder.meat();
        }
        if (raw.isFastFood()) {
            builder.fast();
        }
        if (raw.canAlwaysEat()) {
            builder.alwaysEat();
        }

        return builder.build();
    }
}
