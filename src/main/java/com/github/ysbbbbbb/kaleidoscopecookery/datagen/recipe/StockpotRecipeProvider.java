package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.FlexStockpotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.StockpotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoupBases;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems.STUFFED_DOUGH_FOOD;

public class StockpotRecipeProvider extends ModRecipeProvider {
    public StockpotRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        addRiceRecipes(consumer);
        addDumplingRecipes(consumer);
        addShengjianMantouRecipes(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(Items.BONE, Items.BONE, Items.BONE, Items.BONE, Items.BONE, Items.BONE, Items.BONE)
                .setResult(ModItems.PORK_BONE_SOUP.get(), 1)
                .save(consumer, "pork_bone_soup");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES, TagCommon.RAW_FISHES,
                        TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setResult(ModItems.SEAFOOD_MISO_SOUP.get(), 1)
                .save(consumer, "seafood_miso_soup");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setSoupBase(ModSoupBases.COD_BUCKET)
                .setResult(ModItems.SEAFOOD_MISO_SOUP.get(), 1)
                .save(consumer, "seafood_miso_soup_cod_entity");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setSoupBase(ModSoupBases.SALMON_BUCKET)
                .setResult(ModItems.SEAFOOD_MISO_SOUP.get(), 1)
                .save(consumer, "seafood_miso_soup_salmon_entity");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setSoupBase(ModSoupBases.TROPICAL_FISH_BUCKET)
                .setResult(ModItems.SEAFOOD_MISO_SOUP.get(), 1)
                .save(consumer, "seafood_miso_soup_tropical_entity");

        StockpotRecipeBuilder.builder()
                .addInput(Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH,
                        Items.SCULK, Items.SCULK, Items.SCULK, Items.SCULK, Items.SCULK)
                .setSoupBase(ModSoupBases.LAVA)
                .setResult(ModItems.FEARSOME_THICK_SOUP.get(), 1)
                .save(consumer, "fearsome_thick_soup");

        StockpotRecipeBuilder.builder()
                .addInput(Items.CARROT, Items.CARROT, Items.CARROT, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON)
                .setResult(ModItems.LAMB_AND_RADISH_SOUP.get(), 1)
                .save(consumer, "lamb_and_radish_soup");

        StockpotRecipeBuilder.builder()
                .addInput(Items.POTATO, Items.POTATO, Items.POTATO, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF)
                .setResult(ModItems.BRAISED_BEEF_WITH_POTATOES.get(), 1)
                .save(consumer, "braised_beef_with_potatoes");

        StockpotRecipeBuilder.builder()
                .addInput(Tags.Items.MUSHROOMS, Tags.Items.MUSHROOMS, Tags.Items.MUSHROOMS, Items.RABBIT, Items.RABBIT)
                .setResult(ModItems.WILD_MUSHROOM_RABBIT_SOUP.get(), 1)
                .save(consumer, "wild_mushroom_rabbit_soup");

        StockpotRecipeBuilder.builder()
                .addInput(Items.PUFFERFISH, Items.PUFFERFISH,
                        TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setResult(ModItems.PUFFERFISH_SOUP.get(), 1)
                .save(consumer, "pufferfish_soup");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setSoupBase(ModSoupBases.PUFFERFISH_BUCKET)
                .setResult(ModItems.PUFFERFISH_SOUP.get(), 1)
                .save(consumer, "pufferfish_soup_entity");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF,
                        TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO)
                .setResult(ModItems.BORSCHT.get(), 1)
                .save(consumer, "borscht");

        StockpotRecipeBuilder.builder()
                .addInput(ModItems.RAW_MEATBALL, ModItems.RAW_MEATBALL,
                        TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setResult(ModItems.BEEF_MEATBALL_SOUP.get(), 1)
                .save(consumer, "beef_meatball_soup");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN,
                        Tags.Items.MUSHROOMS, Tags.Items.MUSHROOMS, Tags.Items.MUSHROOMS)
                .setResult(ModItems.CHICKEN_AND_MUSHROOM_STEW.get(), 1)
                .save(consumer, "chicken_and_mushroom_stew");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF,
                        ModItems.RAW_NOODLES, ModItems.RAW_NOODLES, ModItems.RAW_NOODLES)
                .setResult(ModItems.BEEF_NOODLE.get(), 1)
                .save(consumer, "beef_noodle");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON,
                        ModItems.RAW_NOODLES, ModItems.RAW_NOODLES, ModItems.RAW_NOODLES)
                .setResult(ModItems.HUI_NOODLE.get(), 1)
                .save(consumer, "hui_noodle");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE,
                        TagCommon.EGGS, TagCommon.EGGS,
                        ModItems.RAW_NOODLES, ModItems.RAW_NOODLES, ModItems.RAW_NOODLES)
                .setResult(ModItems.UDON_NOODLE.get(), 1)
                .save(consumer, "udon_noodle");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE,
                        TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE,
                        TagCommon.FLOUR, TagCommon.FLOUR)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.DOUGH_DROP_SOUP), 1)
                .save(consumer, "dough_drop_soup");

        StockpotRecipeBuilder.builder()
                .addInput(ModItems.RAW_MEATBALL, ModItems.RAW_MEATBALL,
                        ModItems.RAW_MEATBALL, ModItems.RAW_MEATBALL)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.FOUR_JOY_MEATBALL_SOUP), 1)
                .save(consumer, "four_joy_meatball_soup");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN)
                .setSoupBase(ModSoupBases.LAVA)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.NUMBING_SPICY_CHICKEN), 1)
                .save(consumer, "numbing_spicy_chicken");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        Items.BLAZE_POWDER)
                .setSoupBase(ModSoupBases.LAVA)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.SPICY_BLOOD_STEW), 1)
                .save(consumer, "spicy_blood_stew");

        StockpotRecipeBuilder.builder()
                .addInput(Items.BROWN_MUSHROOM, Items.BROWN_MUSHROOM, Items.BROWN_MUSHROOM,
                        Items.BROWN_MUSHROOM, Items.BROWN_MUSHROOM, Items.BROWN_MUSHROOM)
                .setCarrier(Items.FLOWER_POT)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.BROWN_MUSHROOM_POT_SOUP), 1)
                .save(consumer, "brown_mushroom_pot_soup");

        StockpotRecipeBuilder.builder()
                .addInput(Items.RED_MUSHROOM, Items.RED_MUSHROOM, Items.RED_MUSHROOM,
                        Items.RED_MUSHROOM, Items.RED_MUSHROOM, Items.RED_MUSHROOM)
                .setCarrier(Items.FLOWER_POT)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.RED_MUSHROOM_POT_SOUP), 1)
                .save(consumer, "red_mushroom_pot_soup");

        StockpotRecipeBuilder.builder()
                .addInput(Items.WARPED_FUNGUS, Items.WARPED_FUNGUS, Items.WARPED_FUNGUS,
                        Items.WARPED_FUNGUS, Items.WARPED_FUNGUS, Items.WARPED_FUNGUS)
                .setCarrier(Items.FLOWER_POT)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.WARPED_FUNGUS_POT_SOUP), 1)
                .save(consumer, "warped_fungus_pot_soup");

        StockpotRecipeBuilder.builder()
                .addInput(Items.CRIMSON_FUNGUS, Items.CRIMSON_FUNGUS, Items.CRIMSON_FUNGUS,
                        Items.CRIMSON_FUNGUS, Items.CRIMSON_FUNGUS, Items.CRIMSON_FUNGUS)
                .setCarrier(Items.FLOWER_POT)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.CRIMSON_FUNGUS_POT_SOUP), 1)
                .save(consumer, "crimson_fungus_pot_soup");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, TagCommon.EGGS,
                        Items.PHANTOM_MEMBRANE, Items.PHANTOM_MEMBRANE,
                        TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE,
                        TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setCarrier(Items.FLOWER_POT)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.BUDDHA_JUMPS_OVER_THE_WALL), 1)
                .save(consumer, "buddha_jumps_over_the_wall");

        // 模糊配方
        FlexStockpotRecipeBuilder.builder()
                .addInput(Items.BONE)
                .setResult(ModItems.PORK_BONE_SOUP.get(), 1)
                .save(consumer, "pork_bone_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES, TagCommon.CROPS_LETTUCE)
                .setResult(ModItems.SEAFOOD_MISO_SOUP.get(), 1)
                .save(consumer, "seafood_miso_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE)
                .setSoupBase(ModSoupBases.COD_BUCKET)
                .setResult(ModItems.SEAFOOD_MISO_SOUP.get(), 1)
                .save(consumer, "seafood_miso_soup_cod_entity");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE)
                .setSoupBase(ModSoupBases.SALMON_BUCKET)
                .setResult(ModItems.SEAFOOD_MISO_SOUP.get(), 1)
                .save(consumer, "seafood_miso_soup_salmon_entity");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE)
                .setSoupBase(ModSoupBases.TROPICAL_FISH_BUCKET)
                .setResult(ModItems.SEAFOOD_MISO_SOUP.get(), 1)
                .save(consumer, "seafood_miso_soup_tropical_entity");

        FlexStockpotRecipeBuilder.builder()
                .addInput(Items.ROTTEN_FLESH, Items.SCULK)
                .setSoupBase(ModSoupBases.LAVA)
                .setResult(ModItems.FEARSOME_THICK_SOUP.get(), 1)
                .save(consumer, "fearsome_thick_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_MUTTON, Items.CARROT)
                .setResult(ModItems.LAMB_AND_RADISH_SOUP.get(), 1)
                .save(consumer, "lamb_and_radish_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, Items.POTATO)
                .setResult(ModItems.BRAISED_BEEF_WITH_POTATOES.get(), 1)
                .save(consumer, "braised_beef_with_potatoes");

        FlexStockpotRecipeBuilder.builder()
                .addInput(Items.RABBIT, Tags.Items.MUSHROOMS)
                .setResult(ModItems.WILD_MUSHROOM_RABBIT_SOUP.get(), 1)
                .save(consumer, "wild_mushroom_rabbit_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(Items.PUFFERFISH, TagCommon.CROPS_LETTUCE)
                .setResult(ModItems.PUFFERFISH_SOUP.get(), 1)
                .save(consumer, "pufferfish_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE)
                .setSoupBase(ModSoupBases.PUFFERFISH_BUCKET)
                .setResult(ModItems.PUFFERFISH_SOUP.get(), 1)
                .save(consumer, "pufferfish_soup_entity");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.CROPS_TOMATO)
                .setResult(ModItems.BORSCHT.get(), 1)
                .save(consumer, "borscht");

        FlexStockpotRecipeBuilder.builder()
                .addInput(ModItems.RAW_MEATBALL, TagCommon.CROPS_LETTUCE)
                .setResult(ModItems.BEEF_MEATBALL_SOUP.get(), 1)
                .save(consumer, "beef_meatball_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_CHICKEN, Tags.Items.MUSHROOMS)
                .setResult(ModItems.CHICKEN_AND_MUSHROOM_STEW.get(), 1)
                .save(consumer, "chicken_and_mushroom_stew");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, ModItems.RAW_NOODLES)
                .setResult(ModItems.BEEF_NOODLE.get(), 1)
                .save(consumer, "beef_noodle");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_MUTTON, ModItems.RAW_NOODLES)
                .setResult(ModItems.HUI_NOODLE.get(), 1)
                .save(consumer, "hui_noodle");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE, TagCommon.EGGS, ModItems.RAW_NOODLES)
                .setResult(ModItems.UDON_NOODLE.get(), 1)
                .save(consumer, "udon_noodle");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE, TagCommon.FLOUR)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.DOUGH_DROP_SOUP), 1)
                .save(consumer, "dough_drop_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(ModItems.RAW_MEATBALL)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.FOUR_JOY_MEATBALL_SOUP), 1)
                .save(consumer, "four_joy_meatball_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.RAW_CHICKEN)
                .setSoupBase(ModSoupBases.LAVA)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.NUMBING_SPICY_CHICKEN), 1)
                .save(consumer, "numbing_spicy_chicken");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_PORK, TagCommon.CROPS_CHILI_PEPPER, Items.BLAZE_POWDER)
                .setSoupBase(ModSoupBases.LAVA)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.SPICY_BLOOD_STEW), 1)
                .save(consumer, "spicy_blood_stew");

        FlexStockpotRecipeBuilder.builder()
                .addInput(Items.BROWN_MUSHROOM)
                .setCarrier(Items.FLOWER_POT)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.BROWN_MUSHROOM_POT_SOUP), 1)
                .save(consumer, "brown_mushroom_pot_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(Items.RED_MUSHROOM)
                .setCarrier(Items.FLOWER_POT)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.RED_MUSHROOM_POT_SOUP), 1)
                .save(consumer, "red_mushroom_pot_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(Items.WARPED_FUNGUS)
                .setCarrier(Items.FLOWER_POT)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.WARPED_FUNGUS_POT_SOUP), 1)
                .save(consumer, "warped_fungus_pot_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(Items.CRIMSON_FUNGUS)
                .setCarrier(Items.FLOWER_POT)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.CRIMSON_FUNGUS_POT_SOUP), 1)
                .save(consumer, "crimson_fungus_pot_soup");

        FlexStockpotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, Items.PHANTOM_MEMBRANE, TagCommon.CROPS_LETTUCE)
                .setCarrier(Items.FLOWER_POT)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.BUDDHA_JUMPS_OVER_THE_WALL), 1)
                .save(consumer, "buddha_jumps_over_the_wall");
    }

    private void addRiceRecipes(Consumer<FinishedRecipe> consumer) {
        for (int count = 1; count <= 9; count++) {
            StockpotRecipeBuilder.builder()
                    .addInput((Object[]) this.getItemsWithCount(TagCommon.GRAIN_RICE, count))
                    .setFinishedTexture(modLoc("stockpot/rice_finished"))
                    .setResult(ModItems.COOKED_RICE.get(), count)
                    .setFinishedBubbleColor(0xE9E3DB)
                    .setTime(count * 100)
                    .save(consumer, "rice_" + count);
        }
    }

    private void addDumplingRecipes(Consumer<FinishedRecipe> consumer) {
        for (int count = 1; count <= 9; count++) {
            StockpotRecipeBuilder.builder()
                    .addInput((Object[]) this.getItemsWithCount(STUFFED_DOUGH_FOOD.get(), count))
                    .setResult(ModItems.DUMPLING.get(), count)
                    .save(consumer, "dumpling_count_" + count);
        }
    }

    private void addShengjianMantouRecipes(Consumer<FinishedRecipe> consumer) {
        for (int count = 1; count <= 9; count++) {
            StockpotRecipeBuilder.builder()
                    .addInput((Object[]) this.getItemsWithCount(STUFFED_DOUGH_FOOD.get(), count))
                    .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.SHENGJIAN_MANTOU), count)
                    .setSoupBase(ModSoupBases.LAVA)
                    .save(consumer, "shengjian_mantou_count_" + count);
        }
    }
}
