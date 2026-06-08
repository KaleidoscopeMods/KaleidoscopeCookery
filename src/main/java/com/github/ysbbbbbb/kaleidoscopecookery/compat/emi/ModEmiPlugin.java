package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category.*;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.RiceBowlRecipeMaker;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.CraftingRecipe;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {

    private void registerRecipesCategory(EmiRegistry registry) {
        EmiPotRecipe.register(registry);
        EmiFlexPotRecipe.register(registry);
        EmiStockpotRecipe.register(registry);
        EmiFlexStockpotRecipe.register(registry);
        EmiChoppingBoardRecipe.register(registry);
        EmiMillstoneRecipe.register(registry);
        EmiSteamerRecipe.register(registry);
        EmiTeapotRecipe.register(registry);

        for (CraftingRecipe recipe : RiceBowlRecipeMaker.createRecipes()) {
            registry.addRecipe(new EmiCraftingRecipe(
                    recipe.getIngredients().stream().map(EmiIngredient::of).toList(),
                    EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)),
                    recipe.getId(),
                    true
            ));
        }
    }

    @Override
    public void register(EmiRegistry registry) {
        registerRecipesCategory(registry);
    }
}
