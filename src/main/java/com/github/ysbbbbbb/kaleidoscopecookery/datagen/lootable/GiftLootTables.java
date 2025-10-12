package com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.data.loot.packs.VanillaGiftLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class GiftLootTables extends VanillaGiftLoot {
    public static final ResourceLocation CHEF_GIFT = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "gameplay/hero_of_the_village/chef_gift");

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(CHEF_GIFT, LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL))
                        .add(LootItem.lootTableItem(ModItems.STIR_FRIED_BEEF_OFFAL_RICE_BOWL))
                        .add(LootItem.lootTableItem(ModItems.BRAISED_BEEF_RICE_BOWL))
                        .add(LootItem.lootTableItem(ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL))
                        .add(LootItem.lootTableItem(ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL))
                        .add(LootItem.lootTableItem(ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL))
                        .add(LootItem.lootTableItem(ModItems.BRAISED_FISH_RICE_BOWL))
                        .add(LootItem.lootTableItem(ModItems.SPICY_CHICKEN_RICE_BOWL))
                        .add(LootItem.lootTableItem(ModItems.DELICIOUS_EGG_FRIED_RICE))
        ));
    }
}
