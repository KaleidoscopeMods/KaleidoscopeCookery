package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.PlateBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.dispenser.OilPotDispenseBehavior;
import com.github.ysbbbbbb.kaleidoscopecookery.block.drink.TeacupBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteOneByTwoBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.create.CreateCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight.FarmersDelightCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.harvest.HarvestCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoupBases;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.PlateBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeacupItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = KaleidoscopeCookery.MOD_ID)
public class CommonRegistry {
    @SubscribeEvent
    public static void onSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(ModSoupBases::registerAll);
        event.enqueueWork(CommonRegistry::modCompat);
        event.enqueueWork(CommonRegistry::addDispenserBehavior);
    }

    @SubscribeEvent
    public static void onBlockRegistryEvent(RegisterEvent event) {
        if (event.getRegistry().equals(BuiltInRegistries.BLOCK)) {
            // 方块类食物
            FoodBiteRegistry.FOOD_DATA_MAP.forEach((resourceLocation, data) ->
                    event.register(BuiltInRegistries.BLOCK.key(), resourceLocation, () -> {
                        FoodBiteBlock biteBlock;
                        if (data.blockType() == FoodBiteRegistry.BlockType.ONE_BY_TWO) {
                            biteBlock = new FoodBiteOneByTwoBlock(data.blockFood(), data.maxBites(), data.animateTick());
                        } else {
                            biteBlock = new FoodBiteBlock(data.blockFood(), data.maxBites(), data.animateTick());
                        }

                        VoxelShape aabb = data.getAABB();
                        if (aabb != null) {
                            biteBlock.setAABB(aabb);
                        }
                        return biteBlock;
                    }));

            // 方块类茶水
            TeacupRegistry.TEACUP_DATA_MAP.forEach((resourceLocation, data) ->
                    event.register(BuiltInRegistries.BLOCK.key(), resourceLocation, () -> {
                        TeacupBlock teacupBlock = new TeacupBlock(data.getMaxCount());

                        VoxelShape aabb = data.getAABB();
                        if (aabb != null) {
                            teacupBlock.setAABB(aabb);
                        }
                        return teacupBlock;
                    }));

            // 方块类盘装食物
            PlateRegistry.PLATE_DATA_MAP.forEach((resourceLocation, data) -> {
                event.register(BuiltInRegistries.BLOCK.key(), resourceLocation, () -> {
                    PlateBlock plateBlock = new PlateBlock(data.getMaxCount(), data.getServingItems());

                    VoxelShape aabb = data.getAABB();
                    if (aabb != null) {
                        plateBlock.setAABB(aabb);
                    }
                    return plateBlock;
                });
            });
        }

        if (event.getRegistry().equals(BuiltInRegistries.ITEM)) {
            FoodBiteRegistry.FOOD_DATA_MAP.forEach((resourceLocation, data) -> {
                Block block = BuiltInRegistries.BLOCK.get(resourceLocation);
                // 选取第一个掉落物作为 usingConvertsTo
                ItemLike first = data.getLootItems().getFirst();
                event.register(BuiltInRegistries.ITEM.key(), resourceLocation,
                        () -> new BowlFoodBlockItem(block, data.itemFood(), first));
            });

            TeacupRegistry.TEACUP_DATA_MAP.forEach((resourceLocation, data) -> {
                Block block = BuiltInRegistries.BLOCK.get(resourceLocation);
                event.register(BuiltInRegistries.ITEM.key(), resourceLocation,
                        () -> new TeacupItem(block, data.getEffects()));
            });

            PlateRegistry.PLATE_DATA_MAP.forEach((resourceLocation, data) -> {
                Block block = BuiltInRegistries.BLOCK.get(resourceLocation);
                event.register(BuiltInRegistries.ITEM.key(), resourceLocation,
                        () -> new PlateBlockItem(block, resourceLocation.getPath()));
            });
        }
    }

    private static void modCompat() {
        FarmersDelightCompat.init();
        CreateCompat.init();
        HarvestCompat.init();
    }

    private static void addDispenserBehavior() {
        DispenserBlock.registerBehavior(ModItems.OIL_POT.get(), new OilPotDispenseBehavior());
    }
}
