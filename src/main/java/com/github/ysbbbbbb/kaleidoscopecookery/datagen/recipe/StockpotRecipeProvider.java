package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.StockpotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoupBases;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.google.common.collect.Lists;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Consumer;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems.STUFFED_DOUGH_FOOD;

public class StockpotRecipeProvider extends ModRecipeProvider {
    public StockpotRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.SEEDS_RICE, TagCommon.SEEDS_RICE, TagCommon.SEEDS_RICE)
                .setFinishedTexture(modLoc("stockpot/rice_finished"))
                .setResult(ModItems.COOKED_RICE, 3)
                .setFinishedBubbleColor(0xE9E3DB).setTime(300)
                .save(consumer, "rice_3");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.SEEDS_RICE, TagCommon.SEEDS_RICE,
                        TagCommon.SEEDS_RICE, TagCommon.SEEDS_RICE)
                .setFinishedTexture(modLoc("stockpot/rice_finished"))
                .setResult(ModItems.COOKED_RICE, 4)
                .setFinishedBubbleColor(0xE9E3DB).setTime(400)
                .save(consumer, "rice_4");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.SEEDS_RICE, TagCommon.SEEDS_RICE, TagCommon.SEEDS_RICE,
                        TagCommon.SEEDS_RICE, TagCommon.SEEDS_RICE)
                .setFinishedTexture(modLoc("stockpot/rice_finished"))
                .setResult(ModItems.COOKED_RICE, 5)
                .setFinishedBubbleColor(0xE9E3DB).setTime(500)
                .save(consumer, "rice_5");

        StockpotRecipeBuilder.builder()
                .addInput(Items.BONE, Items.BONE, Items.BONE, Items.BONE, Items.BONE, Items.BONE, Items.BONE)
                .setResult(ModItems.PORK_BONE_SOUP)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES_SALMON, TagCommon.RAW_FISHES_SALMON, TagCommon.RAW_FISHES_SALMON)
                .addInput(Items.KELP, Items.BONE_MEAL)
                .setResult(ModItems.SEAFOOD_MISO_SOUP)
                .save(consumer, "seafood_miso_soup_salmon");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD)
                .addInput(Items.KELP, Items.BONE_MEAL)
                .setResult(ModItems.SEAFOOD_MISO_SOUP)
                .save(consumer, "seafood_miso_soup_cod");

        StockpotRecipeBuilder.builder()
                .addInput(Items.KELP, Items.BONE_MEAL)
                .setSoupBase(ModSoupBases.SALMON_BUCKET)
                .setResult(ModItems.SEAFOOD_MISO_SOUP, 1)
                .save(consumer, "seafood_miso_soup_salmon_entity");

        StockpotRecipeBuilder.builder()
                .addInput(Items.KELP, Items.BONE_MEAL)
                .setSoupBase(ModSoupBases.COD_BUCKET)
                .setResult(ModItems.SEAFOOD_MISO_SOUP, 1)
                .save(consumer, "seafood_miso_soup_cod_entity");

        StockpotRecipeBuilder.builder()
                .addInput(Items.ROTTEN_FLESH, Items.ROTTEN_FLESH,
                        Items.ROTTEN_FLESH, Items.ROTTEN_FLESH)
                .addInput(Items.SCULK, Items.SCULK, Items.SCULK)
                .setSoupBase(ModSoupBases.LAVA)
                .addInput(Blocks.SCULK, Blocks.SCULK)
                .setResult(ModItems.FEARSOME_THICK_SOUP)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(Items.CARROT, Items.CARROT, Items.CARROT, Items.CARROT)
                .addInput(TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON)
                .setResult(ModItems.LAMB_AND_RADISH_SOUP)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(Items.POTATO, Items.POTATO, Items.POTATO, Items.POTATO)
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF)
                .setResult(ModItems.BRAISED_BEEF_WITH_POTATOES)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(Items.RABBIT, Items.RABBIT)
                .addInput(TagCommon.MUSHROOMS, TagCommon.MUSHROOMS, TagCommon.MUSHROOMS)
                .setResult(ModItems.WILD_MUSHROOM_RABBIT_SOUP)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF,
                        TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO,
                        TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO)
                .setResult(ModItems.TOMATO_BEEF_BRISKET_SOUP)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(Items.PUFFERFISH, Items.PUFFERFISH, Items.PUFFERFISH,
                        Items.SEAGRASS, Items.SEAGRASS)
                .setResult(ModItems.PUFFERFISH_SOUP)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(Items.SEAGRASS, Items.SEAGRASS)
                .setSoupBase(ModSoupBases.PUFFERFISH_BUCKET)
                .setResult(ModItems.PUFFERFISH_SOUP, 1)
                .save(consumer, "pufferfish_soup_entity");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.CROPS_TOMATO,
                        TagCommon.CROPS_TOMATO, TagCommon.CROPS_LETTUCE)
                .setResult(ModItems.BORSCHT)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.CROPS_TOMATO)
                .addInput(Items.BEETROOT, Items.BEETROOT)
                .setResult(ModItems.BORSCHT)
                .save(consumer, "borscht_beetroot");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF,
                        TagCommon.RAW_BEEF, TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setResult(ModItems.BEEF_MEATBALL_SOUP)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN,
                        TagCommon.MUSHROOMS, TagCommon.MUSHROOMS, TagCommon.MUSHROOMS)
                .setResult(ModItems.CHICKEN_AND_MUSHROOM_STEW)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF)
                .addInput(ModItems.RAW_NOODLES, ModItems.RAW_NOODLES, ModItems.RAW_NOODLES)
                .setResult(ModItems.BEEF_NOODLE)
                .save(consumer);

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, ModItems.FRIED_EGG)
                .addInput(Items.KELP, ModItems.RAW_NOODLES, ModItems.RAW_NOODLES, ModItems.RAW_NOODLES)
                .setResult(ModItems.HUI_NOODLE)
                .save(consumer, "hui_noodle_eggs");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD)
                .addInput(TagCommon.EGGS, TagCommon.VEGETABLES, TagCommon.VEGETABLES)
                .addInput(ModItems.RAW_NOODLES, ModItems.RAW_NOODLES, ModItems.RAW_NOODLES)
                .setResult(ModItems.UDON_NOODLE)
                .save(consumer, "udon_noodle_eggs");

        StockpotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_MEATS, TagCommon.RAW_MEATS)
                .addInput(TagCommon.COOKED_EGGS, TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE, ModItems.RAW_NOODLES)
                .setResult(ModItems.UDON_NOODLE)
                .save(consumer, "udon_noodle_cooked_eggs");

        StockpotRecipeBuilder.builder()
                .addInput(ModItems.RAW_DONKEY_MEAT, ModItems.RAW_DONKEY_MEAT, ModItems.RAW_DONKEY_MEAT,
                        ModItems.RAW_DONKEY_MEAT, ModItems.RAW_DONKEY_MEAT)
                .setResult(ModItems.DONKEY_SOUP)
                .save(consumer);

        {
            List<Item> inputs = Lists.newArrayList();
            for (int i = 0; i < 9; i++) {
                int count = i + 1;
                inputs.add(STUFFED_DOUGH_FOOD);
                StockpotRecipeBuilder.builder()
                        .addInput(inputs.toArray())
                        .setResult(ModItems.DUMPLING, count)
                        .save(consumer, "dumpling_count_" + count);
            }
        }

        StockpotRecipeBuilder.builder()
                .addInput(STUFFED_DOUGH_FOOD, STUFFED_DOUGH_FOOD, STUFFED_DOUGH_FOOD, STUFFED_DOUGH_FOOD)
                .setSoupBase(ModSoupBases.LAVA)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.SHENGJIAN_MANTOU), 1)
                .save(consumer, "shengjian_mantou_count_1");

        StockpotRecipeBuilder.builder()
                .addInput(STUFFED_DOUGH_FOOD, STUFFED_DOUGH_FOOD, STUFFED_DOUGH_FOOD, STUFFED_DOUGH_FOOD,
                        STUFFED_DOUGH_FOOD, STUFFED_DOUGH_FOOD, STUFFED_DOUGH_FOOD, STUFFED_DOUGH_FOOD)
                .setSoupBase(ModSoupBases.LAVA)
                .setResult(FoodBiteRegistry.getItem(FoodBiteRegistry.SHENGJIAN_MANTOU), 2)
                .save(consumer, "shengjian_mantou_count_2");
    }
}
