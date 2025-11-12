package com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.AdvanceEntityMatchTool;
import com.google.common.collect.Sets;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class EntityLootTables extends EntityLootSubProvider {
    public final Set<EntityType<?>> knownEntities = Sets.newHashSet();

    public EntityLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        super.generate(output);

        // 厨具刀杀猪掉油
        ItemPredicate hasKnife = ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE).build();
        LootItemCondition.Builder toolMatches = AdvanceEntityMatchTool.toolMatches(EquipmentSlot.MAINHAND, hasKnife);
        var count = SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F));
        var looting = LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F));
        var oil = LootItem.lootTableItem(ModItems.OIL.get()).apply(count).apply(looting);
        var donkeyMeat = LootItem.lootTableItem(ModItems.RAW_DONKEY_MEAT.get()).apply(count).apply(looting);

        LootTable.Builder lessOil = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(oil).when(toolMatches));
        LootTable.Builder moreOil = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2)).add(oil).when(toolMatches));
        LootTable.Builder moreDonkeyMeat = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2)).add(donkeyMeat).when(toolMatches));

        output.accept(modLoc("pig"), lessOil);
        output.accept(modLoc("zombified_piglin"), lessOil);
        output.accept(modLoc("piglin"), moreOil);
        output.accept(modLoc("piglin_brute"), moreOil);
        output.accept(modLoc("hoglin"), moreOil);
        output.accept(modLoc("zoglin"), moreOil);
        // 厨刀杀驴掉驴肉
        output.accept(modLoc("donkey"), moreDonkeyMeat);
    }

    @Override
    public void generate() {
    }

    @Override
    protected boolean canHaveLootTable(EntityType<?> type) {
        return true;
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return knownEntities.stream();
    }

    @Override
    protected void add(EntityType<?> type, LootTable.Builder builder) {
        this.add(type, type.getDefaultLootTable(), builder);
    }

    @Override
    protected void add(EntityType<?> type, ResourceLocation lootTable, LootTable.Builder builder) {
        super.add(type, lootTable, builder);
        knownEntities.add(type);
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(KaleidoscopeCookery.MOD_ID, name);
    }
}
