package com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class GiftLootTables implements LootTableSubProvider {
    public static final ResourceLocation CHEF_GIFT = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "gameplay/hero_of_the_village/chef_gift");

    public GiftLootTables(HolderLookup.Provider registries) {
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(ResourceKey.create(Registries.LOOT_TABLE, CHEF_GIFT), LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL.get()))
                        .add(LootItem.lootTableItem(ModItems.BRAISED_BEEF_RICE_BOWL.get()))
                        .add(LootItem.lootTableItem(ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL.get()))
                        .add(LootItem.lootTableItem(ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL.get()))
                        .add(LootItem.lootTableItem(ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL.get()))
        ));
    }
}
