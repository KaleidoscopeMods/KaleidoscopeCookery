package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.item.*;
import com.github.ysbbbbbb.kaleidoscopecookery.item.armor.FarmerArmorMaterial;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

public final class ModItems {
    // 厨具
    public static final Item STOVE = new BlockItem(ModBlocks.STOVE, new Item.Properties());
    public static final Item POT = new BlockItem(ModBlocks.POT, new Item.Properties());
    public static final Item STOCKPOT = new BlockItem(ModBlocks.STOCKPOT, new Item.Properties());
    public static final Item STOCKPOT_LID = new StockpotLidItem();
    public static final Item CHOPPING_BOARD = new BlockItem(ModBlocks.CHOPPING_BOARD, new Item.Properties());
    public static final Item KITCHENWARE_RACKS = new BlockItem(ModBlocks.KITCHENWARE_RACKS, new Item.Properties());
    public static final Item SHAWARMA_SPIT = new BlockItem(ModBlocks.SHAWARMA_SPIT, new Item.Properties());
    public static final Item MILLSTONE = new BlockItem(ModBlocks.MILLSTONE, new Item.Properties());
    public static final Item STEAMER = new BlockItem(ModBlocks.STEAMER, new Item.Properties());

    // 油
    public static final Item OIL = new Item(new Item.Properties());
    public static final Item OIL_POT = new OilPotItem();
    public static final Item OIL_BLOCK = new BlockItem(ModBlocks.OIL_BLOCK, new Item.Properties());
    public static final Item ENAMEL_BASIN = new BlockItem(ModBlocks.ENAMEL_BASIN, new Item.Properties());

    // 杂项
    public static final Item CHILI_RISTRA = new BlockItem(ModBlocks.CHILI_RISTRA, new Item.Properties());
    public static final Item STRAW_BLOCK = new BlockItem(ModBlocks.STRAW_BLOCK, new Item.Properties());
    public static Item FRUIT_BASKET = Items.AIR;
//    public static final Item FRUIT_BASKET = new FruitBasketItem();
    public static final Item SCARECROW = new ScarecrowItem();
    public static final Item RECIPE_ITEM = new RecipeItem();
    public static Item TRANSMUTATION_LUNCH_BAG = Items.AIR;
//    public static final Item TRANSMUTATION_LUNCH_BAG = new TransmutationLunchBagItem();

    // 工具
    public static final Item IRON_KITCHEN_KNIFE = new KitchenKnifeItem(Tiers.IRON);
    public static final Item GOLD_KITCHEN_KNIFE = new KitchenKnifeItem(Tiers.GOLD);
    public static final Item DIAMOND_KITCHEN_KNIFE = new KitchenKnifeItem(Tiers.DIAMOND);
    public static final Item NETHERITE_KITCHEN_KNIFE = new KitchenKnifeItem(Tiers.NETHERITE);
    public static final Item KITCHEN_SHOVEL = new KitchenShovelItem();

    // 服装
    public static final Item STRAW_HAT = new StrawHatItem(false);
    public static final Item STRAW_HAT_FLOWER = new StrawHatItem(true);
    public static final Item FARMER_CHEST_PLATE = new ArmorItem(FarmerArmorMaterial.INSTANCE, ArmorItem.Type.CHESTPLATE, new Item.Properties());
    public static final Item FARMER_LEGGINGS = new ArmorItem(FarmerArmorMaterial.INSTANCE, ArmorItem.Type.LEGGINGS, new Item.Properties());
    public static final Item FARMER_BOOTS = new ArmorItem(FarmerArmorMaterial.INSTANCE, ArmorItem.Type.BOOTS, new Item.Properties());

    // 种子
    public static final Item TOMATO_SEED = new ItemNameBlockItem(ModBlocks.TOMATO_CROP, new Item.Properties());
    public static final Item CHILI_SEED = new ItemNameBlockItem(ModBlocks.CHILI_CROP, new Item.Properties());
    public static final Item LETTUCE_SEED = new ItemNameBlockItem(ModBlocks.LETTUCE_CROP, new Item.Properties());
    public static final Item RICE_SEED = new ItemNameBlockItem(ModBlocks.RICE_CROP, new Item.Properties());
    public static final Item WILD_RICE_SEED = new ItemNameBlockItem(ModBlocks.RICE_CROP, new Item.Properties());

    // 凳子
    public static final Item COOK_STOOL_OAK = new BlockItem(ModBlocks.COOK_STOOL_OAK, new Item.Properties());
    public static final Item COOK_STOOL_SPRUCE = new BlockItem(ModBlocks.COOK_STOOL_SPRUCE, new Item.Properties());
    public static final Item COOK_STOOL_ACACIA = new BlockItem(ModBlocks.COOK_STOOL_ACACIA, new Item.Properties());
    public static final Item COOK_STOOL_BAMBOO = new BlockItem(ModBlocks.COOK_STOOL_BAMBOO, new Item.Properties());
    public static final Item COOK_STOOL_BIRCH = new BlockItem(ModBlocks.COOK_STOOL_BIRCH, new Item.Properties());
    public static final Item COOK_STOOL_CHERRY = new BlockItem(ModBlocks.COOK_STOOL_CHERRY, new Item.Properties());
    public static final Item COOK_STOOL_CRIMSON = new BlockItem(ModBlocks.COOK_STOOL_CRIMSON, new Item.Properties());
    public static final Item COOK_STOOL_DARK_OAK = new BlockItem(ModBlocks.COOK_STOOL_DARK_OAK, new Item.Properties());
    public static final Item COOK_STOOL_JUNGLE = new BlockItem(ModBlocks.COOK_STOOL_JUNGLE, new Item.Properties());
    public static final Item COOK_STOOL_MANGROVE = new BlockItem(ModBlocks.COOK_STOOL_MANGROVE, new Item.Properties());
    public static final Item COOK_STOOL_WARPED = new BlockItem(ModBlocks.COOK_STOOL_WARPED, new Item.Properties());

    // 椅子
    public static final Item CHAIR_OAK = new BlockItem(ModBlocks.CHAIR_OAK, new Item.Properties());
    public static final Item CHAIR_SPRUCE = new BlockItem(ModBlocks.CHAIR_SPRUCE, new Item.Properties());
    public static final Item CHAIR_ACACIA = new BlockItem(ModBlocks.CHAIR_ACACIA, new Item.Properties());
    public static final Item CHAIR_BAMBOO = new BlockItem(ModBlocks.CHAIR_BAMBOO, new Item.Properties());
    public static final Item CHAIR_BIRCH = new BlockItem(ModBlocks.CHAIR_BIRCH, new Item.Properties());
    public static final Item CHAIR_CHERRY = new BlockItem(ModBlocks.CHAIR_CHERRY, new Item.Properties());
    public static final Item CHAIR_CRIMSON = new BlockItem(ModBlocks.CHAIR_CRIMSON, new Item.Properties());
    public static final Item CHAIR_DARK_OAK = new BlockItem(ModBlocks.CHAIR_DARK_OAK, new Item.Properties());
    public static final Item CHAIR_JUNGLE = new BlockItem(ModBlocks.CHAIR_JUNGLE, new Item.Properties());
    public static final Item CHAIR_MANGROVE = new BlockItem(ModBlocks.CHAIR_MANGROVE, new Item.Properties());
    public static final Item CHAIR_WARPED = new BlockItem(ModBlocks.CHAIR_WARPED, new Item.Properties());

    // 桌子
    public static final Item TABLE_OAK = new BlockItem(ModBlocks.TABLE_OAK, new Item.Properties());
    public static final Item TABLE_SPRUCE = new BlockItem(ModBlocks.TABLE_SPRUCE, new Item.Properties());
    public static final Item TABLE_ACACIA = new BlockItem(ModBlocks.TABLE_ACACIA, new Item.Properties());
    public static final Item TABLE_BAMBOO = new BlockItem(ModBlocks.TABLE_BAMBOO, new Item.Properties());
    public static final Item TABLE_BIRCH = new BlockItem(ModBlocks.TABLE_BIRCH, new Item.Properties());
    public static final Item TABLE_CHERRY = new BlockItem(ModBlocks.TABLE_CHERRY, new Item.Properties());
    public static final Item TABLE_CRIMSON = new BlockItem(ModBlocks.TABLE_CRIMSON, new Item.Properties());
    public static final Item TABLE_DARK_OAK = new BlockItem(ModBlocks.TABLE_DARK_OAK, new Item.Properties());
    public static final Item TABLE_JUNGLE = new BlockItem(ModBlocks.TABLE_JUNGLE, new Item.Properties());
    public static final Item TABLE_MANGROVE = new BlockItem(ModBlocks.TABLE_MANGROVE, new Item.Properties());
    public static final Item TABLE_WARPED = new BlockItem(ModBlocks.TABLE_WARPED, new Item.Properties());

    // 蔬菜食材
    public static final Item TOMATO = new Item(new Item.Properties().food(ModFoods.TOMATO));
    public static final Item RED_CHILI = new ChiliItem(2);
    public static final Item GREEN_CHILI = new ChiliItem(1);
    public static final Item LETTUCE = new Item(new Item.Properties().food(ModFoods.LETTUCE));
    public static final Item RICE_PANICLE = new Item(new Item.Properties());
    public static final Item CATERPILLAR = new Item(new Item.Properties().food(ModFoods.CATERPILLAR));

    // 面类食材
    public static final Item RAW_DOUGH = new RawDoughItem();
    public static final Item RAW_NOODLES = new Item(new Item.Properties());
    public static final Item STUFFED_DOUGH_FOOD = new Item(new Item.Properties());

    // 基础食物
    public static final Item FRIED_EGG = new Item(new Item.Properties().food(ModFoods.FRIED_EGG));
    public static final Item DONKEY_BURGER = new FoodWithEffectsItem(ModFoods.DONKEY_BURGER);
    public static final Item BAOZI = new FoodWithEffectsItem(ModFoods.BAOZI);
    public static final Item DUMPLING = new FoodWithEffectsItem(ModFoods.DUMPLING);
    public static final Item SAMSA = new FoodWithEffectsItem(ModFoods.SAMSA);
    public static final Item MANTOU = new FoodWithEffectsItem(ModFoods.MANTOU);
    public static final Item MEAT_PIE = new FoodWithEffectsItem(ModFoods.MEAT_PIE);

    // 小碗菜和盖饭
    public static final Item COOKED_RICE = new BowlFoodOnlyItem(ModFoods.COOKED_RICE);
    public static final Item EGG_FRIED_RICE = new BowlFoodOnlyItem(ModFoods.EGG_FRIED_RICE);
    public static final Item DELICIOUS_EGG_FRIED_RICE = new BowlFoodOnlyItem(ModFoods.DELICIOUS_EGG_FRIED_RICE);
    public static final Item SCRAMBLE_EGG_WITH_TOMATOES = new BowlFoodOnlyItem(ModFoods.SCRAMBLE_EGG_WITH_TOMATOES);
    public static final Item SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL = new BowlFoodOnlyItem(ModFoods.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL);
    public static final Item STIR_FRIED_BEEF_OFFAL = new BowlFoodOnlyItem(ModFoods.STIR_FRIED_BEEF_OFFAL);
    public static final Item STIR_FRIED_BEEF_OFFAL_RICE_BOWL = new BowlFoodOnlyItem(ModFoods.STIR_FRIED_BEEF_OFFAL_RICE_BOWL);
    public static final Item BRAISED_BEEF = new BowlFoodOnlyItem(ModFoods.BRAISED_BEEF);
    public static final Item BRAISED_BEEF_RICE_BOWL = new BowlFoodOnlyItem(ModFoods.BRAISED_BEEF_RICE_BOWL);
    public static final Item STIR_FRIED_PORK_WITH_PEPPERS = new BowlFoodOnlyItem(ModFoods.STIR_FRIED_PORK_WITH_PEPPERS);
    public static final Item STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL = new BowlFoodOnlyItem(ModFoods.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL);
    public static final Item SWEET_AND_SOUR_PORK = new BowlFoodOnlyItem(ModFoods.SWEET_AND_SOUR_PORK);
    public static final Item SWEET_AND_SOUR_PORK_RICE_BOWL = new BowlFoodOnlyItem(ModFoods.SWEET_AND_SOUR_PORK_RICE_BOWL);
    public static final Item COUNTRY_STYLE_MIXED_VEGETABLES = new BowlFoodOnlyItem(ModFoods.COUNTRY_STYLE_MIXED_VEGETABLES);
    public static final Item FISH_FLAVORED_SHREDDED_PORK = new BowlFoodOnlyItem(ModFoods.FISH_FLAVORED_SHREDDED_PORK);
    public static final Item FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL = new BowlFoodOnlyItem(ModFoods.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL);
    public static final Item BRAISED_FISH_RICE_BOWL = new BowlFoodOnlyItem(ModFoods.BRAISED_FISH_RICE_BOWL);
    public static final Item SPICY_CHICKEN_RICE_BOWL = new BowlFoodOnlyItem(ModFoods.SPICY_CHICKEN_RICE_BOWL);
    public static final Item SUSPICIOUS_STIR_FRY_RICE_BOWL = new BowlFoodOnlyItem(ModFoods.SUSPICIOUS_STIR_FRY_RICE_BOWL);

    // 汤类
    public static final Item PORK_BONE_SOUP = new BowlFoodOnlyItem(ModFoods.PORK_BONE_SOUP);
    public static final Item SEAFOOD_MISO_SOUP = new BowlFoodOnlyItem(ModFoods.SEAFOOD_MISO_SOUP);
    public static final Item FEARSOME_THICK_SOUP = new BowlFoodOnlyItem(ModFoods.FEARSOME_THICK_SOUP);
    public static final Item LAMB_AND_RADISH_SOUP = new BowlFoodOnlyItem(ModFoods.LAMB_AND_RADISH_SOUP);
    public static final Item BRAISED_BEEF_WITH_POTATOES = new BowlFoodOnlyItem(ModFoods.BRAISED_BEEF_WITH_POTATOES);
    public static final Item WILD_MUSHROOM_RABBIT_SOUP = new BowlFoodOnlyItem(ModFoods.WILD_MUSHROOM_RABBIT_SOUP);
    public static final Item TOMATO_BEEF_BRISKET_SOUP = new BowlFoodOnlyItem(ModFoods.TOMATO_BEEF_BRISKET_SOUP);
    public static final Item PUFFERFISH_SOUP = new BowlFoodOnlyItem(ModFoods.PUFFERFISH_SOUP);
    public static final Item BORSCHT = new BowlFoodOnlyItem(ModFoods.BORSCHT);
    public static final Item BEEF_MEATBALL_SOUP = new BowlFoodOnlyItem(ModFoods.BEEF_MEATBALL_SOUP);
    public static final Item CHICKEN_AND_MUSHROOM_STEW = new BowlFoodOnlyItem(ModFoods.CHICKEN_AND_MUSHROOM_STEW);
    public static final Item DONKEY_SOUP = new BowlFoodOnlyItem(ModFoods.DONKEY_SOUP);

    // 面条类
    public static final Item BEEF_NOODLE = new BowlFoodOnlyItem(ModFoods.BEEF_NOODLE);
    public static final Item HUI_NOODLE = new BowlFoodOnlyItem(ModFoods.HUI_NOODLE);
    public static final Item UDON_NOODLE = new BowlFoodOnlyItem(ModFoods.UDON_NOODLE);

    // 生肉类
    public static final Item SASHIMI = new Item(new Item.Properties().food(ModFoods.SASHIMI));
    public static final Item RAW_LAMB_CHOPS = new Item(new Item.Properties().food(ModFoods.RAW_LAMB_CHOPS));
    public static final Item RAW_COW_OFFAL = new Item(new Item.Properties().food(ModFoods.RAW_COW_OFFAL));
    public static final Item RAW_PORK_BELLY = new Item(new Item.Properties().food(ModFoods.RAW_PORK_BELLY));
    public static final Item RAW_DONKEY_MEAT = new Item(new Item.Properties().food(ModFoods.RAW_DONKEY_MEAT));

    // 熟肉类
    public static final Item COOKED_LAMB_CHOPS = new Item(new Item.Properties().food(ModFoods.COOKED_LAMB_CHOPS));
    public static final Item COOKED_COW_OFFAL = new Item(new Item.Properties().food(ModFoods.COOKED_COW_OFFAL));
    public static final Item COOKED_PORK_BELLY = new Item(new Item.Properties().food(ModFoods.COOKED_PORK_BELLY));
    public static final Item COOKED_DONKEY_MEAT = new Item(new Item.Properties().food(ModFoods.COOKED_DONKEY_MEAT));

    public static void registerItems() {
        // 厨具
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stove"), STOVE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pot"), POT);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot"), STOCKPOT);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot_lid"), STOCKPOT_LID);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chopping_board"), CHOPPING_BOARD);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "kitchenware_racks"), KITCHENWARE_RACKS);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "shawarma_spit"), SHAWARMA_SPIT);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "millstone"), MILLSTONE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "steamer"), STEAMER);

        // 油
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "oil"), OIL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "oil_pot"), OIL_POT);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "oil_block"), OIL_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "enamel_basin"), ENAMEL_BASIN);

        // 杂项
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chili_ristra"), CHILI_RISTRA);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "straw_block"), STRAW_BLOCK);
//        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "fruit_basket"), FRUIT_BASKET);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "scarecrow"), SCARECROW);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "recipe_item"), RECIPE_ITEM);
//        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "transmutation_lunch_bag"), TRANSMUTATION_LUNCH_BAG);

        // 工具
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "iron_kitchen_knife"), IRON_KITCHEN_KNIFE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "gold_kitchen_knife"), GOLD_KITCHEN_KNIFE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "diamond_kitchen_knife"), DIAMOND_KITCHEN_KNIFE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "netherite_kitchen_knife"), NETHERITE_KITCHEN_KNIFE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "kitchen_shovel"), KITCHEN_SHOVEL);

        // 服装
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "straw_hat"), STRAW_HAT);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "straw_hat_flower"), STRAW_HAT_FLOWER);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "farmer_chest_plate"), FARMER_CHEST_PLATE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "farmer_leggings"), FARMER_LEGGINGS);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "farmer_boots"), FARMER_BOOTS);

        // 种子
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "tomato_seed"), TOMATO_SEED);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chili_seed"), CHILI_SEED);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "lettuce_seed"), LETTUCE_SEED);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "rice"), RICE_SEED);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "wild_rice"), WILD_RICE_SEED);

        // 凳子
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_oak"), COOK_STOOL_OAK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_spruce"), COOK_STOOL_SPRUCE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_acacia"), COOK_STOOL_ACACIA);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_bamboo"), COOK_STOOL_BAMBOO);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_birch"), COOK_STOOL_BIRCH);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_cherry"), COOK_STOOL_CHERRY);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_crimson"), COOK_STOOL_CRIMSON);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_dark_oak"), COOK_STOOL_DARK_OAK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_jungle"), COOK_STOOL_JUNGLE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_mangrove"), COOK_STOOL_MANGROVE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_warped"), COOK_STOOL_WARPED);

        // 椅子
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_oak"), CHAIR_OAK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_spruce"), CHAIR_SPRUCE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_acacia"), CHAIR_ACACIA);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_bamboo"), CHAIR_BAMBOO);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_birch"), CHAIR_BIRCH);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_cherry"), CHAIR_CHERRY);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_crimson"), CHAIR_CRIMSON);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_dark_oak"), CHAIR_DARK_OAK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_jungle"), CHAIR_JUNGLE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_mangrove"), CHAIR_MANGROVE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_warped"), CHAIR_WARPED);

        // 桌子
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_oak"), TABLE_OAK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_spruce"), TABLE_SPRUCE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_acacia"), TABLE_ACACIA);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_bamboo"), TABLE_BAMBOO);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_birch"), TABLE_BIRCH);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_cherry"), TABLE_CHERRY);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_crimson"), TABLE_CRIMSON);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_dark_oak"), TABLE_DARK_OAK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_jungle"), TABLE_JUNGLE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_mangrove"), TABLE_MANGROVE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_warped"), TABLE_WARPED);

        // 蔬菜食材
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "tomato"), TOMATO);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "red_chili"), RED_CHILI);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "green_chili"), GREEN_CHILI);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "lettuce"), LETTUCE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "rice_panicle"), RICE_PANICLE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "caterpillar"), CATERPILLAR);

        // 面类食材
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "raw_dough"), RAW_DOUGH);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "raw_noodles"), RAW_NOODLES);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stuffed_dough_food"), STUFFED_DOUGH_FOOD);

        // 基础食物
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "fried_egg"), FRIED_EGG);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "donkey_burger"), DONKEY_BURGER);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "baozi"), BAOZI);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "dumpling"), DUMPLING);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "samsa"), SAMSA);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "mantou"), MANTOU);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "meat_pie"), MEAT_PIE);

        // 小碗菜和盖饭
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cooked_rice"), COOKED_RICE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "egg_fried_rice"), EGG_FRIED_RICE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "delicious_egg_fried_rice"), DELICIOUS_EGG_FRIED_RICE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "scramble_egg_with_tomatoes"), SCRAMBLE_EGG_WITH_TOMATOES);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "scramble_egg_with_tomatoes_rice_bowl"), SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stir_fried_beef_offal"), STIR_FRIED_BEEF_OFFAL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stir_fried_beef_offal_rice_bowl"), STIR_FRIED_BEEF_OFFAL_RICE_BOWL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "braised_beef"), BRAISED_BEEF);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "braised_beef_rice_bowl"), BRAISED_BEEF_RICE_BOWL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stir_fried_pork_with_peppers"), STIR_FRIED_PORK_WITH_PEPPERS);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stir_fried_pork_with_peppers_rice_bowl"), STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "sweet_and_sour_pork"), SWEET_AND_SOUR_PORK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "sweet_and_sour_pork_rice_bowl"), SWEET_AND_SOUR_PORK_RICE_BOWL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "country_style_mixed_vegetables"), COUNTRY_STYLE_MIXED_VEGETABLES);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "fish_flavored_shredded_pork"), FISH_FLAVORED_SHREDDED_PORK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "fish_flavored_shredded_pork_rice_bowl"), FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "braised_fish_rice_bowl"), BRAISED_FISH_RICE_BOWL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "spicy_chicken_rice_bowl"), SPICY_CHICKEN_RICE_BOWL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "suspicious_stir_fry_rice_bowl"), SUSPICIOUS_STIR_FRY_RICE_BOWL);

        // 汤类
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pork_bone_soup"), PORK_BONE_SOUP);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "seafood_miso_soup"), SEAFOOD_MISO_SOUP);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "fearsome_thick_soup"), FEARSOME_THICK_SOUP);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "lamb_and_radish_soup"), LAMB_AND_RADISH_SOUP);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "braised_beef_with_potatoes"), BRAISED_BEEF_WITH_POTATOES);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "wild_mushroom_rabbit_soup"), WILD_MUSHROOM_RABBIT_SOUP);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "tomato_beef_brisket_soup"), TOMATO_BEEF_BRISKET_SOUP);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pufferfish_soup"), PUFFERFISH_SOUP);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "borscht"), BORSCHT);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "beef_meatball_soup"), BEEF_MEATBALL_SOUP);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chicken_and_mushroom_stew"), CHICKEN_AND_MUSHROOM_STEW);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "donkey_soup"), DONKEY_SOUP);

        // 面条类
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "beef_noodle"), BEEF_NOODLE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "hui_noodle"), HUI_NOODLE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "udon_noodle"), UDON_NOODLE);

        // 生肉类
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "sashimi"), SASHIMI);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "raw_lamb_chops"), RAW_LAMB_CHOPS);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "raw_cow_offal"), RAW_COW_OFFAL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "raw_pork_belly"), RAW_PORK_BELLY);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "raw_donkey_meat"), RAW_DONKEY_MEAT);

        // 熟肉类
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cooked_lamb_chops"), COOKED_LAMB_CHOPS);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cooked_cow_offal"), COOKED_COW_OFFAL);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cooked_pork_belly"), COOKED_PORK_BELLY);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cooked_donkey_meat"), COOKED_DONKEY_MEAT);
    }
}

