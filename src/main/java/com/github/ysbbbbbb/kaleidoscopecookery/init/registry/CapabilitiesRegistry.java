package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = KaleidoscopeCookery.MOD_ID)
public class CapabilitiesRegistry {
    @SubscribeEvent
    public static void registerGenericItemHandlers(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.OIL_POT_BE.get(), (b, v) -> b.createHandler());

        registerHorseAutomation(event, EntityType.MULE);
        registerHorseAutomation(event, EntityType.DONKEY);
        registerHorseAutomation(event, EntityType.HORSE);
        registerHorseAutomation(event, EntityType.ZOMBIE_HORSE);
        registerHorseAutomation(event, EntityType.SKELETON_HORSE);
        registerHorseAutomation(event, EntityType.LLAMA);
        registerHorseAutomation(event, EntityType.TRADER_LLAMA);
    }

    private static <T extends AbstractHorse> void registerHorseAutomation(RegisterCapabilitiesEvent event, EntityType<T> entityType) {
        event.registerEntity(Capabilities.ItemHandler.ENTITY_AUTOMATION, entityType, (horse, side) -> {
            Container inventory = horse.getInventory();
            int firstInputSlot = AbstractHorse.INV_BASE_COUNT;
            if (inventory.getContainerSize() <= firstInputSlot) {
                return null;
            }
            return new RangedWrapper(new InvWrapper(inventory), firstInputSlot, inventory.getContainerSize());
        });
    }
}
