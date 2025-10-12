package com.github.ysbbbbbb.kaleidoscopecookery.datagen.advancement;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static com.github.ysbbbbbb.kaleidoscopecookery.datagen.advancement.AdvancementTools.*;

public class BaseAdvancement {
    public static void generate(Consumer<Advancement> saver) {
        // 根成就 - 森罗厨房
        Advancement root = makeTask(ModItems.POT, "root")
                .addCriterion("has_pot", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(TagMod.COOKERY_MOD_ITEMS).build()
                )).save(saver, modLoc("root"));

        // 菜刀系列成就
        Advancement ironKnife = makeTask(ModItems.IRON_KITCHEN_KNIFE, "iron_knife")
                .parent(root)
                .addCriterion("has_iron_knife", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE).build()
                ))
                .save(saver, modLoc("iron_knife"));

        Advancement netheriteKnife = makeGoal(ModItems.NETHERITE_KITCHEN_KNIFE, "netherite_knife")
                .parent(ironKnife)
                .addCriterion("has_netherite_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHERITE_KITCHEN_KNIFE))
                .save(saver, modLoc("netherite_knife"));

        // 油脂相关成就
        Advancement oil = makeTask(ModItems.OIL, "oil")
                .parent(ironKnife)
                .addCriterion("kill_pig", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(EntityType.PIG),
                        DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(
                                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE).build()
                                ).build()
                        ))
                ))
                .addCriterion("kill_piglin", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(EntityType.PIGLIN),
                        DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(
                                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE).build()
                                ).build()
                        ))
                ))
                .addCriterion("kill_piglin_brute", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(EntityType.PIGLIN_BRUTE),
                        DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(
                                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE).build()
                                ).build()
                        ))
                ))
                .addCriterion("kill_hoglin", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(EntityType.HOGLIN),
                        DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(
                                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE).build()
                                ).build()
                        ))
                ))
                .requirements(RequirementsStrategy.OR)
                .save(saver, modLoc("oil"));

        Advancement choppingBoard = makeTask(ModItems.CHOPPING_BOARD, "chopping_board")
                .parent(ironKnife)
                .addCriterion("use_chopping_board", ModEventTrigger.create(ModEventTriggerType.USE_CHOPPING_BOARD))
                .save(saver, modLoc("chopping_board"));

        Advancement dangerousChef = makeChallenge(ModItems.OIL, "dangerous_chef")
                .parent(oil)
                .addCriterion("kill_piglin_brute", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(EntityType.PIGLIN_BRUTE),
                        DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(
                                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE).build()
                                ).build()
                        ))
                ))
                .save(saver, modLoc("dangerous_chef"));

        // 草帽系列成就
        Advancement strawHat = makeTask(ModItems.STRAW_HAT, "straw_hat")
                .parent(root)
                .addCriterion("has_straw_hat", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW_HAT))
                .save(saver, modLoc("straw_hat"));

        Advancement modSeed = makeTask(ModItems.TOMATO_SEED, "tomato_seed")
                .parent(strawHat)
                .addCriterion("has_tomato_seed", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(TagMod.COOKERY_MOD_SEEDS).build()
                ))
                .requirements(RequirementsStrategy.OR)
                .save(saver, modLoc("tomato_seed"));

        Advancement flowerStrawHat = makeTask(ModItems.STRAW_HAT_FLOWER, "flower_straw_hat")
                .parent(strawHat)
                .addCriterion("has_flower_straw_hat", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW_HAT_FLOWER))
                .save(saver, modLoc("flower_straw_hat"));

        Advancement farmerSet = makeTask(ModItems.FARMER_CHEST_PLATE, "farmer_set")
                .parent(strawHat)
                .addCriterion("has_straw_hat", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW_HAT, ModItems.STRAW_HAT_FLOWER))
                .addCriterion("has_farmer_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FARMER_CHEST_PLATE))
                .addCriterion("has_farmer_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FARMER_LEGGINGS))
                .addCriterion("has_farmer_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FARMER_BOOTS))
                .requirements(RequirementsStrategy.AND)
                .save(saver, modLoc("farmer_set"));

        Advancement ironHoe = makeTask(Items.IRON_HOE, "iron_hoe")
                .parent(modSeed)
                .addCriterion("use_hoe_on_water_field", ModEventTrigger.create(ModEventTriggerType.USE_HOE_ON_WATER_FIELD))
                .save(saver, modLoc("iron_hoe"));

        Advancement caterpillar = makeTask(ModItems.CATERPILLAR, "caterpillar")
                .parent(modSeed)
                .addCriterion("has_caterpillar", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CATERPILLAR))
                .save(saver, modLoc("caterpillar"));

        Advancement dualChili = makeTask(ModItems.RED_CHILI, "dual_chili")
                .parent(modSeed)
                .addCriterion("has_red_chili", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RED_CHILI))
                .addCriterion("has_green_chili", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GREEN_CHILI))
                .requirements(RequirementsStrategy.AND)
                .save(saver, modLoc("dual_chili"));

        Advancement ricePanicle = makeTask(ModItems.RICE_PANICLE, "rice_panicle")
                .parent(ironHoe)
                .addCriterion("has_rice_panicle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RICE_PANICLE))
                .save(saver, modLoc("rice_panicle"));

        Advancement feedChicken = makeTask(ModItems.CATERPILLAR, "feed_chicken")
                .parent(caterpillar)
                .addCriterion("use_caterpillar_feed_chicken", ModEventTrigger.create(ModEventTriggerType.USE_CATERPILLAR_FEED_CHICKEN))
                .save(saver, modLoc("feed_chicken"));

        Advancement fishRice = makeGoal(Items.SALMON, "fish_rice")
                .parent(ricePanicle)
                .addCriterion("place_fish_in_rice_field", ModEventTrigger.create(ModEventTriggerType.PLACE_FISH_IN_RICE_FIELD))
                .save(saver, modLoc("fish_rice"));


        // 烹饪相关成就
        Advancement stove = makeTask(ModItems.STOVE, "stove")
                .parent(root)
                .addCriterion("lit_the_stove", ModEventTrigger.create(ModEventTriggerType.LIT_THE_STOVE))
                .save(saver, modLoc("stove"));

        Advancement pot = makeTask(ModItems.POT, "pot")
                .parent(stove)
                .addCriterion("place_pot_on_heat_source", ModEventTrigger.create(ModEventTriggerType.PLACE_POT_ON_HEAT_SOURCE))
                .save(saver, modLoc("pot"));

        Advancement addOil = makeTask(ModItems.OIL, "add_oil")
                .parent(pot)
                .addCriterion("put_oil_in_pot", ModEventTrigger.create(ModEventTriggerType.PUT_OIL_IN_POT))
                .save(saver, modLoc("add_oil"));

        Advancement stirFry = makeTask(ModItems.KITCHEN_SHOVEL, "stir_fry")
                .parent(addOil)
                .addCriterion("stir_fry_in_pot", ModEventTrigger.create(ModEventTriggerType.STIR_FRY_IN_POT))
                .save(saver, modLoc("stir_fry"));

        Advancement darkCuisine = makeTask(ModItems.SUSPICIOUS_STIR_FRY_RICE_BOWL, "dark_cuisine")
                .parent(stirFry)
                .addCriterion("has_suspicious_stew", InventoryChangeTrigger.TriggerInstance.hasItems(
                        FoodBiteRegistry.getItem(FoodBiteRegistry.SUSPICIOUS_STIR_FRY)
                ))
                .addCriterion("has_dark_cuisine", InventoryChangeTrigger.TriggerInstance.hasItems(
                        FoodBiteRegistry.getItem(FoodBiteRegistry.DARK_CUISINE)
                ))
                .addCriterion("has_suspicious_stir_fry_rice_bowl", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.SUSPICIOUS_STIR_FRY_RICE_BOWL
                ))
                .requirements(RequirementsStrategy.OR)
                .save(saver, modLoc("dark_cuisine"));

        Advancement burnHand = makeTask(Items.FLINT_AND_STEEL, "burn_hand")
                .parent(stirFry)
                .addCriterion("hurt_when_takeout_from_pot", ModEventTrigger.create(ModEventTriggerType.HURT_WHEN_TAKEOUT_FROM_POT))
                .save(saver, modLoc("burn_hand"));

        // 汤锅相关成就
        Advancement stockpot = makeTask(ModItems.STOCKPOT, "stockpot")
                .parent(stove)
                .addCriterion("place_stockpot_on_heat_source", ModEventTrigger.create(ModEventTriggerType.PLACE_STOCKPOT_ON_HEAT_SOURCE))
                .save(saver, modLoc("stockpot"));

        Advancement addBroth = makeTask(Items.LAVA_BUCKET, "add_broth")
                .parent(stockpot)
                .addCriterion("put_soup_base_in_stockpot", ModEventTrigger.create(ModEventTriggerType.PUT_SOUP_BASE_IN_STOCKPOT))
                .save(saver, modLoc("add_broth"));

        Advancement makeSoup = makeTask(ModItems.STOCKPOT_LID, "make_soup")
                .parent(addBroth)
                .addCriterion("use_lid_on_stockpot", ModEventTrigger.create(ModEventTriggerType.USE_LID_ON_STOCKPOT))
                .save(saver, modLoc("make_soup"));

        Advancement burnHandSoup = makeTask(Items.FLINT_AND_STEEL, "burn_hand_soup")
                .parent(makeSoup)
                .addCriterion("hurt_when_takeout_from_stockpot", ModEventTrigger.create(ModEventTriggerType.HURT_WHEN_TAKEOUT_FROM_STOCKPOT))
                .save(saver, modLoc("burn_hand_soup"));

        Advancement apprenticeChef = makeGoal(ModItems.BORSCHT, "apprentice_chef")
                .parent(makeSoup)
                .addCriterion("has_borscht", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BORSCHT))
                .save(saver, modLoc("apprentice_chef"));

        // 胀气系列成就（挑战型）
        Advancement nitrogen100 = makeGoal(Items.FEATHER, "nitrogen_100")
                .parent(root)
                .addCriterion("climb_100", flatulenceFlyHeight(
                        EntityPredicate.Builder.entity().located(LocationPredicate.atYLocation(MinMaxBounds.Doubles.atMost(320))),
                        DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(100)),
                        LocationPredicate.atYLocation(MinMaxBounds.Doubles.atLeast(-60))
                ))
                .save(saver, modLoc("nitrogen_100"));

        Advancement nitrogen300 = makeGoal(Items.FEATHER, "nitrogen_300")
                .parent(nitrogen100)
                .addCriterion("climb_300", flatulenceFlyHeight(
                        EntityPredicate.Builder.entity().located(LocationPredicate.atYLocation(MinMaxBounds.Doubles.atMost(500))),
                        DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(300)),
                        LocationPredicate.atYLocation(MinMaxBounds.Doubles.atLeast(-60))
                ))
                .save(saver, modLoc("nitrogen_300"));

        Advancement nitrogen1000 = makeGoal(Items.FEATHER, "nitrogen_1000")
                .parent(nitrogen300)
                .addCriterion("climb_1000", flatulenceFlyHeight(
                        EntityPredicate.Builder.entity().located(LocationPredicate.atYLocation(MinMaxBounds.Doubles.atMost(1200))),
                        DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(1000)),
                        LocationPredicate.atYLocation(MinMaxBounds.Doubles.atLeast(-60))
                ))
                .save(saver, modLoc("nitrogen_1000"));

        Advancement nitrogen3000 = makeChallenge(Items.FEATHER, "nitrogen_3000")
                .parent(nitrogen1000)
                .addCriterion("climb_3000", flatulenceFlyHeight(
                        EntityPredicate.Builder.entity().located(LocationPredicate.atYLocation(MinMaxBounds.Doubles.atMost(3200))),
                        DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(3000)),
                        LocationPredicate.atYLocation(MinMaxBounds.Doubles.atLeast(-60))
                ))
                .save(saver, modLoc("nitrogen_3000"));

        // 稻草人相关成就
        Advancement scarecrow = makeTask(ModItems.SCARECROW, "scarecrow")
                .parent(farmerSet)
                .addCriterion("place_scarecrow", ModEventTrigger.create(ModEventTriggerType.PLACE_SCARECROW))
                .save(saver, modLoc("scarecrow"));

        Advancement scarecrowHead = makeTask(Items.SKELETON_SKULL, "scarecrow_head")
                .parent(scarecrow)
                .addCriterion("place_head_on_scarecrow", ModEventTrigger.create(ModEventTriggerType.PLACE_HEAD_ON_SCARECROW))
                .save(saver, modLoc("scarecrow_head"));

        // 饭袋成就，独立的
        Advancement lunchBag = makeGoal(ModItems.TRANSMUTATION_LUNCH_BAG, "transmutation_lunch_bag")
                .parent(root)
                .addCriterion("use_transmutation_lunch_bag", ModEventTrigger.create(ModEventTriggerType.USE_TRANSMUTATION_LUNCH_BAG))
                .save(saver, modLoc("transmutation_lunch_bag"));

        // 石磨、蒸笼系列成就
        Advancement millstone = makeTask(ModItems.MILLSTONE, "millstone")
                .parent(root)
                .addCriterion("drive_the_millstone", ModEventTrigger.create(ModEventTriggerType.DRIVE_THE_MILLSTONE))
                .save(saver, modLoc("millstone"));

        Advancement oilPot = makeTask(ModItems.OIL_POT, "oil_pot")
                .parent(millstone)
                .addCriterion("use_millstone_get_oil_pot", ModEventTrigger.create(ModEventTriggerType.USE_MILLSTONE_GET_OIL_POT))
                .save(saver, modLoc("oil_pot"));

        Advancement dough = makeTask(ModItems.RAW_DOUGH, "dough")
                .parent(millstone)
                .addCriterion("pull_the_dough", ModEventTrigger.create(ModEventTriggerType.PULL_THE_DOUGH))
                .save(saver, modLoc("dough"));

        Advancement steamer = makeTask(ModItems.STEAMER, "steamer")
                .parent(dough)
                .addCriterion("use_steamer", ModEventTrigger.create(ModEventTriggerType.USE_STEAMER))
                .save(saver, modLoc("steamer"));

        Advancement baozi = makeGoal(ModItems.BAOZI, "baozi")
                .parent(steamer)
                .addCriterion("meat_buns_beat_dogs", ModEventTrigger.create(ModEventTriggerType.MEAT_BUNS_BEAT_DOGS))
                .save(saver, modLoc("baozi"));
    }
}
