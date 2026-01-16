package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.dispenser.OilPotDispenseBehavior;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight.FarmersDelightCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.harvest.HarvestCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable.GiftLootTables;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoupBases;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModVillager;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.network.NetworkHandler;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.shapes.VoxelShape;
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
        event.enqueueWork(CommonRegistry::addVillagerGift);
        event.enqueueWork(CommonRegistry::modCompat);
        event.enqueueWork(CommonRegistry::addDispenserBehavior);
    }

    @SubscribeEvent
    public static void onBlockRegistryEvent(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
            FoodBiteRegistry.FOOD_DATA_MAP.forEach((resourceLocation, data) ->
                    event.register(ForgeRegistries.Keys.BLOCKS, resourceLocation, () -> {
                        FoodBiteBlock biteBlock = new FoodBiteBlock(data.blockFood(), data.maxBites(), data.animateTick());
                        VoxelShape aabb = data.getAABB();
                        if (aabb != null) {
                            biteBlock.setAABB(aabb);
                        }
                        return biteBlock;
                    }));
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

    private static void modCompat() {
        FarmersDelightCompat.init();
        HarvestCompat.init();
    }

    private static void addVillagerGift() {
        GiveGiftToHero.GIFTS.put(ModVillager.CHEF.get(), GiftLootTables.CHEF_GIFT);
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

    private static void addDispenserBehavior() {
        DispenserBlock.registerBehavior(ModItems.OIL_POT.get(), new OilPotDispenseBehavior());
    }
}
