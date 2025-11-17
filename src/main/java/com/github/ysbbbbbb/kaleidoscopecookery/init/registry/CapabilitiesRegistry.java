package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = KaleidoscopeCookery.MOD_ID)
public class CapabilitiesRegistry {
    @SubscribeEvent
    public static void registerGenericItemHandlers(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.OIL_POT_BE.get(), (b, v) -> b.createHandler());
    }
}
