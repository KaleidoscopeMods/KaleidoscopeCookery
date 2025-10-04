package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.ingredinet.AnyIngredient;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.*;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, KaleidoscopeCookery.MOD_ID);

    public static RegistryObject<RecipeSerializer<?>> POT_SERIALIZER = RECIPE_SERIALIZERS.register("pot", PotRecipeSerializer::new);
    public static RegistryObject<RecipeSerializer<?>> CHOPPING_BOARD_SERIALIZER = RECIPE_SERIALIZERS.register("chopping_board", ChoppingBoardRecipeSerializer::new);
    public static RegistryObject<RecipeSerializer<?>> STOCKPOT_SERIALIZER = RECIPE_SERIALIZERS.register("stockpot", StockpotRecipeSerializer::new);
    public static RegistryObject<RecipeSerializer<?>> MILLSTONE_SERIALIZER = RECIPE_SERIALIZERS.register("millstone", MillstoneRecipeSerializer::new);
    public static RegistryObject<RecipeSerializer<?>> STEAMER_SERIALIZER = RECIPE_SERIALIZERS.register("steamer", SteamerRecipeSerializer::new);

    public static RecipeType<PotRecipe> POT_RECIPE;
    public static RecipeType<ChoppingBoardRecipe> CHOPPING_BOARD_RECIPE;
    public static RecipeType<StockpotRecipe> STOCKPOT_RECIPE;
    public static RecipeType<MillstoneRecipe> MILLSTONE_RECIPE;
    public static RecipeType<SteamerRecipe> STEAMER_RECIPE;

    @SubscribeEvent
    public static void register(RegisterEvent evt) {
        if (evt.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
            POT_RECIPE = RecipeType.simple(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pot"));
            CHOPPING_BOARD_RECIPE = RecipeType.simple(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chopping_board"));
            STOCKPOT_RECIPE = RecipeType.simple(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot"));
            MILLSTONE_RECIPE = RecipeType.simple(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "millstone"));
            STEAMER_RECIPE = RecipeType.simple(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "steamer"));
        }

        if (evt.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(AnyIngredient.ID, AnyIngredient.Serializer.INSTANCE);
        }
    }
}
