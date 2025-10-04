package com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.RecipeRandomlyFunction;
import net.minecraft.data.loot.packs.VanillaChestLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class ChestLootTables extends VanillaChestLoot {
    public static final ResourceLocation VILLAGE_CHEST = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chest/village_chest");
    public static final ResourceLocation VILLAGE_HIDE_CHEST = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chest/village_hide_chest");

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(VILLAGE_CHEST, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(UniformGenerator.between(3, 8))

                .add(LootItem.lootTableItem(ModItems.TOMATO.get()).setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 7))))

                .add(LootItem.lootTableItem(ModItems.RED_CHILI.get()).setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))

                .add(LootItem.lootTableItem(ModItems.OIL.get()).setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))

                .add(LootItem.lootTableItem(ModItems.TOMATO_SEED.get()).setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))

                .add(LootItem.lootTableItem(ModItems.CHILI_SEED.get()).setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))

                .add(LootItem.lootTableItem(ModItems.LETTUCE_SEED.get()).setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))

                .add(LootItem.lootTableItem(ModItems.WILD_RICE_SEED.get()).setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))

                .add(LootItem.lootTableItem(ModItems.RICE_PANICLE.get()).setWeight(8)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))

                .add(LootItem.lootTableItem(ModItems.STRAW_BLOCK.get()).setWeight(8)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))

                .add(LootItem.lootTableItem(ModItems.RECIPE_ITEM.get()).setWeight(8)
                        .apply(RecipeRandomlyFunction.randomRecipe()))

                .add(LootItem.lootTableItem(ModItems.IRON_KITCHEN_KNIFE.get()).setWeight(5))
                .add(LootItem.lootTableItem(ModItems.STRAW_HAT.get()).setWeight(5))
        ));

        consumer.accept(VILLAGE_HIDE_CHEST, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(UniformGenerator.between(2, 3))
                .add(LootItem.lootTableItem(ModItems.SEAFOOD_MISO_SOUP.get()).setWeight(10))
                .add(LootItem.lootTableItem(ModItems.CHICKEN_AND_MUSHROOM_STEW.get()).setWeight(10))
                .add(LootItem.lootTableItem(ModItems.PORK_BONE_SOUP.get()).setWeight(10))
                .add(LootItem.lootTableItem(ModItems.BRAISED_BEEF_WITH_POTATOES.get()).setWeight(10))
                .add(LootItem.lootTableItem(ModItems.BEEF_MEATBALL_SOUP.get()).setWeight(10))
                .add(LootItem.lootTableItem(ModItems.SUSPICIOUS_STIR_FRY_RICE_BOWL.get()).setWeight(10))
                .add(LootItem.lootTableItem(ModItems.EGG_FRIED_RICE.get()).setWeight(10))
                .add(LootItem.lootTableItem(FoodBiteRegistry.getItem(FoodBiteRegistry.SLIME_BALL_MEAL)).setWeight(10))
        ));
    }
}
