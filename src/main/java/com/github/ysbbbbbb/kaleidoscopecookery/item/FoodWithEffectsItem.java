package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.config.ClientConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.CompatRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FoodWithEffectsItem extends Item {
    private final List<MobEffectInstance> effectInstances = Lists.newArrayList();
    private final Function<Quality, List<MobEffectInstance>> effectCache = Util.memoize(
            quality -> QualityUtils.modifyEffects(this.effectInstances, quality)
    );
    private final BiFunction<Quality, FoodProperties, FoodProperties> foodPropertiesCache = Util.memoize(
            (quality, raw) -> QualityUtils.modifyFoodProperties(raw, quality)
    );

    public FoodWithEffectsItem(FoodProperties properties) {
        super(new Item.Properties().food(properties));
        properties.getEffects().forEach(effect -> {
            if (effect.getSecond() >= 1F) {
                effectInstances.add(effect.getFirst());
            }
        });
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        FoodProperties raw = super.getFoodProperties(stack, entity);
        if (!QualityUtils.hasQuality(stack) || raw == null) {
            return raw;
        }
        // 如果有品质，那么依据品质
        Quality quality = QualityUtils.getQuality(stack);
        return this.foodPropertiesCache.apply(quality, raw);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id != null) {
            String key = "tooltip.%s.%s.maxim".formatted(id.getNamespace(), id.getPath());
            MutableComponent full = Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);
            // 先拿到纯文本，再按 \n 切
            String text = full.getString();
            for (String line : text.split("\n")) {
                if (!line.isEmpty()) {
                    tooltip.add(Component.literal(line).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                } else {
                    tooltip.add(CommonComponents.EMPTY);
                }
            }
        }

        boolean showEffect = !this.effectInstances.isEmpty()
                             && CompatRegistry.SHOW_POTION_EFFECT_TOOLTIPS
                             && ClientConfig.SHOW_FOOD_EFFECT_TOOLTIPS.get();

        // 品质
        if (QualityUtils.hasQuality(stack)) {
            Quality quality = QualityUtils.getQuality(stack);
            tooltip.add(quality.getTooltip());
            if (showEffect) {
                tooltip.add(CommonComponents.space());
                PotionUtils.addPotionTooltip(this.effectCache.apply(quality), tooltip, 1.0F);
            }
        } else {
            tooltip.add(CommonComponents.space());
            PotionUtils.addPotionTooltip(this.effectInstances, tooltip, 1.0F);
        }
    }
}
