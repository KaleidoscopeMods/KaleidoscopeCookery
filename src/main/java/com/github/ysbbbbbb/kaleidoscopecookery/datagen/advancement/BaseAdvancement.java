package com.github.ysbbbbbb.kaleidoscopecookery.datagen.advancement;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

import static com.github.ysbbbbbb.kaleidoscopecookery.datagen.advancement.AdvancementTools.*;

public class BaseAdvancement {
    public static void generate(Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        // 根成就 - 森罗厨房
        AdvancementHolder root = makeTask(ModItems.POT.get(), "root")
                .addCriterion("has_pot", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(TagMod.COOKERY_MOD_ITEMS).build()
                )).save(saver, modLoc("root"), existingFileHelper);

        // 菜刀系列成就
        AdvancementHolder ironKnife = makeTask(ModItems.IRON_KITCHEN_KNIFE.get(), "iron_knife")
                .parent(root)
                .addCriterion("has_iron_knife", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE).build()
                ))
                .save(saver, modLoc("iron_knife"), existingFileHelper);

        AdvancementHolder netheriteKnife = makeGoal(ModItems.NETHERITE_KITCHEN_KNIFE.get(), "netherite_knife")
                .parent(ironKnife)
                .addCriterion("has_netherite_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHERITE_KITCHEN_KNIFE.get()))
                .save(saver, modLoc("netherite_knife"), existingFileHelper);

        // 油脂相关成就
        AdvancementHolder oil = makeTask(ModItems.OIL.get(), "oil")
                .parent(ironKnife)
                .addCriterion("kill_pig", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(EntityType.PIG),
                        DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(
                                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE))
                        ))
                ))
                .addCriterion("kill_piglin", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(EntityType.PIGLIN),
                        DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(
                                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE))
                        ))
                ))
                .addCriterion("kill_zombified_piglin", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(EntityType.ZOMBIFIED_PIGLIN),
                        DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(
                                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE))
                        ))
                ))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(saver, modLoc("oil"), existingFileHelper);

        AdvancementHolder choppingBoard = makeTask(ModItems.CHOPPING_BOARD.get(), "chopping_board")
                .parent(ironKnife)
                .addCriterion("use_chopping_board", ModEventTrigger.create(ModEventTriggerType.USE_CHOPPING_BOARD))
                .save(saver, modLoc("chopping_board"), existingFileHelper);

        AdvancementHolder dangerousChef = makeChallenge(ModItems.OIL.get(), "dangerous_chef")
                .parent(oil)
                .addCriterion("kill_piglin_brute", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(EntityType.PIGLIN_BRUTE),
                        DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(
                                        ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE))
                        ))
                ))
                .save(saver, modLoc("dangerous_chef"), existingFileHelper);

        // 草帽系列成就
        AdvancementHolder strawHat = makeTask(ModItems.STRAW_HAT.get(), "straw_hat")
                .parent(root)
                .addCriterion("has_straw_hat", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW_HAT.get()))
                .save(saver, modLoc("straw_hat"), existingFileHelper);

        AdvancementHolder modSeed = makeTask(ModItems.TOMATO_SEED.get(), "tomato_seed")
                .parent(strawHat)
                .addCriterion("has_tomato_seed", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(TagMod.COOKERY_MOD_SEEDS).build()
                ))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(saver, modLoc("tomato_seed"), existingFileHelper);

        AdvancementHolder flowerStrawHat = makeTask(ModItems.STRAW_HAT_FLOWER.get(), "flower_straw_hat")
                .parent(strawHat)
                .addCriterion("has_flower_straw_hat", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW_HAT_FLOWER.get()))
                .save(saver, modLoc("flower_straw_hat"), existingFileHelper);

        AdvancementHolder farmerSet = makeTask(ModItems.FARMER_CHEST_PLATE.get(), "farmer_set")
                .parent(strawHat)
                .addCriterion("has_straw_hat", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW_HAT.get(), ModItems.STRAW_HAT_FLOWER.get()))
                .addCriterion("has_farmer_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FARMER_CHEST_PLATE.get()))
                .addCriterion("has_farmer_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FARMER_LEGGINGS.get()))
                .addCriterion("has_farmer_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FARMER_BOOTS.get()))
                .requirements(AdvancementRequirements.Strategy.AND)
                .save(saver, modLoc("farmer_set"), existingFileHelper);

        AdvancementHolder ironHoe = makeTask(Items.IRON_HOE, "iron_hoe")
                .parent(modSeed)
                .addCriterion("use_hoe_on_water_field", ModEventTrigger.create(ModEventTriggerType.USE_HOE_ON_WATER_FIELD))
                .save(saver, modLoc("iron_hoe"), existingFileHelper);

        AdvancementHolder caterpillar = makeTask(ModItems.CATERPILLAR.get(), "caterpillar")
                .parent(modSeed)
                .addCriterion("has_caterpillar", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CATERPILLAR.get()))
                .save(saver, modLoc("caterpillar"), existingFileHelper);

        AdvancementHolder dualChili = makeTask(ModItems.RED_CHILI.get(), "dual_chili")
                .parent(modSeed)
                .addCriterion("has_red_chili", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RED_CHILI.get()))
                .addCriterion("has_green_chili", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GREEN_CHILI.get()))
                .requirements(AdvancementRequirements.Strategy.AND)
                .save(saver, modLoc("dual_chili"), existingFileHelper);

        AdvancementHolder ricePanicle = makeTask(ModItems.RICE_PANICLE.get(), "rice_panicle")
                .parent(ironHoe)
                .addCriterion("has_rice_panicle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RICE_PANICLE.get()))
                .save(saver, modLoc("rice_panicle"), existingFileHelper);

        AdvancementHolder feedChicken = makeTask(ModItems.CATERPILLAR.get(), "feed_chicken")
                .parent(caterpillar)
                .addCriterion("use_caterpillar_feed_chicken", ModEventTrigger.create(ModEventTriggerType.USE_CATERPILLAR_FEED_CHICKEN))
                .save(saver, modLoc("feed_chicken"), existingFileHelper);

        AdvancementHolder fishRice = makeGoal(Items.SALMON, "fish_rice")
                .parent(ricePanicle)
                .addCriterion("place_fish_in_rice_field", ModEventTrigger.create(ModEventTriggerType.PLACE_FISH_IN_RICE_FIELD))
                .save(saver, modLoc("fish_rice"), existingFileHelper);


        // 烹饪相关成就
        AdvancementHolder stove = makeTask(ModItems.STOVE.get(), "stove")
                .parent(root)
                .addCriterion("lit_the_stove", ModEventTrigger.create(ModEventTriggerType.LIT_THE_STOVE))
                .save(saver, modLoc("stove"), existingFileHelper);

        AdvancementHolder pot = makeTask(ModItems.POT.get(), "pot")
                .parent(stove)
                .addCriterion("place_pot_on_heat_source", ModEventTrigger.create(ModEventTriggerType.PLACE_POT_ON_HEAT_SOURCE))
                .save(saver, modLoc("pot"), existingFileHelper);

        AdvancementHolder addOil = makeTask(ModItems.OIL.get(), "add_oil")
                .parent(pot)
                .addCriterion("put_oil_in_pot", ModEventTrigger.create(ModEventTriggerType.PUT_OIL_IN_POT))
                .save(saver, modLoc("add_oil"), existingFileHelper);

        AdvancementHolder stirFry = makeTask(ModItems.KITCHEN_SHOVEL.get(), "stir_fry")
                .parent(addOil)
                .addCriterion("stir_fry_in_pot", ModEventTrigger.create(ModEventTriggerType.STIR_FRY_IN_POT))
                .save(saver, modLoc("stir_fry"), existingFileHelper);

        AdvancementHolder darkCuisine = makeTask(ModItems.SUSPICIOUS_STEW_RICE_BOWL.get(), "dark_cuisine")
                .parent(stirFry)
                .addCriterion("has_suspicious_stew", InventoryChangeTrigger.TriggerInstance.hasItems(
                        FoodBiteRegistry.getItem(FoodBiteRegistry.SUSPICIOUS_STIR_FRY)
                ))
                .addCriterion("has_dark_cuisine", InventoryChangeTrigger.TriggerInstance.hasItems(
                        FoodBiteRegistry.getItem(FoodBiteRegistry.DARK_CUISINE)
                ))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(saver, modLoc("dark_cuisine"), existingFileHelper);

        AdvancementHolder burnHand = makeTask(Items.FLINT_AND_STEEL, "burn_hand")
                .parent(stirFry)
                .addCriterion("hurt_when_takeout_from_pot", ModEventTrigger.create(ModEventTriggerType.HURT_WHEN_TAKEOUT_FROM_POT))
                .save(saver, modLoc("burn_hand"), existingFileHelper);

        // 汤锅相关成就
        AdvancementHolder stockpot = makeTask(ModItems.STOCKPOT.get(), "stockpot")
                .parent(stove)
                .addCriterion("place_stockpot_on_heat_source", ModEventTrigger.create(ModEventTriggerType.PLACE_STOCKPOT_ON_HEAT_SOURCE))
                .save(saver, modLoc("stockpot"), existingFileHelper);

        AdvancementHolder addBroth = makeTask(Items.LAVA_BUCKET, "add_broth")
                .parent(stockpot)
                .addCriterion("put_soup_base_in_stockpot", ModEventTrigger.create(ModEventTriggerType.PUT_SOUP_BASE_IN_STOCKPOT))
                .save(saver, modLoc("add_broth"), existingFileHelper);

        AdvancementHolder makeSoup = makeTask(ModItems.STOCKPOT_LID.get(), "make_soup")
                .parent(addBroth)
                .addCriterion("use_lid_on_stockpot", ModEventTrigger.create(ModEventTriggerType.USE_LID_ON_STOCKPOT))
                .save(saver, modLoc("make_soup"), existingFileHelper);

        AdvancementHolder burnHandSoup = makeTask(Items.FLINT_AND_STEEL, "burn_hand_soup")
                .parent(makeSoup)
                .addCriterion("hurt_when_takeout_from_stockpot", ModEventTrigger.create(ModEventTriggerType.HURT_WHEN_TAKEOUT_FROM_STOCKPOT))
                .save(saver, modLoc("burn_hand_soup"), existingFileHelper);

        AdvancementHolder apprenticeChef = makeGoal(ModItems.BORSCHT.get(), "apprentice_chef")
                .parent(makeSoup)
                .addCriterion("has_borscht", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BORSCHT.get()))
                .save(saver, modLoc("apprentice_chef"), existingFileHelper);

        // 胀气系列成就（挑战型）
        AdvancementHolder nitrogen100 = makeGoal(Items.FEATHER, "nitrogen_100")
                .parent(root)
                .addCriterion("climb_100", flatulenceFlyHeight(
                        EntityPredicate.Builder.entity().located(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atMost(320))),
                        DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(100)),
                        LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atLeast(-60)).build()
                ))
                .save(saver, modLoc("nitrogen_100"), existingFileHelper);

        AdvancementHolder nitrogen300 = makeGoal(Items.FEATHER, "nitrogen_300")
                .parent(nitrogen100)
                .addCriterion("climb_300", flatulenceFlyHeight(
                        EntityPredicate.Builder.entity().located(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atMost(500))),
                        DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(300)),
                        LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atLeast(-60)).build()
                ))
                .save(saver, modLoc("nitrogen_300"), existingFileHelper);

        AdvancementHolder nitrogen1000 = makeGoal(Items.FEATHER, "nitrogen_1000")
                .parent(nitrogen300)
                .addCriterion("climb_1000", flatulenceFlyHeight(
                        EntityPredicate.Builder.entity().located(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atMost(1200))),
                        DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(1000)),
                        LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atLeast(-60)).build()
                ))
                .save(saver, modLoc("nitrogen_1000"), existingFileHelper);

        AdvancementHolder nitrogen3000 = makeChallenge(Items.FEATHER, "nitrogen_3000")
                .parent(nitrogen1000)
                .addCriterion("climb_3000", flatulenceFlyHeight(
                        EntityPredicate.Builder.entity().located(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atMost(3200))),
                        DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(3000)),
                        LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atLeast(-60)).build()
                ))
                .save(saver, modLoc("nitrogen_3000"), existingFileHelper);

        // 稻草人相关成就
        AdvancementHolder scarecrow = makeTask(ModItems.SCARECROW.get(), "scarecrow")
                .parent(farmerSet)
                .addCriterion("place_scarecrow", ModEventTrigger.create(ModEventTriggerType.PLACE_SCARECROW))
                .save(saver, modLoc("scarecrow"), existingFileHelper);

        AdvancementHolder scarecrowHead = makeTask(Items.SKELETON_SKULL, "scarecrow_head")
                .parent(scarecrow)
                .addCriterion("place_head_on_scarecrow", ModEventTrigger.create(ModEventTriggerType.PLACE_HEAD_ON_SCARECROW))
                .save(saver, modLoc("scarecrow_head"), existingFileHelper);
    }
}
