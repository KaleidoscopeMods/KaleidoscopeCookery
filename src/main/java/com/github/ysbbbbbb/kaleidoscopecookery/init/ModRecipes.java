package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.*;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, KaleidoscopeCookery.MOD_ID);

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> POT_SERIALIZER = RECIPE_SERIALIZERS.register("pot", PotRecipeSerializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> CHOPPING_BOARD_SERIALIZER = RECIPE_SERIALIZERS.register("chopping_board", ChoppingBoardRecipeSerializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> STOCKPOT_SERIALIZER = RECIPE_SERIALIZERS.register("stockpot", StockpotRecipeSerializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> MILLSTONE_SERIALIZER = RECIPE_SERIALIZERS.register("millstone", MillstoneRecipeSerializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> STEAMER_SERIALIZER = RECIPE_SERIALIZERS.register("steamer", SteamerRecipeSerializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> TEAPOT_SERIALIZER = RECIPE_SERIALIZERS.register("teapot", TeapotRecipeSerializer::new);

    public static RecipeType<PotRecipe> POT_RECIPE;
    public static RecipeType<ChoppingBoardRecipe> CHOPPING_BOARD_RECIPE;
    public static RecipeType<StockpotRecipe> STOCKPOT_RECIPE;
    public static RecipeType<MillstoneRecipe> MILLSTONE_RECIPE;
    public static RecipeType<SteamerRecipe> STEAMER_RECIPE;
    public static RecipeType<TeapotRecipe> TEAPOT_RECIPE;

    @SubscribeEvent
    public static void register(RegisterEvent evt) {
        if (evt.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
            POT_RECIPE = RecipeType.simple(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "pot"));
            CHOPPING_BOARD_RECIPE = RecipeType.simple(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "chopping_board"));
            STOCKPOT_RECIPE = RecipeType.simple(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "stockpot"));
            MILLSTONE_RECIPE = RecipeType.simple(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "millstone"));
            STEAMER_RECIPE = RecipeType.simple(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "steamer"));
            TEAPOT_RECIPE = RecipeType.simple(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "teapot"));
        }
    }
}
