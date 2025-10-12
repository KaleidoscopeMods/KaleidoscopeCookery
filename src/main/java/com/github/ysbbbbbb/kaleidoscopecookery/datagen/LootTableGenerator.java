package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable.BlockLootTables;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable.ChestLootTables;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable.GiftLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class LootTableGenerator extends LootTableProvider {
    public LootTableGenerator(FabricDataOutput output) {
        super(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(() -> new BlockLootTables(output), LootContextParamSets.BLOCK),
//                new LootTableProvider.SubProviderEntry(() -> new EntityLootTables(output), LootContextParamSets.ENTITY),
                new LootTableProvider.SubProviderEntry(ChestLootTables::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(GiftLootTables::new, LootContextParamSets.GIFT)
        ));
    }
}
