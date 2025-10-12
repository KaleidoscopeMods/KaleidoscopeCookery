package com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.EnamelBasinBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.ChiliRistraBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class BlockLootTables extends FabricBlockLootTableProvider {
    public BlockLootTables(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.STOVE);
        dropSelf(ModBlocks.POT);
        dropSelf(ModBlocks.CHOPPING_BOARD);

        dropSelf(ModBlocks.OIL_POT);
        dropSelf(ModBlocks.OIL_POT);

        dropSelf(ModBlocks.COOK_STOOL_OAK);
        dropSelf(ModBlocks.COOK_STOOL_SPRUCE);
        dropSelf(ModBlocks.COOK_STOOL_ACACIA);
        dropSelf(ModBlocks.COOK_STOOL_BAMBOO);
        dropSelf(ModBlocks.COOK_STOOL_BIRCH);
        dropSelf(ModBlocks.COOK_STOOL_CHERRY);
        dropSelf(ModBlocks.COOK_STOOL_CRIMSON);
        dropSelf(ModBlocks.COOK_STOOL_DARK_OAK);
        dropSelf(ModBlocks.COOK_STOOL_JUNGLE);
        dropSelf(ModBlocks.COOK_STOOL_MANGROVE);
        dropSelf(ModBlocks.COOK_STOOL_WARPED);

        dropSelf(ModBlocks.CHAIR_OAK);
        dropSelf(ModBlocks.CHAIR_SPRUCE);
        dropSelf(ModBlocks.CHAIR_ACACIA);
        dropSelf(ModBlocks.CHAIR_BAMBOO);
        dropSelf(ModBlocks.CHAIR_BIRCH);
        dropSelf(ModBlocks.CHAIR_CHERRY);
        dropSelf(ModBlocks.CHAIR_CRIMSON);
        dropSelf(ModBlocks.CHAIR_DARK_OAK);
        dropSelf(ModBlocks.CHAIR_JUNGLE);
        dropSelf(ModBlocks.CHAIR_MANGROVE);
        dropSelf(ModBlocks.CHAIR_WARPED);

        dropSelf(ModBlocks.TABLE_OAK);
        dropSelf(ModBlocks.TABLE_SPRUCE);
        dropSelf(ModBlocks.TABLE_ACACIA);
        dropSelf(ModBlocks.TABLE_BAMBOO);
        dropSelf(ModBlocks.TABLE_BIRCH);
        dropSelf(ModBlocks.TABLE_CHERRY);
        dropSelf(ModBlocks.TABLE_CRIMSON);
        dropSelf(ModBlocks.TABLE_DARK_OAK);
        dropSelf(ModBlocks.TABLE_JUNGLE);
        dropSelf(ModBlocks.TABLE_MANGROVE);
        dropSelf(ModBlocks.TABLE_WARPED);

        dropSelf(ModBlocks.STOCKPOT);
        dropSelf(ModBlocks.FRUIT_BASKET);
        dropSelf(ModBlocks.KITCHENWARE_RACKS);
        dropSelf(ModBlocks.STRAW_BLOCK);
        dropSelf(ModBlocks.SHAWARMA_SPIT);
        dropSelf(ModBlocks.OIL_BLOCK);

        this.add(ModBlocks.TOMATO_CROP, createCropDrops(ModBlocks.TOMATO_CROP, ModItems.TOMATO,
                ModItems.TOMATO_SEED, createCropBuilder(ModBlocks.TOMATO_CROP)));

        var chiliBuilder = createCropBuilder(ModBlocks.CHILI_CROP);
        LootPoolSingletonContainer.Builder<?> greenChili = LootItem.lootTableItem(ModItems.GREEN_CHILI)
                .when(LootItemRandomChanceCondition.randomChance(0.2F));
        this.add(ModBlocks.CHILI_CROP, createCropDrops(ModBlocks.CHILI_CROP, ModItems.RED_CHILI, ModItems.CHILI_SEED, chiliBuilder)
                .withPool(LootPool.lootPool().when(chiliBuilder).add(greenChili)));

        var lettuceBuilder = createCropBuilder(ModBlocks.LETTUCE_CROP);
        LootPoolSingletonContainer.Builder<?> caterpillar = LootItem.lootTableItem(ModItems.CATERPILLAR)
                .when(LootItemRandomChanceCondition.randomChance(0.1F));
        this.add(ModBlocks.LETTUCE_CROP, createCropDrops(ModBlocks.LETTUCE_CROP, ModItems.LETTUCE, ModItems.LETTUCE_SEED, lettuceBuilder)
                .withPool(LootPool.lootPool().when(lettuceBuilder).add(caterpillar)));

        Item riceSeed = ModItems.WILD_RICE_SEED;
        LootItemCondition.Builder riceCropBuilder = createRiceCropBuilder();
        var countFunction = SetItemCountFunction.setCount(UniformGenerator.between(2, 4));
        LootPool.Builder ricePanicle = LootPool.lootPool().add(LootItem.lootTableItem(ModItems.RICE_PANICLE)
                .when(riceCropBuilder).apply(countFunction).otherwise(LootItem.lootTableItem(riceSeed)));
        LootPool.Builder extraRiceSeeds = LootPool.lootPool().add(LootItem.lootTableItem(riceSeed))
                .when(riceCropBuilder).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3));

        this.add(ModBlocks.RICE_CROP, this.applyExplosionDecay(ModBlocks.RICE_CROP,
                LootTable.lootTable().withPool(ricePanicle).withPool(extraRiceSeeds)));

        FoodBiteRegistry.FOOD_DATA_MAP.forEach(this::dropFoodBite);

        this.add(ModBlocks.ENAMEL_BASIN, createEnamelBasinLootTable());
        this.add(ModBlocks.CHILI_RISTRA, createChiliRistraLootTable());
    }

    private LootTable.Builder createChiliRistraLootTable() {
        LootPool.Builder builder = LootPool.lootPool();

        StatePropertiesPredicate.Builder isSheared = StatePropertiesPredicate.Builder.properties().hasProperty(ChiliRistraBlock.SHEARED, true);
        LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.CHILI_RISTRA).setProperties(isSheared);

        LootItemConditionalFunction.Builder<?> normalDrop = SetItemCountFunction.setCount(ConstantValue.exactly(6));
        LootItemConditionalFunction.Builder<?> shearedDrop = SetItemCountFunction.setCount(ConstantValue.exactly(3));

        LootPoolSingletonContainer.Builder<?> normalLoot = LootItem.lootTableItem(ModItems.RED_CHILI).apply(normalDrop);
        LootPoolSingletonContainer.Builder<?> shearedLoot = LootItem.lootTableItem(ModItems.RED_CHILI).apply(shearedDrop);

        builder.add(shearedLoot.when(condition).otherwise(normalLoot));

        return LootTable.lootTable().withPool(builder.when(ExplosionCondition.survivesExplosion()));
    }

    private LootTable.Builder createEnamelBasinLootTable() {
        LootPool.Builder oilDrop = LootPool.lootPool();
        for (int i = 1; i <= EnamelBasinBlock.MAX_OIL_COUNT; i++) {
            StatePropertiesPredicate.Builder property = StatePropertiesPredicate.Builder.properties().hasProperty(EnamelBasinBlock.OIL_COUNT, i);
            LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(ModBlocks.ENAMEL_BASIN)
                    .setProperties(property);
            int dropCount = i / 2;
            if (dropCount > 0) {
                LootItemConditionalFunction.Builder<?> count = SetItemCountFunction.setCount(ConstantValue.exactly(dropCount));
                oilDrop.add(LootItem.lootTableItem(ModItems.OIL).when(condition).apply(count));
            }
        }
        LootPool.Builder bucketDrop = LootPool.lootPool().add(LootItem.lootTableItem(Items.BUCKET));
        return LootTable.lootTable().withPool(oilDrop.when(ExplosionCondition.survivesExplosion()))
                .withPool(bucketDrop.when(ExplosionCondition.survivesExplosion()));
    }

    private LootItemCondition.Builder createCropBuilder(Block cropBlock) {
        StatePropertiesPredicate.Builder property = StatePropertiesPredicate.Builder
                .properties().hasProperty(CropBlock.AGE, 7);
        return LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(cropBlock)
                .setProperties(property);
    }

    private LootItemCondition.Builder createRiceCropBuilder() {
        StatePropertiesPredicate.Builder property = StatePropertiesPredicate.Builder
                .properties().hasProperty(CropBlock.AGE, 7)
                .hasProperty(RiceCropBlock.LOCATION, RiceCropBlock.DOWN);
        return LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.RICE_CROP)
                .setProperties(property);
    }

    private void dropFoodBite(ResourceLocation id, FoodBiteRegistry.FoodData data) {
        Block block = BuiltInRegistries.BLOCK.get(id);
        Item food = BuiltInRegistries.ITEM.get(id);
        if (!(block instanceof FoodBiteBlock foodBiteBlock)) {
            return;
        }
        ConstantValue exactly = ConstantValue.exactly(1);
        StatePropertiesPredicate.Builder notBite = StatePropertiesPredicate.Builder.properties().hasProperty(foodBiteBlock.getBites(), 0);
        LootItemCondition.Builder builder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(foodBiteBlock).setProperties(notBite);

        LootTable.Builder lootTable = LootTable.lootTable();
        for (int i = 0; i < data.getLootItems().size(); i++) {
            ItemLike itemLike = data.getLootItems().get(i);
            LootPool.Builder rolls = LootPool.lootPool().setRolls(exactly).when(ExplosionCondition.survivesExplosion());
            if (i == 0) {
                rolls.add(LootItem.lootTableItem(food).when(builder).otherwise(LootItem.lootTableItem(itemLike)));
            } else {
                rolls.add(EmptyLootItem.emptyItem().when(builder).otherwise(LootItem.lootTableItem(itemLike)));
            }
            lootTable.withPool(rolls);
        }
        this.add(block, lootTable);
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(KaleidoscopeCookery.MOD_ID, name);
    }
}
