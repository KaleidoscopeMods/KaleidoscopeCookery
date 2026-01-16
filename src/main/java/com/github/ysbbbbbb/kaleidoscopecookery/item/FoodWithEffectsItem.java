package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.CompatRegistry;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FoodWithEffectsItem extends Item {
    private final List<MobEffectInstance> effectInstances = Lists.newArrayList();

    public FoodWithEffectsItem(FoodProperties properties) {
        super(new Item.Properties().food(properties));
        properties.getEffects().forEach(effect -> {
            if (effect.getSecond() >= 1F) {
                effectInstances.add(effect.getFirst());
            }
        });
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
        if (!this.effectInstances.isEmpty() && CompatRegistry.SHOW_POTION_EFFECT_TOOLTIPS) {
            tooltip.add(CommonComponents.space());
            PotionUtils.addPotionTooltip(this.effectInstances, tooltip, 1.0F);
        }
    }
}
