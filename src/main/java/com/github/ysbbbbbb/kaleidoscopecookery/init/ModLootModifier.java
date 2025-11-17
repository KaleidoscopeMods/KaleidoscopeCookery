package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.AdvanceBlockMatchTool;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.AdvanceEntityMatchTool;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.RecipeRandomlyFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public final class ModLootModifier {
    public static final LootItemConditionType ADVANCE_ENTITY_MATCH_TOOL = new LootItemConditionType(new AdvanceEntityMatchTool.AdvanceEntityMatchToolSerializer());
    public static final LootItemConditionType ADVANCE_BLOCK_MATCH_TOOL = new LootItemConditionType(new AdvanceBlockMatchTool.AdvanceBlockMatchToolSerializer());

    public static final LootItemFunctionType RECIPE_RANDOMLY = new LootItemFunctionType(new RecipeRandomlyFunction.Serializer());

    public static void registerLootModifiers() {
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "advance_entity_match_tool"), ADVANCE_ENTITY_MATCH_TOOL);
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "advance_block_match_tool"), ADVANCE_BLOCK_MATCH_TOOL);

        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "recipe_randomly"), RECIPE_RANDOMLY);
    }
}