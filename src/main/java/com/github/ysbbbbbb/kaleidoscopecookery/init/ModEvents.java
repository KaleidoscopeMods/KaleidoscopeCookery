package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.api.event.ActionEventCallback;
import com.github.ysbbbbbb.kaleidoscopecookery.event.SpecialRecipeItemEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.event.recipe.MillstoneSpecialFinishEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.event.recipe.MillstoneSpecialRecipeEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ModEvents {

    public static final Event<ActionEventCallback.MillstoneFinish> MILLSTONE_FINISH =
            EventFactory.createArrayBacked(ActionEventCallback.MillstoneFinish.class, call -> action -> {
                for (ActionEventCallback.MillstoneFinish listener : call) {
                    listener.onMillstoneFinish(action);
                }
            });

    public static final Event<ActionEventCallback.MillstoneTakeItem> MILLSTONE_TAKE_ITEM =
            EventFactory.createArrayBacked(ActionEventCallback.MillstoneTakeItem.class, call -> action -> {
                for (ActionEventCallback.MillstoneTakeItem listener : call) {
                    listener.onMillstoneTakeItem(action);
                }
            });

    public static final Event<ActionEventCallback.CheckSpecialItem> CHECK_SPECIAL_ITEM =
            EventFactory.createArrayBacked(ActionEventCallback.CheckSpecialItem.class, call -> action -> {
                for (ActionEventCallback.CheckSpecialItem listener : call) {
                    listener.onCheckItemEvent(action);
                }
            });

    public static final Event<ActionEventCallback.DeductSpecialItem> DEDUCT_SPECIAL_ITEM =
            EventFactory.createArrayBacked(ActionEventCallback.DeductSpecialItem.class, call -> action -> {
                for (ActionEventCallback.DeductSpecialItem listener : call) {
                    listener.onDeductItemEvent(action);
                }
            });

    public static void init() {
        MillstoneSpecialRecipeEvent.onMillstoneTakeItem();
        MillstoneSpecialFinishEvent.onMillstoneTakeItem();
        SpecialRecipeItemEvent.onCheckItemEvent();
        SpecialRecipeItemEvent.onDeductItemEvent();
    }
}
