package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.FlexPotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class FoodBiteRecipeProvider extends ModRecipeProvider {
    public FoodBiteRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        PotRecipeBuilder.builder()
                .addInput(Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR,
                        Items.PUMPKIN_PIE, Items.PUMPKIN_PIE, Items.PUMPKIN_PIE, Items.PUMPKIN_PIE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FONDANT_PIE)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.BAMBOO, Items.BAMBOO,
                        TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK,
                        TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.DONGPO_PORK)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR,
                        Items.SPIDER_EYE, Items.SPIDER_EYE, Items.SPIDER_EYE, Items.SPIDER_EYE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FONDANT_SPIDER_EYE)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.CHORUS_FRUIT, Items.CHORUS_FRUIT, Items.CHORUS_FRUIT,
                        TagCommon.EGGS, TagCommon.EGGS, TagCommon.EGGS)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.CHORUS_FRIED_EGG)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN,
                        TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SPICY_CHICKEN)
                .save(consumer);

        PotRecipeBuilder.builder().addInput(Items.BONE, Items.BONE, Items.BONE,
                        Items.SWEET_BERRIES, Items.SWEET_BERRIES, Items.SWEET_BERRIES,
                        TagCommon.RAW_BEEF, TagCommon.RAW_BEEF)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.PAN_SEARED_KNIGHT_STEAK)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.PUMPKIN_PIE,
                        TagCommon.RAW_FISHES, TagCommon.RAW_FISHES, TagCommon.RAW_FISHES,
                        TagCommon.RAW_FISHES, TagCommon.RAW_FISHES)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.STARGAZY_PIE)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_EYE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SWEET_AND_SOUR_ENDER_PEARLS)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.AMETHYST_SHARD, Items.AMETHYST_SHARD, Items.AMETHYST_SHARD,
                        TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.CRYSTAL_LAMB_CHOP)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.BLAZE_POWDER, Items.BLAZE_POWDER, Items.BLAZE_POWDER,
                        TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.BLAZE_LAMB_CHOP)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.BLUE_ICE, Items.BLUE_ICE, Items.BLUE_ICE,
                        TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FROST_LAMB_CHOP)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.POTATO, Items.POTATO, Items.POTATO, Items.POTATO,
                        Items.SUGAR, Items.SUGAR)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.CANDIED_POTATO)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(ModItems.GREEN_CHILI.get(), ModItems.GREEN_CHILI.get(),
                        ModItems.GREEN_CHILI.get(), ModItems.GREEN_CHILI.get(),
                        ModItems.RAW_CUT_SMALL_MEATS.get(), ModItems.RAW_CUT_SMALL_MEATS.get(),
                        ModItems.RAW_CUT_SMALL_MEATS.get(), ModItems.RAW_CUT_SMALL_MEATS.get())
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.STUFFED_TIGER_SKIN_PEPPER)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.RABBIT, Items.RABBIT, Items.RABBIT,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SPICY_RABBIT_HEAD)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagMod.CATERPILLARS, TagMod.CATERPILLARS, TagMod.CATERPILLARS)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FRIED_CATERPILLAR)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.PHANTOM_MEMBRANE, Items.PHANTOM_MEMBRANE,
                        TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK,
                        Tags.Items.MUSHROOMS, Tags.Items.MUSHROOMS)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FRIED_SPRING_ROLL)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES, TagCommon.RAW_FISHES,
                        TagCommon.RAW_FISHES, TagCommon.RAW_FISHES,
                        TagCommon.RAW_FISHES, TagCommon.RAW_FISHES,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.OIL_SPLASHED_FISH)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.BONE, Items.BONE, Items.BONE, Items.BONE,
                        TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.BRAISED_PORK_RIBS)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.SLIME_BALL, Items.SLIME_BALL, Items.SLIME_BALL,
                        Items.SLIME_BALL, Items.SLIME_BALL, Items.SLIME_BALL)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SLIME_BALL_MEAL)
                .save(consumer);

        // 模糊配方
        FlexPotRecipeBuilder.builder()
                .addInput(Items.SUGAR, Items.PUMPKIN_PIE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FONDANT_PIE)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_PORK, Items.BAMBOO)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.DONGPO_PORK)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(Items.SUGAR, Items.SPIDER_EYE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FONDANT_SPIDER_EYE)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, Items.CHORUS_FRUIT)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.CHORUS_FRIED_EGG)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_CHICKEN, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SPICY_CHICKEN)
                .save(consumer);

        FlexPotRecipeBuilder.builder().addInput(Items.SWEET_BERRIES, TagCommon.RAW_BEEF, Items.BONE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.PAN_SEARED_KNIGHT_STEAK)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES, Items.PUMPKIN_PIE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.STARGAZY_PIE)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(Items.ENDER_PEARL, Items.ENDER_EYE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SWEET_AND_SOUR_ENDER_PEARLS)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(Items.AMETHYST_SHARD, TagCommon.RAW_MUTTON)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.CRYSTAL_LAMB_CHOP)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(Items.BLAZE_POWDER, TagCommon.RAW_MUTTON)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.BLAZE_LAMB_CHOP)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(Items.BLUE_ICE, TagCommon.RAW_MUTTON)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FROST_LAMB_CHOP)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(Items.SUGAR, Items.POTATO)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.CANDIED_POTATO)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(ModItems.GREEN_CHILI.get(), ModItems.RAW_CUT_SMALL_MEATS.get())
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.STUFFED_TIGER_SKIN_PEPPER)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(Items.RABBIT, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SPICY_RABBIT_HEAD)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagMod.CATERPILLARS)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FRIED_CATERPILLAR)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(Items.PHANTOM_MEMBRANE, TagCommon.RAW_PORK, Tags.Items.MUSHROOMS)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FRIED_SPRING_ROLL)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.OIL_SPLASHED_FISH)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(Items.BONE, TagCommon.RAW_PORK)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.BRAISED_PORK_RIBS)
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(Items.SLIME_BALL)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SLIME_BALL_MEAL)
                .save(consumer);
    }
}
