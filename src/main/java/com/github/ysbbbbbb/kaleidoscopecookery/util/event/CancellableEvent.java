package com.github.ysbbbbbb.kaleidoscopecookery.util.event;

public abstract class CancellableEvent implements IEvent {

    boolean isCanceled = false;

    public void setCanceled(boolean canceled) {
        this.isCanceled = canceled;
    }

    public boolean isCanceled() {
        return this.isCanceled;
    }

}
