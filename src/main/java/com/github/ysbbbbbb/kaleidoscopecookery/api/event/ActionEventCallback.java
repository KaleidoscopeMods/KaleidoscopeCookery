package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface ActionEventCallback {

    @FunctionalInterface
    interface MillstoneFinish {
        void onMillstoneFinish(MillstoneFinishEvent event);
    }

    @FunctionalInterface
    interface MillstoneTakeItem {
        void onMillstoneTakeItem(MillstoneTakeItemEvent event);
    }

    @FunctionalInterface
    interface CheckSpecialItem{
        void onCheckItemEvent(RecipeItemEvent.CheckItem event);
    }

    @FunctionalInterface
    interface DeductSpecialItem{
        void onDeductItemEvent(RecipeItemEvent.DeductItem event);
    }

    @FunctionalInterface
    interface LivingEntityHurt {
        void onLivingEntityHurt(LivingDamageEvent event);
    }

    @FunctionalInterface
    interface PlayerLeftClick {
        void onPlayerLeftClick(@Nullable Player player, InteractionHand hand);
    }
}
