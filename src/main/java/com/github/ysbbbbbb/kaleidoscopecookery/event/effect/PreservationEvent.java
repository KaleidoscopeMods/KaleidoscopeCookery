package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.util.TodoCheck;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.minecraft.world.effect.MobEffectCategory.HARMFUL;

public class PreservationEvent {
    public static void register() {
        UseItemCallback.EVENT.register(PreservationEvent::onUseItem);
    }

    @TodoCheck
    private static InteractionResultHolder<ItemStack> onUseItem(Player player, Level world, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEdible() && player.hasEffect(ModEffects.PRESERVATION)) {
//        if (stack.isEdible() && player.hasEffect(ModEffects.PRESERVATION) && stack.is(TagMod.PRESERVATION_FOOD)) {
            FoodProperties foodProperties = stack.getItem().getFoodProperties();
            if (foodProperties == null) {
                return InteractionResultHolder.pass(stack);
            }
            for (var effectPair : foodProperties.getEffects()) {
                MobEffect effect = effectPair.getFirst().getEffect();
                if (effect.getCategory() == HARMFUL) {
                    player.removeEffect(effect);
                }
            }
        }
        return InteractionResultHolder.pass(stack);
    }
}
