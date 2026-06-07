package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class ShapelessRecipeProvider extends ModRecipeProvider {
    public ShapelessRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.RICE_PANICLE.get(), 9)
                .requires(ModItems.STRAW_BLOCK.get())
                .unlockedBy("has_rice_panicle", has(ModItems.RICE_PANICLE.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.OIL.get(), 9)
                .requires(ModItems.OIL_BLOCK.get())
                .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHILI_SEED.get(), 1)
                .requires(ModItems.GREEN_CHILI.get())
                .unlockedBy("has_chili", has(ModItems.GREEN_CHILI.get()))
                .save(consumer, "chili_seed_from_green_chili");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHILI_SEED.get(), 1)
                .requires(ModItems.RED_CHILI.get())
                .unlockedBy("has_chili", has(ModItems.RED_CHILI.get()))
                .save(consumer, "chili_seed_from_red_chili");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.TOMATO_SEED.get(), 1)
                .requires(ModItems.TOMATO.get())
                .unlockedBy("has_tomato", has(ModItems.TOMATO.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.STUFFED_DOUGH_FOOD.get(), 1)
                .requires(TagCommon.RAW_MEATS)
                .requires(TagCommon.VEGETABLES)
                .requires(TagCommon.DOUGH)
                .unlockedBy("has_dough", has(TagCommon.DOUGH))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RECIPE_ITEM.get(), 1)
                .requires(ModItems.RECIPE_ITEM.get())
                .unlockedBy("has_recipe_item", has(ModItems.RECIPE_ITEM.get()))
                .save(consumer, "reset_recipe_item");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RAW_MEATBALL.get(), 1)
                .requires(TagCommon.RAW_MEATS)
                .requires(TagCommon.RAW_MEATS)
                .requires(TagCommon.VEGETABLES)
                .unlockedBy("has_raw_meats", has(TagCommon.RAW_MEATS))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.EMPTY_CUP.get(), 1)
                .requires(Items.FLOWER_POT)
                .unlockedBy("has_flower_pot", has(Items.FLOWER_POT))
                .save(consumer);

        // 水果拼盘: 2苹果+2发光浆果+2甜浆果+1碗
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.FRUIT_PLATTER), 1)
                .requires(Items.APPLE, 2)
                .requires(Items.GLOW_BERRIES, 2)
                .requires(Items.SWEET_BERRIES, 2)
                .requires(Items.BOWL)
                .unlockedBy("has_apple", has(Items.APPLE))
                .save(consumer);

        // 沙拉类
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.GOLDEN_SALAD), 1)
                .requires(Items.GOLDEN_APPLE, 2)
                .requires(Items.GOLDEN_CARROT, 2)
                .requires(Items.GLISTERING_MELON_SLICE, 2)
                .requires(Items.BOWL)
                .unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE))
                .save(consumer, modLoc("golden_salad"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.NETHER_STYLE_SASHIMI), 1)
                .requires(Items.CRIMSON_FUNGUS)
                .requires(Items.WARPED_FUNGUS)
                .requires(ModItems.SASHIMI.get(), 4)
                .requires(Items.BOWL)
                .unlockedBy("has_sashimi", has(ModItems.SASHIMI.get()))
                .save(consumer, modLoc("nether_style_sashimi"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.DESERT_STYLE_SASHIMI), 1)
                .requires(Items.CACTUS, 2)
                .requires(ModItems.SASHIMI.get(), 4)
                .requires(Items.BOWL)
                .unlockedBy("has_sashimi", has(ModItems.SASHIMI.get()))
                .save(consumer, modLoc("desert_style_sashimi"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.COLD_STYLE_SASHIMI), 1)
                .requires(Items.SNOWBALL, 3)
                .requires(ModItems.SASHIMI.get(), 4)
                .requires(Items.BOWL)
                .unlockedBy("has_sashimi", has(ModItems.SASHIMI.get()))
                .save(consumer, modLoc("cold_style_sashimi"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.END_STYLE_SASHIMI), 1)
                .requires(Items.CHORUS_FRUIT, 3)
                .requires(ModItems.SASHIMI.get(), 4)
                .requires(Items.BOWL)
                .unlockedBy("has_sashimi", has(ModItems.SASHIMI.get()))
                .save(consumer, modLoc("end_style_sashimi"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.TUNDRA_STYLE_SASHIMI), 1)
                .requires(Ingredient.of(ItemTags.FLOWERS), 2)
                .requires(ModItems.SASHIMI.get(), 4)
                .requires(Items.BOWL)
                .unlockedBy("has_sashimi", has(ModItems.SASHIMI.get()))
                .save(consumer, modLoc("tundra_style_sashimi"));

        // 冷切类
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.COLD_ROASTED_MEAT), 1)
                .requires(Items.COOKED_BEEF, 3)
                .requires(Items.BOWL)
                .unlockedBy("has_cooked_beef", has(Items.COOKED_BEEF))
                .save(consumer, modLoc("cold_roasted_meat"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.COLD_CUT_HAM_SLICES.get(), 1)
                .requires(ModItems.COOKED_PORK_BELLY.get(), 8)
                .requires(Items.BOWL)
                .unlockedBy("has_cooked_pork_belly", has(ModItems.COOKED_PORK_BELLY.get()))
                .save(consumer, modLoc("cold_cut_ham_slices"));

        // 盖饭类
        addRiceBowlRecipe(consumer,
                ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get(),
                ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL.get(),
                "scramble_egg_with_tomatoes_rice_bowl"
        );

        addRiceBowlRecipe(consumer,
                ModItems.BRAISED_BEEF.get(),
                ModItems.BRAISED_BEEF_RICE_BOWL.get(),
                "braised_beef_rice_bowl"
        );

        addRiceBowlRecipe(consumer,
                ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get(),
                ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL.get(),
                "stir_fried_pork_with_peppers_rice_bowl"
        );

        addRiceBowlRecipe(consumer,
                ModItems.SWEET_AND_SOUR_PORK.get(),
                ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL.get(),
                "sweet_and_sour_pork_rice_bowl"
        );

        addRiceBowlRecipe(consumer,
                ModItems.FISH_FLAVORED_SHREDDED_PORK.get(),
                ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL.get(),
                "fish_flavored_shredded_pork_rice_bowl"
        );

        // 面团
        for (int i = 0; i < 8; i++) {
            int count = i + 1;
            String name = "flour_from_" + count + "_wheat";
            ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RAW_DOUGH.get(), count)
                    .requires(Items.WATER_BUCKET)
                    .requires(ModItems.FLOUR.get(), count)
                    .unlockedBy("has_wheat", has(Items.WHEAT))
                    .save(consumer, name);
        }
    }

    private void addRiceBowlRecipe(Consumer<FinishedRecipe> consumer, ItemLike dish, Item result, String id) {
        consumer.accept(new RiceBowlFinishedRecipe(modLoc(id), dish, result));
    }
}
