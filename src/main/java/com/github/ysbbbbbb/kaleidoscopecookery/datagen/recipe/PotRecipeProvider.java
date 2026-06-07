package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.FlexPotRecipeBuilder;
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
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER,
                        TagCommon.DOUGH, TagCommon.DOUGH,
                        ModItems.RAW_CUT_SMALL_MEATS.get(), ModItems.RAW_CUT_SMALL_MEATS.get())
                .setResult(ModItems.DONKEY_BURGER.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, TagCommon.EGGS, TagCommon.COOKED_RICE)
                .setBowlCarrier()
                .setResult(ModItems.EGG_FRIED_RICE.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, TagCommon.EGGS, TagCommon.EGGS,
                        TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO)
                .setBowlCarrier()
                .setResult(ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF,
                        ModItems.RED_CHILI.get(), ModItems.RED_CHILI.get(), ModItems.RED_CHILI.get())
                .setBowlCarrier()
                .setResult(ModItems.BRAISED_BEEF.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(ModItems.GREEN_CHILI.get(), ModItems.GREEN_CHILI.get(), ModItems.GREEN_CHILI.get())
                .addInput(TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK)
                .setBowlCarrier()
                .setResult(ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Items.SUGAR, Items.SUGAR, Items.SUGAR)
                .addInput(TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK)
                .setBowlCarrier()
                .setResult(ModItems.SWEET_AND_SOUR_PORK.get())
                .save(consumer);

        PotRecipeBuilder.builder()
                .addInput(Tags.Items.MUSHROOMS, Tags.Items.MUSHROOMS, TagCommon.RAW_PORK,
                        TagCommon.RAW_PORK, TagCommon.CROPS_CHILI_PEPPER, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(ModItems.FISH_FLAVORED_SHREDDED_PORK.get())
                .save(consumer);

        // 模糊配方
        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.DOUGH, ModItems.RAW_CUT_SMALL_MEATS.get())
                .setResult(ModItems.DONKEY_BURGER.get())
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, TagCommon.COOKED_RICE)
                .setBowlCarrier()
                .setResult(ModItems.EGG_FRIED_RICE.get())
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.EGGS, TagCommon.CROPS_TOMATO)
                .setBowlCarrier()
                .setResult(ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get())
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_BEEF, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(ModItems.BRAISED_BEEF.get())
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_PORK, TagCommon.CROPS_CHILI_PEPPER)
                .setBowlCarrier()
                .setResult(ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get())
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_PORK, Items.SUGAR)
                .setBowlCarrier()
                .setResult(ModItems.SWEET_AND_SOUR_PORK.get())
                .save(consumer);

        FlexPotRecipeBuilder.builder()
                .addInput(TagCommon.RAW_PORK, TagCommon.CROPS_CHILI_PEPPER, Tags.Items.MUSHROOMS)
                .setBowlCarrier()
                .setResult(ModItems.FISH_FLAVORED_SHREDDED_PORK.get())
                .save(consumer);
    }
}
