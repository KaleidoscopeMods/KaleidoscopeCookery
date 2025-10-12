package com.github.ysbbbbbb.kaleidoscopecookery.util.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface IEvent {

    void post();

    Event<Callback<IEvent>> CALLBACK = EventFactory.createArrayBacked(Callback.class, callbacks -> event -> {
        for (Callback<IEvent> callback : callbacks) {
            callback.post(event);
        }
    });

    static <T extends IEvent> void post(T event) {
        CALLBACK.invoker().post(event);
    }

    interface Callback<T extends IEvent> {
        void post(T event);
    }
}
