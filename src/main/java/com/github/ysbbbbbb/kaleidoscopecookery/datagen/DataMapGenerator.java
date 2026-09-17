package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable.GiftLootTables;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModVillager;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.RaidHeroGift;

import java.util.concurrent.CompletableFuture;

public class DataMapGenerator extends DataMapProvider {
    private final Builder<Compostable, Item> compostableBuilder;
    private final Builder<RaidHeroGift, VillagerProfession> heroGiftBuilder;

    public DataMapGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
        this.compostableBuilder = builder(NeoForgeDataMaps.COMPOSTABLES);
        this.heroGiftBuilder = builder(NeoForgeDataMaps.RAID_HERO_GIFTS);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        addCompostable(ModItems.TOMATO_SEED, 0.3F);
        addCompostable(ModItems.CHILI_SEED, 0.3F);
        addCompostable(ModItems.LETTUCE_SEED, 0.3F);
        addCompostable(ModItems.WILD_RICE_SEED, 0.3F);
        addCompostable(ModItems.RICE_SEED, 0.3F);
        addCompostable(ModItems.TEA_SEED, 0.3F);
        addCompostable(ModItems.FRESH_TEA_LEAVES, 0.3F);
        addCompostable(ModItems.DRIED_TEA_LEAVES, 0.3F);
        addCompostable(ModItems.TOMATO, 0.65F);
        addCompostable(ModItems.RED_CHILI, 0.65F);
        addCompostable(ModItems.GREEN_CHILI, 0.65F);
        addCompostable(ModItems.LETTUCE, 0.65F);
        addCompostable(ModItems.RICE_PANICLE, 0.65F);
        addCompostable(ModItems.CATERPILLAR, 1.0F);

        RaidHeroGift gift = new RaidHeroGift(ResourceKey.create(Registries.LOOT_TABLE, GiftLootTables.CHEF_GIFT));
        heroGiftBuilder.add(ModVillager.CHEF, gift, false);
    }

    private void addCompostable(DeferredItem<Item> item, float chance) {
        compostableBuilder.add(item, new Compostable(chance, true), false);
    }
}
