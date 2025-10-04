package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category.*;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {

    private void registerRecipesCategory(EmiRegistry registry) {
        EmiPotRecipe.register(registry);
        EmiStockpotRecipe.register(registry);
        EmiChoppingBoardRecipe.register(registry);
        EmiMillstoneRecipe.register(registry);
        EmiSteamerRecipe.register(registry);
    }

    @Override
    public void register(EmiRegistry registry) {
        registerRecipesCategory(registry);
    }
}