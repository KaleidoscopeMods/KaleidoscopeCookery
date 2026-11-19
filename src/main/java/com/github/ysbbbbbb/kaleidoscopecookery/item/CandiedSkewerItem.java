package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CandiedSkewerItem extends Item {
    private static final UUID KNOCKBACK_MODIFIER_UUID = UUID.fromString("d0190f53-13a1-4c8c-baa2-d3d0059911d4");

    private static final Multimap<Attribute, AttributeModifier> ATTRIBUTE_MODIFIERS = ImmutableMultimap.of(
            Attributes.ATTACK_KNOCKBACK,
            new AttributeModifier(
                    KNOCKBACK_MODIFIER_UUID,
                    "Candied skewer knockback",
                    3,
                    AttributeModifier.Operation.ADDITION
            )
    );

    private final @Nullable MobEffect attackEffect;
    private final int attackEffectDuration;

    public CandiedSkewerItem(FoodProperties foodProperties) {
        this(foodProperties, null, 0);
    }

    public CandiedSkewerItem(FoodProperties foodProperties, @Nullable MobEffect attackEffect, int attackEffectDuration) {
        super(new Properties().food(foodProperties));
        this.attackEffect = attackEffect;
        this.attackEffectDuration = attackEffectDuration;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(this);
        if (key == null) {
            return;
        }
        tooltip.add(Component.translatable(key.toLanguageKey("tooltip"))
                .withStyle(ChatFormatting.DARK_GRAY)
                .withStyle(ChatFormatting.ITALIC));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? ATTRIBUTE_MODIFIERS : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.attackEffect != null && !target.level().isClientSide) {
            target.addEffect(new MobEffectInstance(this.attackEffect, this.attackEffectDuration), attacker);
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
