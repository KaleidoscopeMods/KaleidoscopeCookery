package com.github.ysbbbbbb.kaleidoscopecookery.item.quality;

import net.minecraft.world.item.ItemStack;

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
}
