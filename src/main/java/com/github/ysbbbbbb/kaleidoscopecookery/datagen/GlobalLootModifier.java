package com.github.ysbbbbbb.kaleidoscopecookery.datagen;


import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class GlobalLootModifier extends GlobalLootModifierProvider {
    public GlobalLootModifier(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, KaleidoscopeCookery.MOD_ID);
    }

    @Override
    public void start() {
        addEntityLootModifier("pig", EntityType.PIG);
        addEntityLootModifier("zombified_piglin", EntityType.ZOMBIFIED_PIGLIN);
        addEntityLootModifier("piglin", EntityType.PIGLIN);
        addEntityLootModifier("piglin_brute", EntityType.PIGLIN_BRUTE);
        addEntityLootModifier("hoglin", EntityType.HOGLIN);
        addEntityLootModifier("zoglin", EntityType.ZOGLIN);
        addEntityLootModifier("donkey", EntityType.DONKEY);

        addBlockLootModifier("straw_hat_seed_drop", Blocks.SHORT_GRASS);
    }

    private void addEntityLootModifier(String name, EntityType<?> type) {
        LootItemCondition condition = LootTableIdCondition.builder(type.getDefaultLootTable().location()).build();
        var conditions = new LootItemCondition[]{condition};
        this.add(name, new AddTableLootModifier(conditions, modLoc(name)));
    }

    private void addBlockLootModifier(String name, Block block) {
        LootItemCondition condition = LootTableIdCondition.builder(block.getLootTable().location()).build();
        var conditions = new LootItemCondition[]{condition};
        this.add(name, new AddTableLootModifier(conditions, modLoc(name)));
    }

    public ResourceKey<LootTable> modLoc(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, name));
    }
}
