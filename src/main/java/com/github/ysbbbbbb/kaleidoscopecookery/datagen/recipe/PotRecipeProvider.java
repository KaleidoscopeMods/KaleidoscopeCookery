package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class PotRecipeProvider extends ModRecipeProvider {
    public PotRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        PotRecipeBuilder.builder()
                .addInput(TagCommon.COOKED_EGGS, TagCommon.COOKED_EGGS, TagCommon.COOKED_EGGS,
                        TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO)
                .setBowlCarrier()
                .setResult(ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.COOKED_EGGS, TagCommon.COOKED_EGGS,
                        TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(ModItems.RAW_COW_OFFAL.get(), ModItems.RAW_COW_OFFAL.get(), ModItems.RAW_COW_OFFAL.get(),
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setBowlCarrier()
                .setResult(ModItems.STIR_FRIED_BEEF_OFFAL.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(ModItems.RAW_COW_OFFAL.get(), ModItems.RAW_COW_OFFAL.get())
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.STIR_FRIED_BEEF_OFFAL_RICE_BOWL.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(ModItems.BRAISED_BEEF.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.BRAISED_BEEF_RICE_BOWL.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(ModItems.BRAISED_BEEF.get(), 2)
                .save(consumer, "braised_beef_2");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.BRAISED_BEEF_RICE_BOWL.get(), 2)
                .save(consumer, "braised_beef_rice_bowl_2");

        PotRecipeBuilder.builder()
                .addInput(ModItems.GREEN_CHILI.get(), ModItems.GREEN_CHILI.get(), ModItems.GREEN_CHILI.get())
                .addInput(TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK)
                .setBowlCarrier()
                .setResult(ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(ModItems.GREEN_CHILI.get(), ModItems.GREEN_CHILI.get(), ModItems.GREEN_CHILI.get())
                .addInput(TagCommon.RAW_PORK, TagCommon.RAW_PORK)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.SUGAR, Items.SUGAR, Items.SUGAR)
                .addInput(TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK)
                .setBowlCarrier()
                .setResult(ModItems.SWEET_AND_SOUR_PORK.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.SUGAR, Items.SUGAR, Items.SUGAR)
                .addInput(TagCommon.RAW_PORK, TagCommon.RAW_PORK)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_LETTUCE, TagCommon.CROPS_LETTUCE, TagCommon.CROPS_TOMATO)
                .addInput(Items.CARROT, Items.POTATO)
                .setBowlCarrier()
                .setResult(ModItems.COUNTRY_STYLE_MIXED_VEGETABLES.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Tags.Items.MUSHROOMS, Tags.Items.MUSHROOMS, TagCommon.RAW_PORK,
                        TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(ModItems.FISH_FLAVORED_SHREDDED_PORK.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Tags.Items.MUSHROOMS, Tags.Items.MUSHROOMS, TagCommon.RAW_PORK,
                        TagCommon.RAW_PORK, TagCommon.CROPS_CHILI_PEPPER)
                .setCarrier(ModItems.COOKED_RICE.get())
                .setResult(ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, TagCommon.EGGS, TagCommon.COOKED_RICE)
                .setBowlCarrier()
                .setResult(ModItems.EGG_FRIED_RICE.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, TagCommon.EGGS, TagCommon.EGGS, TagCommon.EGGS,
                        TagCommon.COOKED_RICE, TagCommon.COOKED_RICE)
                .setBowlCarrier()
                .setResult(ModItems.EGG_FRIED_RICE.get(), 2)
                .save(consumer, "egg_fried_rice_2");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, TagCommon.EGGS, TagCommon.EGGS,
                        TagCommon.EGGS, TagCommon.EGGS, TagCommon.EGGS,
                        TagCommon.COOKED_RICE, TagCommon.COOKED_RICE, TagCommon.COOKED_RICE)
                .setBowlCarrier()
                .setResult(ModItems.EGG_FRIED_RICE.get(), 3)
                .save(consumer, "egg_fried_rice_3");

        PotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, TagCommon.EGGS, TagCommon.CROPS_LETTUCE,
                        TagCommon.CROPS_LETTUCE, TagCommon.COOKED_RICE)
                .addInput(Items.CARROT).setBowlCarrier()
                .setResult(ModItems.DELICIOUS_EGG_FRIED_RICE.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER)
                .addInput(TagCommon.DOUGH, TagCommon.DOUGH, ModItems.RAW_DONKEY_MEAT, ModItems.RAW_DONKEY_MEAT)
                .setResult(ModItems.DONKEY_BURGER.get())
                .save(consumer);
    }
}
