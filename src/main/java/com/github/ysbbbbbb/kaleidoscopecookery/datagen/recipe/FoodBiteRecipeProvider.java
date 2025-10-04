package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class FoodBiteRecipeProvider extends ModRecipeProvider {
    public FoodBiteRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        PotRecipeBuilder.builder()
                .addInput(Items.HONEY_BOTTLE, Items.HONEY_BOTTLE, Items.HONEY_BOTTLE,
                        Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.PUMPKIN_PIE)
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
                .addInput(Items.SUGAR, Items.SUGAR, Items.SUGAR,
                        Items.SPIDER_EYE, Items.SPIDER_EYE, Items.SPIDER_EYE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FONDANT_SPIDER_EYE)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.CHORUS_FRUIT, Items.CHORUS_FRUIT, Items.CHORUS_FRUIT,
                        TagCommon.COOKED_EGGS, TagCommon.COOKED_EGGS, TagCommon.COOKED_EGGS)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.CHORUS_FRIED_EGG, 3)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD,
                        TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD,
                        TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.BRAISED_FISH)
                .save(consumer, "braised_fish_cod");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES_SALMON, TagCommon.RAW_FISHES_SALMON,
                        TagCommon.RAW_FISHES_SALMON, TagCommon.RAW_FISHES_SALMON,
                        TagCommon.RAW_FISHES_SALMON, TagCommon.RAW_FISHES_SALMON)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.BRAISED_FISH)
                .save(consumer, "braised_fish_salmon");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD,
                        TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD,
                        TagCommon.RAW_FISHES_COD)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.BRAISED_FISH_RICE_BOWL.get())
                .save(consumer, "braised_fish_cod_with_rice");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES_SALMON, TagCommon.RAW_FISHES_SALMON,
                        TagCommon.RAW_FISHES_SALMON, TagCommon.RAW_FISHES_SALMON,
                        TagCommon.RAW_FISHES_SALMON)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.BRAISED_FISH_RICE_BOWL.get())
                .save(consumer, "braised_fish_salmon_with_rice");

        PotRecipeBuilder.builder()
                .addInput(Items.GOLDEN_APPLE, Items.GOLDEN_APPLE,
                        Items.GOLDEN_CARROT, Items.GOLDEN_CARROT,
                        Items.GLISTERING_MELON_SLICE, Items.GLISTERING_MELON_SLICE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.GOLDEN_SALAD)
                .save(consumer, "golden_salad_golden_apple");

        PotRecipeBuilder.builder()
                .addInput(Items.ENCHANTED_GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE,
                        Items.GOLDEN_CARROT, Items.GOLDEN_CARROT,
                        Items.GLISTERING_MELON_SLICE, Items.GLISTERING_MELON_SLICE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.GOLDEN_SALAD)
                .save(consumer, "golden_salad_enchanted_golden_apple");

        PotRecipeBuilder.builder()
                .addInput(Items.AMETHYST_SHARD, Items.AMETHYST_SHARD, Items.AMETHYST_SHARD,
                        TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.CRYSTAL_LAMB_CHOP)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.CRIMSON_FUNGUS, Items.CRIMSON_FUNGUS,
                        Items.WARPED_FUNGUS, Items.WARPED_FUNGUS,
                        TagCommon.RAW_FISHES_TROPICAL, TagCommon.RAW_FISHES_TROPICAL,
                        TagCommon.RAW_FISHES_TROPICAL, TagCommon.RAW_FISHES_TROPICAL)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.NETHER_STYLE_SASHIMI)
                .save(consumer);

        PotRecipeBuilder.builder().addInput(Items.BONE, Items.BONE, Items.BONE,
                        Items.SWEET_BERRIES, Items.SWEET_BERRIES, Items.SWEET_BERRIES,
                        TagCommon.RAW_BEEF, TagCommon.RAW_BEEF)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.PAN_SEARED_KNIGHT_STEAK)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.PUMPKIN_PIE, TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD,
                        TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD, TagCommon.RAW_FISHES_COD)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.STARGAZY_PIE)
                .save(consumer, "stargazy_pie_cod");

        PotRecipeBuilder.builder()
                .addInput(Items.PUMPKIN_PIE, TagCommon.RAW_FISHES_SALMON, TagCommon.RAW_FISHES_SALMON,
                        TagCommon.RAW_FISHES_SALMON, TagCommon.RAW_FISHES_SALMON, TagCommon.RAW_FISHES_SALMON)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.STARGAZY_PIE)
                .save(consumer, "stargazy_pie_salmon");

        PotRecipeBuilder.builder()
                .addInput(Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_EYE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SWEET_AND_SOUR_ENDER_PEARLS)
                .save(consumer, "sweet_and_sour_ender_pearls_1");

        PotRecipeBuilder.builder()
                .addInput(Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_PEARL,
                        Items.ENDER_PEARL, Items.ENDER_EYE, Items.ENDER_EYE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SWEET_AND_SOUR_ENDER_PEARLS, 2)
                .save(consumer, "sweet_and_sour_ender_pearls_2");

        PotRecipeBuilder.builder()
                .addInput(Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_PEARL,
                        Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_PEARL,
                        Items.ENDER_EYE, Items.ENDER_EYE, Items.ENDER_EYE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SWEET_AND_SOUR_ENDER_PEARLS, 3)
                .save(consumer, "sweet_and_sour_ender_pearls_3");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN,
                        TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN,
                        Items.BLAZE_POWDER)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SPICY_CHICKEN)
                .save(consumer, "spicy_chicken_blaze_powder");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN,
                        Items.BLAZE_POWDER)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.SPICY_CHICKEN_RICE_BOWL.get())
                .save(consumer, "spicy_chicken_rice_bowl_blaze_powder");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.RAW_CHICKEN,
                        TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.SPICY_CHICKEN)
                .save(consumer, "spicy_chicken");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.RAW_CHICKEN,
                        TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.SPICY_CHICKEN_RICE_BOWL.get())
                .save(consumer, "spicy_chicken_rice_bowl");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN,
                        TagCommon.RAW_CHICKEN, TagCommon.RAW_CHICKEN)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.YAKITORI)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON,
                        Items.BLAZE_ROD, Items.BLAZE_ROD, Items.BLAZE_ROD)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.BLAZE_LAMB_CHOP).save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON, TagCommon.RAW_MUTTON,
                        Items.BLUE_ICE, Items.BLUE_ICE, Items.BLUE_ICE)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.FROST_LAMB_CHOP)
                .save(consumer);

        PotRecipeBuilder.builder().addInput(TagCommon.RAW_FISHES_TROPICAL, TagCommon.RAW_FISHES_TROPICAL,
                        TagCommon.RAW_FISHES_TROPICAL, TagCommon.RAW_FISHES_TROPICAL)
                .addInput(Items.CHORUS_FRUIT, Items.CHORUS_FRUIT, Items.CHORUS_FRUIT)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.END_STYLE_SASHIMI)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES_TROPICAL, TagCommon.RAW_FISHES_TROPICAL,
                        TagCommon.RAW_FISHES_TROPICAL, TagCommon.RAW_FISHES_TROPICAL,
                        Items.CACTUS, Items.CACTUS, Items.CACTUS)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.DESERT_STYLE_SASHIMI)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES_TROPICAL, TagCommon.RAW_FISHES_TROPICAL,
                        TagCommon.RAW_FISHES_TROPICAL, TagCommon.RAW_FISHES_TROPICAL,
                        ItemTags.FLOWERS, ItemTags.FLOWERS, ItemTags.FLOWERS,
                        ItemTags.FLOWERS, ItemTags.FLOWERS)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.TUNDRA_STYLE_SASHIMI)
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_FISHES_TROPICAL, TagCommon.RAW_FISHES_TROPICAL,
                        TagCommon.RAW_FISHES_TROPICAL, TagCommon.RAW_FISHES_TROPICAL,
                        Items.SNOWBALL, Items.SNOWBALL, Items.SNOWBALL,
                        Items.SNOWBALL, Blocks.SPRUCE_SAPLING)
                .setBowlCarrier()
                .setResult(FoodBiteRegistry.COLD_STYLE_SASHIMI)
                .save(consumer);

        Item slimeBallMeal = FoodBiteRegistry.getItem(FoodBiteRegistry.SLIME_BALL_MEAL);
        addSameItemRecipe(Items.SLIME_BALL, 4, slimeBallMeal.getDefaultInstance(), Items.BOWL, consumer);
        addSameItemRecipe(Items.SLIME_BALL, 5, slimeBallMeal.getDefaultInstance(), Items.BOWL, consumer);
        addSameItemRecipe(Items.SLIME_BALL, 6, slimeBallMeal.getDefaultInstance(), Items.BOWL, consumer);
        addSameItemRecipe(Items.SLIME_BALL, 7, slimeBallMeal.getDefaultInstance(), Items.BOWL, consumer);
        addSameItemRecipe(Items.SLIME_BALL, 8, new ItemStack(slimeBallMeal, 2), Items.BOWL, consumer);
        addSameItemRecipe(Items.SLIME_BALL, 9, new ItemStack(slimeBallMeal, 2), Items.BOWL, consumer);
    }

    public void addSameItemRecipe(Item inputItem, int count, ItemStack output, ItemLike carrier, Consumer<FinishedRecipe> consumer) {
        addSameItemRecipe(inputItem, count, output, Ingredient.of(carrier), consumer);
    }

    @SuppressWarnings("all")
    public void addSameItemRecipe(Item inputItem, int count, ItemStack output, Ingredient carrier, Consumer<FinishedRecipe> consumer) {
        ItemLike[] inputs = this.getItemsWithCount(inputItem, count);
        String idInput = this.getRecipeIdWithCount(inputItem, count);
        String idOutput = this.getRecipeIdWithCount(output.getItem(), output.getCount());
        String id = String.format("%s_to_%s", idInput, idOutput);
        PotRecipeBuilder.builder().addInput(inputs).setResult(output).setCarrier(carrier).save(consumer, id);
    }
}
