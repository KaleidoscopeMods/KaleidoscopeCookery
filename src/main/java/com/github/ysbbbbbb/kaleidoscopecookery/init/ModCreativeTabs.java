package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeCookery.MOD_ID);

    public static RegistryObject<CreativeModeTab> COOKERY_MAIN_TAB = TABS.register("cookery_main", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.kaleidoscope_cookery.cookery_main.name"))
            .icon(() -> ModItems.IRON_KITCHEN_KNIFE.get().getDefaultInstance())
            .displayItems((par, output) -> {
                // 厨具与杂项
                output.accept(ModItems.STOVE.get());
                output.accept(ModItems.SHAWARMA_SPIT.get());
                output.accept(ModItems.STRAW_BLOCK.get());
                output.accept(ModItems.OIL_BLOCK.get());
                output.accept(ModItems.POT.get());
                output.accept(ModItems.STOCKPOT.get());
                output.accept(ModItems.STOCKPOT_LID.get());
                output.accept(ModItems.CHOPPING_BOARD.get());
                output.accept(ModItems.MILLSTONE.get());
                output.accept(ModItems.STEAMER.get());
                output.accept(ModItems.KITCHENWARE_RACKS.get());
                output.accept(ModItems.FRUIT_BASKET.get());
                output.accept(ModItems.SCARECROW.get());
                output.accept(ModItems.ENAMEL_BASIN.get());
                output.accept(ModItems.OIL_POT.get());
                output.accept(OilPotItem.getFullOilPot());
                output.accept(ModItems.OIL.get());
                output.accept(ModItems.RECIPE_ITEM.get());
                output.accept(ModItems.TRANSMUTATION_LUNCH_BAG.get());
                // 原材料
                output.accept(ModItems.RAW_DOUGH.get());
                output.accept(ModItems.RAW_NOODLES.get());
                output.accept(ModItems.STUFFED_DOUGH_FOOD.get());
                output.accept(ModItems.CHILI_RISTRA.get());
                output.accept(ModItems.RICE_SEED.get());
                output.accept(ModItems.WILD_RICE_SEED.get());
                output.accept(ModItems.TOMATO_SEED.get());
                output.accept(ModItems.CHILI_SEED.get());
                output.accept(ModItems.LETTUCE_SEED.get());
                // 工具
                output.accept(ModItems.KITCHEN_SHOVEL.get());
                output.accept(ModItems.GOLD_KITCHEN_KNIFE.get());
                output.accept(ModItems.IRON_KITCHEN_KNIFE.get());
                output.accept(ModItems.DIAMOND_KITCHEN_KNIFE.get());
                output.accept(ModItems.NETHERITE_KITCHEN_KNIFE.get());
                // 服装
                output.accept(ModItems.STRAW_HAT.get());
                output.accept(ModItems.STRAW_HAT_FLOWER.get());
                output.accept(ModItems.FARMER_CHEST_PLATE.get());
                output.accept(ModItems.FARMER_LEGGINGS.get());
                output.accept(ModItems.FARMER_BOOTS.get());
                // 凳子
                output.accept(ModItems.COOK_STOOL_OAK.get());
                output.accept(ModItems.COOK_STOOL_SPRUCE.get());
                output.accept(ModItems.COOK_STOOL_ACACIA.get());
                output.accept(ModItems.COOK_STOOL_BAMBOO.get());
                output.accept(ModItems.COOK_STOOL_BIRCH.get());
                output.accept(ModItems.COOK_STOOL_CHERRY.get());
                output.accept(ModItems.COOK_STOOL_CRIMSON.get());
                output.accept(ModItems.COOK_STOOL_DARK_OAK.get());
                output.accept(ModItems.COOK_STOOL_JUNGLE.get());
                output.accept(ModItems.COOK_STOOL_MANGROVE.get());
                output.accept(ModItems.COOK_STOOL_WARPED.get());
                // 椅子
                output.accept(ModItems.CHAIR_OAK.get());
                output.accept(ModItems.CHAIR_SPRUCE.get());
                output.accept(ModItems.CHAIR_ACACIA.get());
                output.accept(ModItems.CHAIR_BAMBOO.get());
                output.accept(ModItems.CHAIR_BIRCH.get());
                output.accept(ModItems.CHAIR_CHERRY.get());
                output.accept(ModItems.CHAIR_CRIMSON.get());
                output.accept(ModItems.CHAIR_DARK_OAK.get());
                output.accept(ModItems.CHAIR_JUNGLE.get());
                output.accept(ModItems.CHAIR_MANGROVE.get());
                output.accept(ModItems.CHAIR_WARPED.get());
                // 桌子
                output.accept(ModItems.TABLE_OAK.get());
                output.accept(ModItems.TABLE_SPRUCE.get());
                output.accept(ModItems.TABLE_ACACIA.get());
                output.accept(ModItems.TABLE_BAMBOO.get());
                output.accept(ModItems.TABLE_BIRCH.get());
                output.accept(ModItems.TABLE_CHERRY.get());
                output.accept(ModItems.TABLE_CRIMSON.get());
                output.accept(ModItems.TABLE_DARK_OAK.get());
                output.accept(ModItems.TABLE_JUNGLE.get());
                output.accept(ModItems.TABLE_MANGROVE.get());
                output.accept(ModItems.TABLE_WARPED.get());
            }).build());

    public static RegistryObject<CreativeModeTab> COOKERY_FOOD_TAB = TABS.register("cookery_food", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.kaleidoscope_cookery.cookery_food.name"))
            .icon(() -> ModItems.RED_CHILI.get().getDefaultInstance())
            .withTabsBefore(COOKERY_MAIN_TAB.getId())
            .displayItems((par, output) -> {
                // 原材料
                output.accept(ModItems.TOMATO.get());
                output.accept(ModItems.RED_CHILI.get());
                output.accept(ModItems.GREEN_CHILI.get());
                output.accept(ModItems.LETTUCE.get());
                output.accept(ModItems.RICE_PANICLE.get());
                output.accept(ModItems.CATERPILLAR.get());
                // 肉类
                output.accept(ModItems.SASHIMI.get());
                output.accept(ModItems.RAW_LAMB_CHOPS.get());
                output.accept(ModItems.COOKED_LAMB_CHOPS.get());
                output.accept(ModItems.RAW_COW_OFFAL.get());
                output.accept(ModItems.COOKED_COW_OFFAL.get());
                output.accept(ModItems.RAW_PORK_BELLY.get());
                output.accept(ModItems.COOKED_PORK_BELLY.get());
                output.accept(ModItems.RAW_DONKEY_MEAT.get());
                output.accept(ModItems.COOKED_DONKEY_MEAT.get());
                // 基础食物
                output.accept(ModItems.DONKEY_BURGER.get());
                output.accept(ModItems.MANTOU.get());
                output.accept(ModItems.BAOZI.get());
                output.accept(ModItems.SAMSA.get());
                output.accept(ModItems.MEAT_PIE.get());
                output.accept(ModItems.DUMPLING.get());
                output.accept(ModItems.FRIED_EGG.get());
                // 小碗菜和盖饭
                output.accept(ModItems.COOKED_RICE.get());
                output.accept(ModItems.EGG_FRIED_RICE.get());
                output.accept(ModItems.DELICIOUS_EGG_FRIED_RICE.get());
                output.accept(ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get());
                output.accept(ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL.get());
                output.accept(ModItems.STIR_FRIED_BEEF_OFFAL.get());
                output.accept(ModItems.STIR_FRIED_BEEF_OFFAL_RICE_BOWL.get());
                output.accept(ModItems.BRAISED_BEEF.get());
                output.accept(ModItems.BRAISED_BEEF_RICE_BOWL.get());
                output.accept(ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get());
                output.accept(ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL.get());
                output.accept(ModItems.SWEET_AND_SOUR_PORK.get());
                output.accept(ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL.get());
                output.accept(ModItems.COUNTRY_STYLE_MIXED_VEGETABLES.get());
                output.accept(ModItems.FISH_FLAVORED_SHREDDED_PORK.get());
                output.accept(ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL.get());
                output.accept(ModItems.BRAISED_FISH_RICE_BOWL.get());
                output.accept(ModItems.SPICY_CHICKEN_RICE_BOWL.get());
                output.accept(ModItems.SUSPICIOUS_STIR_FRY_RICE_BOWL.get());
                // 汤类
                output.accept(ModItems.PORK_BONE_SOUP.get());
                output.accept(ModItems.SEAFOOD_MISO_SOUP.get());
                output.accept(ModItems.FEARSOME_THICK_SOUP.get());
                output.accept(ModItems.LAMB_AND_RADISH_SOUP.get());
                output.accept(ModItems.BRAISED_BEEF_WITH_POTATOES.get());
                output.accept(ModItems.WILD_MUSHROOM_RABBIT_SOUP.get());
                output.accept(ModItems.TOMATO_BEEF_BRISKET_SOUP.get());
                output.accept(ModItems.PUFFERFISH_SOUP.get());
                output.accept(ModItems.BORSCHT.get());
                output.accept(ModItems.BEEF_MEATBALL_SOUP.get());
                output.accept(ModItems.CHICKEN_AND_MUSHROOM_STEW.get());
                output.accept(ModItems.DONKEY_SOUP.get());
                // 面条
                output.accept(ModItems.BEEF_NOODLE.get());
                output.accept(ModItems.HUI_NOODLE.get());
                output.accept(ModItems.UDON_NOODLE.get());
                // 硬菜
                FoodBiteRegistry.FOOD_DATA_MAP.keySet().forEach(foodName -> {
                    var foodItem = ForgeRegistries.ITEMS.getValue(foodName);
                    if (foodItem != null) {
                        output.accept(foodItem);
                    }
                });
            }).build());
}
