package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.dispenser.OilPotDispenseBehavior;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight.FarmersDelightCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.harvest.HarvestCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable.GiftLootTables;
import com.github.ysbbbbbb.kaleidoscopecookery.event.*;
import com.github.ysbbbbbb.kaleidoscopecookery.event.effect.PreservationEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.event.effect.SatiatedShieldEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModVillager;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.level.block.DispenserBlock;

public class CommonRegistry {
    public static void init() {
        addComposter();
        registerFoodBiteBlocks();
        registerServerEvents();
//        event.enqueueWork(CommonRegistry::addVillagerGift);
//        event.enqueueWork(CommonRegistry::modCompat);
//        event.enqueueWork(CommonRegistry::addDispenserBehavior);
    }

    public static void registerServerEvents() {
//        SatiatedShieldEvent.register();
        PreservationEvent.register();
//        ArmorEffectHandler.register();
        EntityJoinWorldEvent.register();
        HoeUseEvent.register();
        RightClickEvent.register();
        ExtraLootTableDrop.register();
    }

    private static void registerFoodBiteBlocks() {
        FoodBiteRegistry.init();

        FoodBiteRegistry.FOOD_DATA_MAP.forEach((resourceLocation, data) -> {
            FoodBiteBlock block = new FoodBiteBlock(data.blockFood(), data.maxBites(), data.animateTick());
            Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);

            BowlFoodBlockItem item = new BowlFoodBlockItem(block, data.itemFood());
            Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
        });
    }

    private static void modCompat() {
        FarmersDelightCompat.init();
        HarvestCompat.init();
    }

    private static void addVillagerGift() {
//        GiveGiftToHero.GIFTS.put(ModVillager.CHEF.get(), GiftLootTables.CHEF_GIFT);
    }

    private static void addComposter() {
        CompostingChanceRegistry.INSTANCE.add(ModItems.TOMATO_SEED, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.CHILI_SEED, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.LETTUCE_SEED, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.WILD_RICE_SEED, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.RICE_SEED, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.TOMATO, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.RED_CHILI, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.GREEN_CHILI, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.LETTUCE, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.RICE_PANICLE, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.CATERPILLAR, 1.0F);
    }

    private static void addDispenserBehavior() {
        DispenserBlock.registerBehavior(ModItems.OIL_POT, new OilPotDispenseBehavior());
    }
}
