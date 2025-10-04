package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "jei");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new PotRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ChoppingBoardRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new StockpotRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MillstoneRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SteamerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(PotRecipeCategory.TYPE, PotRecipeCategory.getRecipes());
        registration.addRecipes(ChoppingBoardRecipeCategory.TYPE, ChoppingBoardRecipeCategory.getRecipes());
        registration.addRecipes(StockpotRecipeCategory.TYPE, StockpotRecipeCategory.getRecipes());
        registration.addRecipes(MillstoneRecipeCategory.TYPE, MillstoneRecipeCategory.getRecipes());
        registration.addRecipes(SteamerRecipeCategory.TYPE, SteamerRecipeCategory.getRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModItems.POT.get(), PotRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModItems.CHOPPING_BOARD.get(), ChoppingBoardRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModItems.STOCKPOT.get(), StockpotRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModItems.MILLSTONE.get(), MillstoneRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModItems.STEAMER.get(), SteamerRecipeCategory.TYPE);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}
