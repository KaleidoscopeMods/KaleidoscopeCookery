package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.BambooTrayBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = KaleidoscopeCookery.MOD_ID)
public class CapabilitiesRegistry {
    @SubscribeEvent
    public static void registerGenericItemHandlers(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.OIL_POT_BE.get(), (b, v) -> b.createHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.TEAPOT_BE.get(), (b, side) -> b.getInputHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.MILLSTONE_BE.get(),
                (b, side) -> side == Direction.UP ? b.getInputHandler() : null);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.BAMBOO_TRAY_BE.get(),
                BambooTrayBlockEntity::getItemHandler);
    }
}
