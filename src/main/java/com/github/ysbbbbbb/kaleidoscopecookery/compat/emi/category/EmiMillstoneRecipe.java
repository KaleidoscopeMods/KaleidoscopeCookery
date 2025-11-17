package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class EmiMillstoneRecipe extends BasicEmiRecipe {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            new ResourceLocation(ModRecipes.MILLSTONE_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModItems.MILLSTONE))
    );

    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/millstone.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 95;

    public EmiMillstoneRecipe(ResourceLocation id, List<EmiIngredient> inputs, List<EmiStack> outputs, List<EmiIngredient> ingredients) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = outputs;
        this.catalysts = ingredients;
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.MILLSTONE));

        registry.getRecipeManager().getAllRecipesFor(ModRecipes.MILLSTONE_RECIPE).forEach(r -> {
            List<EmiIngredient> inputs = r.getIngredients().stream().map(EmiIngredient::of).toList();
            List<EmiStack> outputs = List.of(EmiStack.of(r.getResultItem(RegistryAccess.EMPTY)));
            List<EmiIngredient> catalysts = List.of(EmiIngredient.of(r.getCarrier()));
            registry.addRecipe(new EmiMillstoneRecipe(r.getId(), inputs, outputs, catalysts));
        });
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);

        widgets.addSlot(inputs.get(0), 69, 39)
                .drawBack(true);
        widgets.addSlot(outputs.get(0), 146, 47)
                .drawBack(false)
                .recipeContext(this);
        if (!catalysts.isEmpty()) {
            widgets.addSlot(catalysts.get(0), 115, 36)
                    .drawBack(false)
                    .recipeContext(this);
        }
    }
}
