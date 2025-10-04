package com.github.ysbbbbbb.kaleidoscopecookery.init.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * 按照社区统一规范，使用的 Tag
 * <p>
 * 蔬菜类的有两种，本模组遵循农夫乐事规范，采用 crops/前缀
 */
public interface TagCommon {
    TagKey<Item> CROPS = itemTag("crops");
    TagKey<Item> CROPS_CHILI_PEPPER = itemTag("crops/chilipepper");
    TagKey<Item> CROPS_TOMATO = itemTag("crops/tomato");
    TagKey<Item> CROPS_LETTUCE = itemTag("crops/lettuce");
    TagKey<Item> CROPS_RICE = itemTag("crops/rice");

    TagKey<Item> VEGETABLES = itemTag("vegetables");
    TagKey<Item> VEGETABLES_CHILI_PEPPER = itemTag("vegetables/chilipepper");
    TagKey<Item> VEGETABLES_TOMATO = itemTag("vegetables/tomato");
    TagKey<Item> VEGETABLES_LETTUCE = itemTag("vegetables/lettuce");

    TagKey<Item> SEEDS_CHILI_PEPPER = itemTag("seeds/chilipepper");
    TagKey<Item> SEEDS_TOMATO = itemTag("seeds/tomato");
    TagKey<Item> SEEDS_LETTUCE = itemTag("seeds/lettuce");
    TagKey<Item> SEEDS_RICE = itemTag("seeds/rice");

    TagKey<Item> GRAIN_RICE = itemTag("grain/rice");

    TagKey<Item> COOKED_BEEF = itemTag("cooked_beef");
    TagKey<Item> COOKED_PORK = itemTag("cooked_pork");
    TagKey<Item> COOKED_MUTTON = itemTag("cooked_mutton");
    TagKey<Item> COOKED_EGGS = itemTag("cooked_eggs");
    TagKey<Item> COOKED_RICE = itemTag("cooked_rice");

    // forge:raw_meats 生肉
    TagKey<Item> RAW_MEATS = itemTag("raw_meats");

    TagKey<Item> RAW_BEEF = itemTag("raw_beef");
    TagKey<Item> RAW_CHICKEN = itemTag("raw_chicken");
    TagKey<Item> RAW_PORK = itemTag("raw_pork");
    TagKey<Item> RAW_MUTTON = itemTag("raw_mutton");
    TagKey<Item> EGGS = itemTag("eggs");
    TagKey<Item> RAW_FISHES_COD = itemTag("raw_fishes/cod");
    TagKey<Item> RAW_FISHES_SALMON = itemTag("raw_fishes/salmon");
    TagKey<Item> RAW_FISHES_TROPICAL = itemTag("raw_fishes/tropical_fish");

    TagKey<Item> DOUGH = itemTag("dough");

    // 均衡饮食模组
    TagKey<Item> FRUITS = dietTag("fruits");
    TagKey<Item> GRAINS = dietTag("grains");
    TagKey<Item> PROTEINS = dietTag("proteins");
    TagKey<Item> SUGARS = dietTag("sugars");
    TagKey<Item> DIET_VEGETABLES = dietTag("vegetables");

    // 静谧四季模组
    TagKey<Item> SPRING_CROPS = seasonsItemTag("spring_crops");
    TagKey<Item> SUMMER_CROPS = seasonsItemTag("summer_crops");
    TagKey<Item> AUTUMN_CROPS = seasonsItemTag("autumn_crops");
    TagKey<Item> WINTER_CROPS = seasonsItemTag("winter_crops");

    TagKey<Block> SPRING_CROPS_BLOCK = seasonsBlockTag("spring_crops");
    TagKey<Block> SUMMER_CROPS_BLOCK = seasonsBlockTag("summer_crops");
    TagKey<Block> AUTUMN_CROPS_BLOCK = seasonsBlockTag("autumn_crops");
    TagKey<Block> WINTER_CROPS_BLOCK = seasonsBlockTag("winter_crops");

    // 节气模组
    TagKey<Block> DRY_AVERAGE = eclipticSeasonsTag("crops/dry_average");
    TagKey<Block> AVERAGE_MOIST = eclipticSeasonsTag("crops/average_moist");
    TagKey<Block> MOIST_HUMID = eclipticSeasonsTag("crops/moist_humid");
    TagKey<Block> HUMID_HUMID = eclipticSeasonsTag("crops/humid_humid");

    // 农夫乐事
    TagKey<Item> FD_KNIVES = TagKey.create(Registries.ITEM, new ResourceLocation("farmersdelight:tools/knives"));

    static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("forge", name));
    }

    /**
     * 兼容均衡饮食模组
     */
    static TagKey<Item> dietTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("diet", name));
    }

    /**
     * 静谧四季模组兼容
     */
    static TagKey<Item> seasonsItemTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("sereneseasons", name));
    }

    static TagKey<Block> seasonsBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation("sereneseasons", name));
    }

    static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation("forge", name));
    }

    static TagKey<Block> eclipticSeasonsTag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation("eclipticseasons", name));
    }
}
