package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
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

public class EmiSteamerRecipe extends BasicEmiRecipe {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            new ResourceLocation(ModRecipes.STEAMER_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModItems.STEAMER.get()))
    );

    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/steamer.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 78;

    public EmiSteamerRecipe(ResourceLocation id, List<EmiIngredient> inputs, List<EmiStack> outputs) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.STEAMER.get()));
        registry.addWorkstation(CATEGORY, EmiIngredient.of(TagMod.KITCHEN_KNIFE));

        registry.getRecipeManager().getAllRecipesFor(ModRecipes.STEAMER_RECIPE).forEach(r -> {
            List<EmiIngredient> inputs = r.getIngredients().stream().map(EmiIngredient::of).toList();
            List<EmiStack> outputs = List.of(EmiStack.of(r.getResultItem(RegistryAccess.EMPTY)));
            registry.addRecipe(new EmiSteamerRecipe(r.getId(), inputs, outputs));
        });
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);

        widgets.addSlot(inputs.get(0), 38, 27)
                .drawBack(false);
        widgets.addSlot(outputs.get(0), 124, 26)
                .drawBack(false)
                .recipeContext(this)
                .large(true);
    }
}
