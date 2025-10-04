package com.github.ysbbbbbb.kaleidoscopecookery.init;


import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.AdditionLootModifier;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.AdvanceBlockMatchTool;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.AdvanceEntityMatchTool;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.RecipeRandomlyFunction;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModLootModifier {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, KaleidoscopeCookery.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADDITION =
            GLOBAL_LOOT_MODIFIER_SERIALIZER.register("addition", AdditionLootModifier.CODEC);

    public static final LootItemConditionType ADVANCE_ENTITY_MATCH_TOOL = new LootItemConditionType(new AdvanceEntityMatchTool.AdvanceEntityMatchToolSerializer());
    public static final LootItemConditionType ADVANCE_BLOCK_MATCH_TOOL = new LootItemConditionType(new AdvanceBlockMatchTool.AdvanceBlockMatchToolSerializer());

    public static final LootItemFunctionType RECIPE_RANDOMLY = new LootItemFunctionType(new RecipeRandomlyFunction.Serializer());

    @SubscribeEvent
    public static void register(RegisterEvent evt) {
        if (evt.getRegistryKey().equals(Registries.LOOT_CONDITION_TYPE)) {
            Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "advance_entity_match_tool"), ADVANCE_ENTITY_MATCH_TOOL);
            Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "advance_block_match_tool"), ADVANCE_BLOCK_MATCH_TOOL);
        }
        if (evt.getRegistryKey().equals(Registries.LOOT_FUNCTION_TYPE)) {
            Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "recipe_randomly"), RECIPE_RANDOMLY);
        }
    }
}