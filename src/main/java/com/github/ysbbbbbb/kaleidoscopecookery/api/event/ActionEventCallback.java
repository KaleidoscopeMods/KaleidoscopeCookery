package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

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
}
