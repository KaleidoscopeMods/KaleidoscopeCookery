package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods.*;

public class FoodBiteRegistry {
    public static final Map<ResourceLocation, FoodData> FOOD_DATA_MAP = Maps.newLinkedHashMap();

    public static ResourceLocation DARK_CUISINE;
    public static ResourceLocation SUSPICIOUS_STIR_FRY;
    public static ResourceLocation SLIME_BALL_MEAL;
    public static ResourceLocation FONDANT_PIE;
    public static ResourceLocation DONGPO_PORK;
    public static ResourceLocation FONDANT_SPIDER_EYE;
    public static ResourceLocation CHORUS_FRIED_EGG;
    public static ResourceLocation BRAISED_FISH;
    public static ResourceLocation GOLDEN_SALAD;
    public static ResourceLocation SPICY_CHICKEN;
    public static ResourceLocation YAKITORI;
    public static ResourceLocation PAN_SEARED_KNIGHT_STEAK;
    public static ResourceLocation STARGAZY_PIE;
    public static ResourceLocation SWEET_AND_SOUR_ENDER_PEARLS;
    public static ResourceLocation CRYSTAL_LAMB_CHOP;
    public static ResourceLocation BLAZE_LAMB_CHOP;
    public static ResourceLocation FROST_LAMB_CHOP;
    public static ResourceLocation NETHER_STYLE_SASHIMI;
    public static ResourceLocation END_STYLE_SASHIMI;
    public static ResourceLocation DESERT_STYLE_SASHIMI;
    public static ResourceLocation TUNDRA_STYLE_SASHIMI;
    public static ResourceLocation COLD_STYLE_SASHIMI;
    public static ResourceLocation SHENGJIAN_MANTOU;
    public static ResourceLocation CANDIED_POTATO;
    public static ResourceLocation DOUGH_DROP_SOUP;
    public static ResourceLocation STUFFED_TIGER_SKIN_PEPPER;
    public static ResourceLocation SPICY_RABBIT_HEAD;
    public static ResourceLocation FOUR_JOY_MEATBALL_SOUP;
    public static ResourceLocation NUMBING_SPICY_CHICKEN;
    public static ResourceLocation FRIED_CATERPILLAR;
    public static ResourceLocation FRIED_SPRING_ROLL;
    public static ResourceLocation SPICY_BLOOD_STEW;
    public static ResourceLocation FRUIT_PLATTER;

    public static ResourceLocation BRAISED_PORK_RIBS;
    public static ResourceLocation COLD_ROASTED_MEAT;
    public static ResourceLocation OIL_SPLASHED_FISH;

    public static ResourceLocation BROWN_MUSHROOM_POT_SOUP;
    public static ResourceLocation RED_MUSHROOM_POT_SOUP;
    public static ResourceLocation WARPED_FUNGUS_POT_SOUP;
    public static ResourceLocation CRIMSON_FUNGUS_POT_SOUP;
    public static ResourceLocation BUDDHA_JUMPS_OVER_THE_WALL;

    public static void init() {
        FoodBiteRegistry registry = new FoodBiteRegistry();

        DARK_CUISINE = registry.registerFoodData("dark_cuisine", FoodData
                .create(3, DARK_CUISINE_BLOCK, DARK_CUISINE_ITEM)
                .setAnimateTick(FoodBiteAnimateTicks.DARK_CUISINE_ANIMATE_TICK));

        SUSPICIOUS_STIR_FRY = registry.registerFoodData("suspicious_stir_fry", FoodData
                .create(1, SUSPICIOUS_STIR_FRY_BLOCK, SUSPICIOUS_STIR_FRY_ITEM)
                .setAnimateTick(FoodBiteAnimateTicks.SUSPICIOUS_STIR_FRY_ANIMATE_TICK));

        SLIME_BALL_MEAL = registry.registerFoodData("slime_ball_meal", FoodData
                .create(3, SLIME_BALL_MEAL_BLOCK, SLIME_BALL_MEAL_ITEM));

        FONDANT_PIE = registry.registerFoodData("fondant_pie", FoodData
                .create(4, FONDANT_PIE_BLOCK, FONDANT_PIE_ITEM));

        DONGPO_PORK = registry.registerFoodData("dongpo_pork", FoodData
                .create(3, DONGPO_PORK_BLOCK, DONGPO_PORK_ITEM)
                .addLootItems(Items.BAMBOO));

        FONDANT_SPIDER_EYE = registry.registerFoodData("fondant_spider_eye", FoodData
                .create(4, FONDANT_SPIDER_EYE_BLOCK, FONDANT_SPIDER_EYE_ITEM));

        CHORUS_FRIED_EGG = registry.registerFoodData("chorus_fried_egg", FoodData
                .create(3, CHORUS_FRIED_EGG_BLOCK, CHORUS_FRIED_EGG_ITEM));

        BRAISED_FISH = registry.registerFoodData("braised_fish", FoodData
                .create(4, BRAISED_FISH_BLOCK, BRAISED_FISH_ITEM)
                .addLootItems(Items.BONE, Items.BONE_MEAL));

        GOLDEN_SALAD = registry.registerFoodData("golden_salad", FoodData
                .create(6, GOLDEN_SALAD_BLOCK, GOLDEN_SALAD_ITEM));

        SPICY_CHICKEN = registry.registerFoodData("spicy_chicken", FoodData
                .create(4, SPICY_CHICKEN_BLOCK, SPICY_CHICKEN_ITEM));

        YAKITORI = registry.registerFoodData("yakitori", FoodData
                .create(4, YAKITORI_BLOCK, YAKITORI_ITEM));

        PAN_SEARED_KNIGHT_STEAK = registry.registerFoodData("pan_seared_knight_steak", FoodData
                .create(4, PAN_SEARED_KNIGHT_STEAK_BLOCK, PAN_SEARED_KNIGHT_STEAK_ITEM)
                .addLootItems(Items.BONE, Items.BONE_MEAL));

        STARGAZY_PIE = registry.registerFoodData("stargazy_pie", FoodData
                .create(4, STARGAZY_PIE_BLOCK, STARGAZY_PIE_ITEM));

        SWEET_AND_SOUR_ENDER_PEARLS = registry.registerFoodData("sweet_and_sour_ender_pearls", FoodData
                .create(3, SWEET_AND_SOUR_ENDER_PEARLS_BLOCK, SWEET_AND_SOUR_ENDER_PEARLS_ITEM));

        CRYSTAL_LAMB_CHOP = registry.registerFoodData("crystal_lamb_chop", FoodData
                .create(3, CRYSTAL_LAMB_CHOP_BLOCK, CRYSTAL_LAMB_CHOP_ITEM)
                .addLootItems(Items.AMETHYST_SHARD));

        BLAZE_LAMB_CHOP = registry.registerFoodData("blaze_lamb_chop", FoodData
                .create(3, BLAZE_LAMB_CHOP_BLOCK, BLAZE_LAMB_CHOP_ITEM)
                .addLootItems(Items.BLAZE_ROD));

        FROST_LAMB_CHOP = registry.registerFoodData("frost_lamb_chop", FoodData
                .create(3, FROST_LAMB_CHOP_BLOCK, FROST_LAMB_CHOP_ITEM)
                .addLootItems(Items.BLUE_ICE));

        NETHER_STYLE_SASHIMI = registry.registerFoodData("nether_style_sashimi", FoodData
                .create(4, NETHER_STYLE_SASHIMI_BLOCK, NETHER_STYLE_SASHIMI_ITEM)
                .addLootItems(Items.CRIMSON_FUNGUS, Items.WARPED_FUNGUS));

        END_STYLE_SASHIMI = registry.registerFoodData("end_style_sashimi", FoodData
                .create(4, END_STYLE_SASHIMI_BLOCK, END_STYLE_SASHIMI_ITEM)
                .addLootItems(Items.CHORUS_FRUIT));

        DESERT_STYLE_SASHIMI = registry.registerFoodData("desert_style_sashimi", FoodData
                .create(4, DESERT_STYLE_SASHIMI_BLOCK, DESERT_STYLE_SASHIMI_ITEM)
                .addLootItems(Items.CACTUS));

        TUNDRA_STYLE_SASHIMI = registry.registerFoodData("tundra_style_sashimi", FoodData
                .create(4, TUNDRA_STYLE_SASHIMI_BLOCK, TUNDRA_STYLE_SASHIMI_ITEM));

        COLD_STYLE_SASHIMI = registry.registerFoodData("cold_style_sashimi", FoodData
                .create(4, COLD_STYLE_SASHIMI_BLOCK, COLD_STYLE_SASHIMI_ITEM)
                .addLootItems(Items.SNOWBALL, Items.SNOWBALL));

        SHENGJIAN_MANTOU = registry.registerFoodData("shengjian_mantou", FoodData
                .create(4, SHENGJIAN_MANTOU_BLOCK, SHENGJIAN_MANTOU_ITEM));

        CANDIED_POTATO = registry.registerFoodData("candied_potato", FoodData
                .create(3, CANDIED_POTATO_BLOCK, CANDIED_POTATO_ITEM));

        DOUGH_DROP_SOUP = registry.registerFoodData("dough_drop_soup", FoodData
                .create(3, DOUGH_DROP_SOUP_BLOCK, DOUGH_DROP_SOUP_ITEM)
                .bowlAABB());

        STUFFED_TIGER_SKIN_PEPPER = registry.registerFoodData("stuffed_tiger_skin_pepper", FoodData
                .create(5, STUFFED_TIGER_SKIN_PEPPER_BLOCK, STUFFED_TIGER_SKIN_PEPPER_ITEM));

        SPICY_RABBIT_HEAD = registry.registerFoodData("spicy_rabbit_head", FoodData
                .create(3, SPICY_RABBIT_HEAD_BLOCK, SPICY_RABBIT_HEAD_ITEM));

        FOUR_JOY_MEATBALL_SOUP = registry.registerFoodData("four_joy_meatball_soup", FoodData
                .create(4, FOUR_JOY_MEATBALL_SOUP_BLOCK, FOUR_JOY_MEATBALL_SOUP_ITEM)
                .bowlAABB());

        NUMBING_SPICY_CHICKEN = registry.registerFoodData("numbing_spicy_chicken", FoodData
                .create(3, NUMBING_SPICY_CHICKEN_BLOCK, NUMBING_SPICY_CHICKEN_ITEM)
                .bowlAABB());

        FRIED_CATERPILLAR = registry.registerFoodData("fried_caterpillar", FoodData
                .create(3, FRIED_CATERPILLAR_BLOCK, FRIED_CATERPILLAR_ITEM)
                .setAABB(Block.box(1, 0, 3, 15, 4, 13)));

        FRIED_SPRING_ROLL = registry.registerFoodData("fried_spring_roll", FoodData
                .create(3, FRIED_SPRING_ROLL_BLOCK, FRIED_SPRING_ROLL_ITEM));

        SPICY_BLOOD_STEW = registry.registerFoodData("spicy_blood_stew", FoodData
                .create(3, SPICY_BLOOD_STEW_BLOCK, SPICY_BLOOD_STEW_ITEM)
                .bowlAABB());

        FRUIT_PLATTER = registry.registerFoodData("fruit_platter", FoodData
                .create(4, FRUIT_PLATTER_BLOCK, FRUIT_PLATTER_ITEM));

        // ========================== 1x2 食物 ==========================

        BRAISED_PORK_RIBS = registry.registerFoodData("braised_pork_ribs", FoodData
                .createOneByTwo(4, BRAISED_PORK_RIBS_BLOCK, BRAISED_PORK_RIBS_ITEM)
                .addLootItems(Items.BONE));

        COLD_ROASTED_MEAT = registry.registerFoodData("cold_roasted_meat", FoodData
                .createOneByTwo(3, COLD_ROASTED_MEAT_BLOCK, COLD_ROASTED_MEAT_ITEM));

        OIL_SPLASHED_FISH = registry.registerFoodData("oil_splashed_fish", FoodData
                .createOneByTwo(5, OIL_SPLASHED_FISH_BLOCK, OIL_SPLASHED_FISH_ITEM)
                .addLootItems(Items.BONE_MEAL));

        // ========================== 瓦罐汤 ==========================

        BROWN_MUSHROOM_POT_SOUP = registry.registerFoodData("brown_mushroom_pot_soup", FoodData
                .create(2, BROWN_MUSHROOM_POT_SOUP_BLOCK, BROWN_MUSHROOM_POT_SOUP_ITEM)
                .setLootItem(Items.FLOWER_POT)
                .soupPotAABB()
                .potSoupAnimateTick());

        RED_MUSHROOM_POT_SOUP = registry.registerFoodData("red_mushroom_pot_soup", FoodData
                .create(2, RED_MUSHROOM_POT_SOUP_BLOCK, RED_MUSHROOM_POT_SOUP_ITEM)
                .setLootItem(Items.FLOWER_POT)
                .soupPotAABB()
                .potSoupAnimateTick());

        WARPED_FUNGUS_POT_SOUP = registry.registerFoodData("warped_fungus_pot_soup", FoodData
                .create(2, WARPED_FUNGUS_POT_SOUP_BLOCK, WARPED_FUNGUS_POT_SOUP_ITEM)
                .setLootItem(Items.FLOWER_POT)
                .soupPotAABB()
                .potSoupAnimateTick());

        CRIMSON_FUNGUS_POT_SOUP = registry.registerFoodData("crimson_fungus_pot_soup", FoodData
                .create(2, CRIMSON_FUNGUS_POT_SOUP_BLOCK, CRIMSON_FUNGUS_POT_SOUP_ITEM)
                .setLootItem(Items.FLOWER_POT)
                .soupPotAABB()
                .potSoupAnimateTick());

        BUDDHA_JUMPS_OVER_THE_WALL = registry.registerFoodData("buddha_jumps_over_the_wall", FoodData
                .create(2, BUDDHA_JUMPS_OVER_THE_WALL_BLOCK, BUDDHA_JUMPS_OVER_THE_WALL_ITEM)
                .setLootItem(Items.FLOWER_POT)
                .soupPotAABB()
                .potSoupAnimateTick());
    }

    public ResourceLocation registerFoodData(ResourceLocation foodName, FoodData data) {
        FOOD_DATA_MAP.put(foodName, data);
        return foodName;
    }

    public ResourceLocation registerFoodData(String foodName, FoodData data) {
        ResourceLocation id = mcLoc(foodName);
        FOOD_DATA_MAP.put(id, data);
        return id;
    }

    public static ResourceLocation mcLoc(String name) {
        return new ResourceLocation(KaleidoscopeCookery.MOD_ID, name);
    }

    public static Item getItem(ResourceLocation name) {
        return ForgeRegistries.ITEMS.getValue(name);
    }

    public static Block getBlock(ResourceLocation name) {
        return ForgeRegistries.BLOCKS.getValue(name);
    }

    public static final class FoodData {
        private final BlockType blockType;
        private final int maxBites;
        private final List<ItemLike> lootItems = Lists.newArrayList();
        private final FoodProperties blockFood;
        private final FoodProperties itemFood;
        private @Nullable FoodBiteAnimateTicks.AnimateTick animateTick = null;
        private @Nullable VoxelShape aabb = null;

        private FoodData(BlockType blockType, int maxBites, FoodProperties blockFood, FoodProperties itemFood) {
            this.blockType = blockType;
            this.maxBites = maxBites;
            this.lootItems.add(Items.BOWL);
            this.blockFood = blockFood;
            this.itemFood = itemFood;
        }

        public static FoodData create(int maxBites, FoodProperties blockFood, FoodProperties itemFood) {
            return new FoodData(BlockType.SINGLE, maxBites, blockFood, itemFood);
        }

        public static FoodData createOneByTwo(int maxBites, FoodProperties blockFood, FoodProperties itemFood) {
            return new FoodData(BlockType.ONE_BY_TWO, maxBites, blockFood, itemFood);
        }

        public FoodData setAnimateTick(FoodBiteAnimateTicks.AnimateTick animateTick) {
            this.animateTick = animateTick;
            return this;
        }

        public FoodData potSoupAnimateTick() {
            this.animateTick = FoodBiteAnimateTicks.POT_SOUP_ANIMATE_TICK;
            return this;
        }

        public FoodData addLootItems(ItemLike... lootItems) {
            this.lootItems.addAll(Arrays.stream(lootItems).toList());
            return this;
        }

        public FoodData setLootItem(ItemLike lootItem) {
            this.lootItems.clear();
            this.lootItems.add(lootItem);
            return this;
        }

        public FoodData setAABB(VoxelShape aabb) {
            this.aabb = aabb;
            return this;
        }

        public FoodData bowlAABB() {
            this.aabb = Block.box(2, 0, 2, 14, 6, 14);
            return this;
        }

        public FoodData soupPotAABB() {
            this.aabb = Shapes.or(
                    Block.box(1, 0, 1, 15, 1, 15),
                    Block.box(4, 1, 4, 12, 7, 12)
            );
            return this;
        }

        public BlockType blockType() {
            return blockType;
        }

        public int maxBites() {
            return maxBites;
        }

        @Nullable
        public VoxelShape getAABB() {
            return aabb;
        }

        @Nullable
        public FoodBiteAnimateTicks.AnimateTick animateTick() {
            return animateTick;
        }

        public List<ItemLike> getLootItems() {
            return lootItems;
        }

        public FoodProperties blockFood() {
            return blockFood;
        }

        public FoodProperties itemFood() {
            return itemFood;
        }
    }

    public enum BlockType {
        SINGLE,
        ONE_BY_TWO
    }
}
