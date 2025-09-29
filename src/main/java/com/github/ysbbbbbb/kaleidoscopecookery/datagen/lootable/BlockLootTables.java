package com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.EnamelBasinBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.ChiliRistraBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.AdvanceBlockMatchTool;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
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

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class BlockLootTables extends BlockLootSubProvider {
    public final Set<Block> knownBlocks = new HashSet<>();
    public final HolderLookup.RegistryLookup<Enchantment> enchantment;

    public BlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        this.enchantment = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.STOVE.get());
        dropSelf(ModBlocks.POT.get());
        dropSelf(ModBlocks.CHOPPING_BOARD.get());

        dropSelf(ModBlocks.OIL_POT.get());

        dropSelf(ModBlocks.COOK_STOOL_OAK.get());
        dropSelf(ModBlocks.COOK_STOOL_SPRUCE.get());
        dropSelf(ModBlocks.COOK_STOOL_ACACIA.get());
        dropSelf(ModBlocks.COOK_STOOL_BAMBOO.get());
        dropSelf(ModBlocks.COOK_STOOL_BIRCH.get());
        dropSelf(ModBlocks.COOK_STOOL_CHERRY.get());
        dropSelf(ModBlocks.COOK_STOOL_CRIMSON.get());
        dropSelf(ModBlocks.COOK_STOOL_DARK_OAK.get());
        dropSelf(ModBlocks.COOK_STOOL_JUNGLE.get());
        dropSelf(ModBlocks.COOK_STOOL_MANGROVE.get());
        dropSelf(ModBlocks.COOK_STOOL_WARPED.get());

        dropSelf(ModBlocks.CHAIR_OAK.get());
        dropSelf(ModBlocks.CHAIR_SPRUCE.get());
        dropSelf(ModBlocks.CHAIR_ACACIA.get());
        dropSelf(ModBlocks.CHAIR_BAMBOO.get());
        dropSelf(ModBlocks.CHAIR_BIRCH.get());
        dropSelf(ModBlocks.CHAIR_CHERRY.get());
        dropSelf(ModBlocks.CHAIR_CRIMSON.get());
        dropSelf(ModBlocks.CHAIR_DARK_OAK.get());
        dropSelf(ModBlocks.CHAIR_JUNGLE.get());
        dropSelf(ModBlocks.CHAIR_MANGROVE.get());
        dropSelf(ModBlocks.CHAIR_WARPED.get());

        dropSelf(ModBlocks.TABLE_OAK.get());
        dropSelf(ModBlocks.TABLE_SPRUCE.get());
        dropSelf(ModBlocks.TABLE_ACACIA.get());
        dropSelf(ModBlocks.TABLE_BAMBOO.get());
        dropSelf(ModBlocks.TABLE_BIRCH.get());
        dropSelf(ModBlocks.TABLE_CHERRY.get());
        dropSelf(ModBlocks.TABLE_CRIMSON.get());
        dropSelf(ModBlocks.TABLE_DARK_OAK.get());
        dropSelf(ModBlocks.TABLE_JUNGLE.get());
        dropSelf(ModBlocks.TABLE_MANGROVE.get());
        dropSelf(ModBlocks.TABLE_WARPED.get());

        dropSelf(ModBlocks.STOCKPOT.get());
        dropSelf(ModBlocks.FRUIT_BASKET.get());
        dropSelf(ModBlocks.KITCHENWARE_RACKS.get());
        dropSelf(ModBlocks.STRAW_BLOCK.get());
        dropSelf(ModBlocks.SHAWARMA_SPIT.get());
        dropSelf(ModBlocks.OIL_BLOCK.get());

        this.add(ModBlocks.TOMATO_CROP.get(), createCropDrops(ModBlocks.TOMATO_CROP.get(), ModItems.TOMATO.get(),
                ModItems.TOMATO_SEED.get(), createCropBuilder(ModBlocks.TOMATO_CROP.get())));

        var chiliBuilder = createCropBuilder(ModBlocks.CHILI_CROP.get());
        LootPoolSingletonContainer.Builder<?> greenChili = LootItem.lootTableItem(ModItems.GREEN_CHILI.get())
                .when(LootItemRandomChanceCondition.randomChance(0.2F));
        this.add(ModBlocks.CHILI_CROP.get(), createCropDrops(ModBlocks.CHILI_CROP.get(), ModItems.RED_CHILI.get(), ModItems.CHILI_SEED.get(), chiliBuilder)
                .withPool(LootPool.lootPool().when(chiliBuilder).add(greenChili)));

        var lettuceBuilder = createCropBuilder(ModBlocks.LETTUCE_CROP.get());
        LootPoolSingletonContainer.Builder<?> caterpillar = LootItem.lootTableItem(ModItems.CATERPILLAR.get())
                .when(LootItemRandomChanceCondition.randomChance(0.1F));
        this.add(ModBlocks.LETTUCE_CROP.get(), createCropDrops(ModBlocks.LETTUCE_CROP.get(), ModItems.LETTUCE.get(), ModItems.LETTUCE_SEED.get(), lettuceBuilder)
                .withPool(LootPool.lootPool().when(lettuceBuilder).add(caterpillar)));

        Item riceSeed = ModItems.WILD_RICE_SEED.get();
        LootItemCondition.Builder riceCropBuilder = createRiceCropBuilder();
        var countFunction = SetItemCountFunction.setCount(UniformGenerator.between(2, 4));
        LootPool.Builder ricePanicle = LootPool.lootPool().add(LootItem.lootTableItem(ModItems.RICE_PANICLE.get())
                .when(riceCropBuilder).apply(countFunction).otherwise(LootItem.lootTableItem(riceSeed)));
        LootPool.Builder extraRiceSeeds = LootPool.lootPool().add(LootItem.lootTableItem(riceSeed))
                .when(riceCropBuilder).apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantment.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3));

        this.add(ModBlocks.RICE_CROP.get(), this.applyExplosionDecay(ModBlocks.RICE_CROP.get(),
                LootTable.lootTable().withPool(ricePanicle).withPool(extraRiceSeeds)));

        FoodBiteRegistry.FOOD_DATA_MAP.forEach(this::dropFoodBite);

        this.add(ModBlocks.ENAMEL_BASIN.get(), createEnamelBasinLootTable());
        this.add(ModBlocks.CHILI_RISTRA.get(), createChiliRistraLootTable());
    }

    private LootTable.Builder createChiliRistraLootTable() {
        LootPool.Builder builder = LootPool.lootPool();

        StatePropertiesPredicate.Builder isSheared = StatePropertiesPredicate.Builder.properties().hasProperty(ChiliRistraBlock.SHEARED, true);
        LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.CHILI_RISTRA.get()).setProperties(isSheared);

        LootItemConditionalFunction.Builder<?> normalDrop = SetItemCountFunction.setCount(ConstantValue.exactly(6));
        LootItemConditionalFunction.Builder<?> shearedDrop = SetItemCountFunction.setCount(ConstantValue.exactly(3));

        LootPoolSingletonContainer.Builder<?> normalLoot = LootItem.lootTableItem(ModItems.RED_CHILI.get()).apply(normalDrop);
        LootPoolSingletonContainer.Builder<?> shearedLoot = LootItem.lootTableItem(ModItems.RED_CHILI.get()).apply(shearedDrop);

        builder.add(shearedLoot.when(condition).otherwise(normalLoot));

        return LootTable.lootTable().withPool(builder.when(ExplosionCondition.survivesExplosion()));
    }

    private LootTable.Builder createEnamelBasinLootTable() {
        LootPool.Builder oilDrop = LootPool.lootPool();
        for (int i = 1; i <= EnamelBasinBlock.MAX_OIL_COUNT; i++) {
            StatePropertiesPredicate.Builder property = StatePropertiesPredicate.Builder.properties().hasProperty(EnamelBasinBlock.OIL_COUNT, i);
            LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(ModBlocks.ENAMEL_BASIN.get())
                    .setProperties(property);
            int dropCount = i / 2;
            if (dropCount > 0) {
                LootItemConditionalFunction.Builder<?> count = SetItemCountFunction.setCount(ConstantValue.exactly(dropCount));
                oilDrop.add(LootItem.lootTableItem(ModItems.OIL.get()).when(condition).apply(count));
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
                .hasBlockStateProperties(ModBlocks.RICE_CROP.get())
                .setProperties(property);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        super.generate(output);

        // 穿戴草帽掉落番茄辣椒等种子
        var tomato = getSeed(ModItems.TOMATO_SEED.get());
        var chili = getSeed(ModItems.CHILI_SEED.get());
        var lettuce = getSeed(ModItems.LETTUCE_SEED.get());
        var rice = getSeed(ModItems.WILD_RICE_SEED.get());
        LootTable.Builder dropSeed = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(tomato).add(chili).add(lettuce).add(rice));
        ResourceKey<LootTable> id = ResourceKey.create(Registries.LOOT_TABLE, modLoc("straw_hat_seed_drop"));
        output.accept(id, dropSeed);
    }

    private LootPoolSingletonContainer.Builder<?> getSeed(ItemLike item) {
        ItemPredicate hasHat = ItemPredicate.Builder.item().of(TagMod.STRAW_HAT).build();
        LootItemCondition.Builder hatMatches = AdvanceBlockMatchTool.toolMatches(EquipmentSlot.HEAD, hasHat);
        return LootItem.lootTableItem(item)
                .when(LootItemRandomChanceCondition.randomChance(0.125F)).when(hatMatches)
                .apply(ApplyBonusCount.addUniformBonusCount(enchantment.getOrThrow(Enchantments.FORTUNE), 2));
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

    @Override
    public void add(Block block, LootTable.Builder builder) {
        this.knownBlocks.add(block);
        super.add(block, builder);
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return this.knownBlocks;
    }

    public ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, name);
    }
}
