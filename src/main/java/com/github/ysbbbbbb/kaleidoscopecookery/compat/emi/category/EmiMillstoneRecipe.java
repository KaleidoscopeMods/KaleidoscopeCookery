package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
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
            ResourceLocation.parse(ModRecipes.MILLSTONE_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModItems.MILLSTONE.get()))
    );

    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/millstone.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 95;

    public EmiMillstoneRecipe(ResourceLocation id, List<EmiIngredient> inputs, List<EmiStack> outputs) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.MILLSTONE.get()));

        registry.getRecipeManager().getAllRecipesFor(ModRecipes.MILLSTONE_RECIPE).forEach(holder -> {
            MillstoneRecipe recipe = holder.value();
            List<EmiIngredient> inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
            List<EmiStack> outputs = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
            registry.addRecipe(new EmiMillstoneRecipe(holder.id(), inputs, outputs));
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
