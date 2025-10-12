package com.github.ysbbbbbb.kaleidoscopecookery.compat.harvest;


import com.github.ysbbbbbb.kaleidoscopecookery.api.event.MillstoneFinishEvent;
import it.crystalnest.harvest_with_ease.api.event.HarvestEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.loader.api.FabricLoader;

public class HarvestCompat {
    public static final String ID = "harvest_with_ease";

    public static void init() {
//        FabricLoader.getInstance().getModContainer(ID).ifPresent(modContainer ->
//                EventFactory.createArrayBacked(HarvestEvents.HarvestCheckEvent.class, CropHarvestEvent::onHarvest)
////                MinecraftForge.EVENT_BUS.addListener(CropHarvestEvent::onHarvest)
//        );
    }

    public static final Event<MillstoneFinishEvent.Callback> CALLBACK = EventFactory.createArrayBacked(MillstoneFinishEvent.Callback.class, callbacks -> event -> {
        for (MillstoneFinishEvent.Callback callback : callbacks) {
            callback.post(event);
        }
    });
}
