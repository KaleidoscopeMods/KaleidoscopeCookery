package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;


public class MillstoneFinishEvent {
    private final MillstoneBlockEntity millstone;
    private final @Nullable Mob bindEntity;

    public MillstoneFinishEvent(MillstoneBlockEntity millstone, @Nullable Mob bindEntity) {
        this.millstone = millstone;
        this.bindEntity = bindEntity;
    }

    public MillstoneBlockEntity getMillstone() {
        return millstone;
    }

    public @Nullable Mob getBindEntity() {
        return bindEntity;
    }

    public void post() {
        CALLBACK.invoker().post(this);
    }

    public static final Event<Callback> CALLBACK = EventFactory.createArrayBacked(Callback.class, callbacks -> event -> {
        for (Callback callback : callbacks) {
            callback.post(event);
        }
    });

    public static void post(MillstoneFinishEvent event) {
        CALLBACK.invoker().post(event);
    }

    public interface Callback {
        void post(MillstoneFinishEvent event);
    }
}
