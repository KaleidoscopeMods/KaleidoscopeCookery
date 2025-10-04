package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.ingredinet.AnyIngredient;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.MillstoneRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.PartialNBTIngredient;

import java.util.function.Consumer;

public class MillstoneRecipeProvider extends ModRecipeProvider {
    public MillstoneRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.ALLIUM)
                .setResult(Items.MAGENTA_DYE, 2)
                .save(consumer, "magenta_dye_from_allium");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.AMETHYST_BLOCK)
                .setResult(Items.AMETHYST_SHARD, 3)
                .save(consumer, "amethyst_shard_from_amethyst_block");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.AZURE_BLUET)
                .setResult(Items.LIGHT_GRAY_DYE, 2)
                .save(consumer, "light_gray_dye_from_azure_bluet");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.BASALT)
                .setResult(Items.POLISHED_BASALT, 1)
                .save(consumer, "polished_basalt_from_basalt");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.BEETROOT)
                .setResult(Items.RED_DYE, 2)
                .save(consumer, "red_dye_from_beetroot");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.BLACK_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_black_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.BLACKSTONE)
                .setResult(Items.POLISHED_BLACKSTONE, 1)
                .save(consumer, "polished_blackstone_from_blackstone");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.BLUE_ORCHID)
                .setResult(Items.LIGHT_BLUE_DYE, 2)
                .save(consumer, "light_blue_dye_from_blue_orchid");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.BLUE_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_blue_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.BONE)
                .setResult(Items.BONE_MEAL, 5)
                .save(consumer, "bone_meal_from_bone");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.BROWN_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_brown_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.CACTUS)
                .setResult(Items.GREEN_DYE, 2)
                .save(consumer, "green_dye_from_cactus");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.CHISELED_QUARTZ_BLOCK)
                .setResult(Items.QUARTZ, 3)
                .save(consumer, "quartz_from_chiseled_quartz_block");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.COAL_ORE)
                .setResult(Items.COAL, 3)
                .save(consumer, "coal_from_coal_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.COBBLED_DEEPSLATE)
                .setResult(Items.POLISHED_DEEPSLATE, 1)
                .save(consumer, "polished_deepslate_from_cobbled_deepslate");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.COBBLESTONE)
                .setResult(Items.SMOOTH_STONE, 1)
                .save(consumer, "smooth_stone_from_cobblestone");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.COPPER_ORE)
                .setResult(Items.RAW_COPPER, 5)
                .save(consumer, "raw_copper_from_copper_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.CORNFLOWER)
                .setResult(Items.BLUE_DYE, 2)
                .save(consumer, "blue_dye_from_cornflower");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.CYAN_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_cyan_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DANDELION)
                .setResult(Items.YELLOW_DYE, 2)
                .save(consumer, "yellow_dye_from_dandelion");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DEEPSLATE)
                .setResult(Items.POLISHED_DEEPSLATE, 1)
                .save(consumer, "polished_deepslate_from_deepslate");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DEEPSLATE_COAL_ORE)
                .setResult(Items.COAL, 3)
                .save(consumer, "coal_from_deepslate_coal_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DEEPSLATE_COPPER_ORE)
                .setResult(Items.RAW_COPPER, 5)
                .save(consumer, "raw_copper_from_deepslate_copper_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DEEPSLATE_DIAMOND_ORE)
                .setResult(Items.DIAMOND, 2)
                .save(consumer, "diamond_from_deepslate_diamond_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DEEPSLATE_EMERALD_ORE)
                .setResult(Items.EMERALD, 2)
                .save(consumer, "emerald_from_deepslate_emerald_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DEEPSLATE_GOLD_ORE)
                .setResult(Items.RAW_GOLD, 3)
                .save(consumer, "raw_gold_from_deepslate_gold_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DEEPSLATE_IRON_ORE)
                .setResult(Items.RAW_IRON, 3)
                .save(consumer, "raw_iron_from_deepslate_iron_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DEEPSLATE_LAPIS_ORE)
                .setResult(Items.LAPIS_LAZULI, 7)
                .save(consumer, "lapis_lazuli_from_deepslate_lapis_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DEEPSLATE_REDSTONE_ORE)
                .setResult(Items.REDSTONE, 7)
                .save(consumer, "redstone_from_deepslate_redstone_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DIAMOND_ORE)
                .setResult(Items.DIAMOND, 2)
                .save(consumer, "diamond_from_diamond_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.DIORITE)
                .setResult(Items.POLISHED_DIORITE, 1)
                .save(consumer, "polished_diorite_from_diorite");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.EMERALD_ORE)
                .setResult(Items.EMERALD, 2)
                .save(consumer, "emerald_from_emerald_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.FLINT)
                .setResult(Items.GUNPOWDER, 1)
                .save(consumer, "gunpowder_from_flint");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.GILDED_BLACKSTONE)
                .setResult(Items.GOLD_NUGGET, 3)
                .save(consumer, "gold_nugget_from_gilded_blackstone");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.GOLD_ORE)
                .setResult(Items.RAW_GOLD, 3)
                .save(consumer, "raw_gold_from_gold_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.GRANITE)
                .setResult(Items.POLISHED_GRANITE, 1)
                .save(consumer, "polished_granite_from_granite");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.GRAVEL)
                .setResult(Items.SAND, 1)
                .save(consumer, "sand_from_gravel");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.GRAY_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_gray_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.GREEN_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_green_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.IRON_ORE)
                .setResult(Items.RAW_IRON, 3)
                .save(consumer, "raw_iron_from_iron_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.LAPIS_ORE)
                .setResult(Items.LAPIS_LAZULI, 7)
                .save(consumer, "lapis_lazuli_from_lapis_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.LARGE_FERN)
                .setResult(Items.GREEN_DYE, 3)
                .save(consumer, "green_dye_from_large_fern");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.LIGHT_BLUE_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_light_blue_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.LIGHT_GRAY_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_light_gray_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.LILAC)
                .setResult(Items.MAGENTA_DYE, 3)
                .save(consumer, "magenta_dye_from_lilac");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.LILY_OF_THE_VALLEY)
                .setResult(Items.WHITE_DYE, 2)
                .save(consumer, "white_dye_from_lily_of_the_valley");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.LIME_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_lime_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.MAGENTA_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_magenta_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.NETHER_GOLD_ORE)
                .setResult(Items.GOLD_NUGGET, 5)
                .save(consumer, "gold_nugget_from_nether_gold_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.NETHER_QUARTZ_ORE)
                .setResult(Items.QUARTZ, 3)
                .save(consumer, "quartz_from_nether_quartz_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.ORANGE_TULIP)
                .setResult(Items.ORANGE_DYE, 2)
                .save(consumer, "orange_dye_from_orange_tulip");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.ORANGE_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_orange_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.OXEYE_DAISY)
                .setResult(Items.LIGHT_GRAY_DYE, 2)
                .save(consumer, "light_gray_dye_from_oxeye_daisy");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.PEONY)
                .setResult(Items.PINK_DYE, 3)
                .save(consumer, "pink_dye_from_peony");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.PINK_PETALS)
                .setResult(Items.PINK_DYE, 2)
                .save(consumer, "pink_dye_from_pink_petals");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.PINK_TULIP)
                .setResult(Items.PINK_DYE, 2)
                .save(consumer, "pink_dye_from_pink_tulip");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.PINK_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_pink_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.PITCHER_PLANT)
                .setResult(Items.CYAN_DYE, 2)
                .save(consumer, "cyan_dye_from_pitcher_plant");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.POPPY)
                .setResult(Items.RED_DYE, 2)
                .save(consumer, "red_dye_from_poppy");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.PURPLE_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_purple_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.QUARTZ_BLOCK)
                .setResult(Items.QUARTZ, 3)
                .save(consumer, "quartz_from_quartz_block");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.QUARTZ_BRICKS)
                .setResult(Items.QUARTZ, 3)
                .save(consumer, "quartz_from_quartz_bricks");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.QUARTZ_PILLAR)
                .setResult(Items.QUARTZ, 3)
                .save(consumer, "quartz_from_quartz_pillar");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.RED_SAND)
                .setResult(Items.SMOOTH_RED_SANDSTONE, 1)
                .save(consumer, "smooth_red_sandstone_from_red_sand");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.RED_SANDSTONE)
                .setResult(Items.SMOOTH_RED_SANDSTONE, 1)
                .save(consumer, "smooth_red_sandstone_from_red_sandstone");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.RED_TULIP)
                .setResult(Items.RED_DYE, 2)
                .save(consumer, "red_dye_from_red_tulip");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.RED_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_red_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.REDSTONE_ORE)
                .setResult(Items.REDSTONE, 7)
                .save(consumer, "redstone_from_redstone_ore");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.ROSE_BUSH)
                .setResult(Items.RED_DYE, 3)
                .save(consumer, "red_dye_from_rose_bush");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.SAND)
                .setResult(Items.SMOOTH_SANDSTONE, 1)
                .save(consumer, "smooth_sandstone_from_sand");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.SANDSTONE)
                .setResult(Items.SMOOTH_SANDSTONE, 1)
                .save(consumer, "smooth_sandstone_from_sandstone");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.FERN)
                .setResult(Items.GREEN_DYE, 1)
                .save(consumer, "green_dye_from_fern");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.SMOOTH_BASALT)
                .setResult(Items.POLISHED_BASALT, 1)
                .save(consumer, "polished_basalt_from_smooth_basalt");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.ANDESITE)
                .setResult(Items.POLISHED_ANDESITE, 1)
                .save(consumer, "polished_andesite_from_andesite");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.SMOOTH_QUARTZ)
                .setResult(Items.QUARTZ, 3)
                .save(consumer, "quartz_from_smooth_quartz");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.STONE)
                .setResult(Items.COBBLESTONE, 1)
                .save(consumer, "cobblestone_from_stone");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.SUNFLOWER)
                .setResult(Items.YELLOW_DYE, 3)
                .save(consumer, "yellow_dye_from_sunflower");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.TORCHFLOWER)
                .setResult(Items.ORANGE_DYE, 2)
                .save(consumer, "orange_dye_from_torchflower");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.WHITE_TULIP)
                .setResult(Items.WHITE_DYE, 2)
                .save(consumer, "white_dye_from_white_tulip");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.WHITE_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_white_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.WITHER_ROSE)
                .setResult(Items.BLACK_DYE, 2)
                .save(consumer, "black_dye_from_wither_rose");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.YELLOW_WOOL)
                .setResult(Items.STRING, 3)
                .save(consumer, "string_from_yellow_wool");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Items.WHEAT)
                .setResult(ModItems.RAW_DOUGH.get())
                .setCarrier(AnyIngredient.of(
                        Ingredient.of(TagMod.MILLSTONE_DOUGH_CONTAINER),
                        PartialNBTIngredient.of(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), Potions.WATER).getOrCreateTag(), Items.POTION)
                ))
                .save(consumer, "raw_dough_from_wheat");

        MillstoneRecipeBuilder.builder()
                .setIngredient(Tags.Items.SEEDS)
                .setResult(ModItems.OIL_POT.get())
                .setCarrier(ModItems.OIL_POT.get())
                .save(consumer, "oil_pot_from_seeds");

        MillstoneRecipeBuilder.builder()
                .setIngredient(ModItems.RICE_PANICLE.get())
                .setResult(ModItems.RICE_SEED.get(), 3)
                .save(consumer, "rice_from_rice_panicle");
    }
}