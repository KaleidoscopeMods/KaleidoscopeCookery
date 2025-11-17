package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

/**
 * Interface for events that can be canceled.
 * Listeners registered to the event bus will not receive {@link #isCanceled() canceled} events,
 * unless they were registered with {@code receiveCanceled = true}.
 */
public interface IActionCancelable {

    @MustBeInvokedByOverriders
    default void setCanceled(boolean canceled) {
        ((ActionEvent) this).isCanceled = canceled;
    }

    /**
     * {@return the canceled state of this event}
     */
    @ApiStatus.NonExtendable
    default boolean isCanceled() {
        return ((ActionEvent) this).isCanceled;
    }
}
