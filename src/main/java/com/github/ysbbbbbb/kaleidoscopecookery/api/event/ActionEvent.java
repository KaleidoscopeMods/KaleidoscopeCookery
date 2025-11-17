package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

/**
 * Base Event class that all other events are derived from.
 * <br>
 * <strong>Note on abstract events</strong>
 * <br>
 * Listeners cannot be registered to an abstract event class.
 * <p>
 * This is useful for classes that extend {@link ActionEvent} with more data and methods,
 * but should never be listened to directly.
 * <p>
 * For example, an event with {@code Pre} and {@code Post} subclasses might want to
 * be declared as {@code abstract} to prevent user accidentally listening to both.
 * <p>
 * All the parents of abstract event classes until {@link ActionEvent} must also be abstract.
 */
public abstract class ActionEvent {

    boolean isCanceled = false;

    protected ActionEvent() {}
}
