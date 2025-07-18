package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeCookery.MOD_ID);

    public static Supplier<CreativeModeTab> COOKERY_MAIN_TAB = TABS.register("cookery_main", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.kaleidoscope_cookery.cookery_main.name"))
            .icon(() -> ModItems.IRON_KITCHEN_KNIFE.get().getDefaultInstance())
            .displayItems((par, output) -> {
                output.accept(ModItems.STOVE.get());
                output.accept(ModItems.SHAWARMA_SPIT.get());
                output.accept(ModItems.STRAW_BLOCK.get());
                output.accept(ModItems.OIL_BLOCK.get());
                output.accept(ModItems.POT.get());
                output.accept(ModItems.STOCKPOT.get());
                output.accept(ModItems.STOCKPOT_LID.get());
                output.accept(ModItems.CHOPPING_BOARD.get());
                output.accept(ModItems.KITCHENWARE_RACKS.get());
                output.accept(ModItems.FRUIT_BASKET.get());
                output.accept(ModItems.SCARECROW.get());
                output.accept(ModItems.ENAMEL_BASIN.get());
                output.accept(ModItems.OIL.get());
                output.accept(ModItems.CHILI_RISTRA.get());
                output.accept(ModItems.RICE_SEED.get());
                output.accept(ModItems.WILD_RICE_SEED.get());
                output.accept(ModItems.TOMATO_SEED.get());
                output.accept(ModItems.CHILI_SEED.get());
                output.accept(ModItems.LETTUCE_SEED.get());
                output.accept(ModItems.KITCHEN_SHOVEL.get());
                output.accept(ModItems.GOLD_KITCHEN_KNIFE.get());
                output.accept(ModItems.IRON_KITCHEN_KNIFE.get());
                output.accept(ModItems.DIAMOND_KITCHEN_KNIFE.get());
                output.accept(ModItems.NETHERITE_KITCHEN_KNIFE.get());
                output.accept(ModItems.STRAW_HAT.get());
                output.accept(ModItems.STRAW_HAT_FLOWER.get());
                output.accept(ModItems.FARMER_CHEST_PLATE.get());
                output.accept(ModItems.FARMER_LEGGINGS.get());
                output.accept(ModItems.FARMER_BOOTS.get());
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

    public static Supplier<CreativeModeTab> COOKERY_FOOD_TAB = TABS.register("cookery_food", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.kaleidoscope_cookery.cookery_food.name"))
            .icon(() -> ModItems.RED_CHILI.get().getDefaultInstance())
            .withTabsBefore(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "cookery_main"))
            .displayItems((par, output) -> {
                output.accept(ModItems.TOMATO.get());
                output.accept(ModItems.RED_CHILI.get());
                output.accept(ModItems.GREEN_CHILI.get());
                output.accept(ModItems.LETTUCE.get());
                output.accept(ModItems.RICE_PANICLE.get());
                output.accept(ModItems.CATERPILLAR.get());
                output.accept(ModItems.SASHIMI.get());
                output.accept(ModItems.RAW_LAMB_CHOPS.get());
                output.accept(ModItems.COOKED_LAMB_CHOPS.get());
                output.accept(ModItems.RAW_COW_OFFAL.get());
                output.accept(ModItems.COOKED_COW_OFFAL.get());
                output.accept(ModItems.RAW_PORK_BELLY.get());
                output.accept(ModItems.COOKED_PORK_BELLY.get());
                output.accept(ModItems.FRIED_EGG.get());
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

                FoodBiteRegistry.FOOD_DATA_MAP.keySet().forEach(foodName ->
                        output.accept(BuiltInRegistries.ITEM.get(foodName)));
            }).build());
}
