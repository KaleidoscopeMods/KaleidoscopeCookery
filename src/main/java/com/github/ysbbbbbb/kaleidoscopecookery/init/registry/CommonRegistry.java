package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoupBases;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.network.NetworkHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = KaleidoscopeCookery.MOD_ID)
public class CommonRegistry {
    @SubscribeEvent
    public static void onSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(CommonRegistry::addComposter);
        event.enqueueWork(NetworkHandler::init);
        event.enqueueWork(ModSoupBases::registerAll);
    }

    @SubscribeEvent
    public static void onBlockRegistryEvent(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
            FoodBiteRegistry.FOOD_DATA_MAP.forEach((resourceLocation, data) ->
                    event.register(ForgeRegistries.Keys.BLOCKS, resourceLocation,
                            () -> new FoodBiteBlock(data.blockFood(), data.maxBites(), data.animateTick())));
        }

        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
            FoodBiteRegistry.FOOD_DATA_MAP.forEach((resourceLocation, data) -> {
                Block block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
                if (block != null) {
                    event.register(ForgeRegistries.Keys.ITEMS, resourceLocation,
                            () -> new BowlFoodBlockItem(block, data.itemFood()));
                }
            });
        }
    }

    private static void addComposter() {
        ComposterBlock.COMPOSTABLES.put(ModItems.TOMATO_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.CHILI_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.LETTUCE_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.WILD_RICE_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.RICE_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.TOMATO.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.RED_CHILI.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.GREEN_CHILI.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.LETTUCE.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.RICE_PANICLE.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.CATERPILLAR.get(), 1.0F);
    }
}
