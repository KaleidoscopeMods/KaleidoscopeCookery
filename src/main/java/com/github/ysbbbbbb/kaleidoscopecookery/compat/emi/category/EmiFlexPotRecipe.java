package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class EmiFlexPotRecipe extends BasicEmiRecipe {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            new ResourceLocation(ModRecipes.FLEX_POT_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModItems.POT.get()))
    );

    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/pot.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 102;
    private final int stirFryCount;

    public EmiFlexPotRecipe(ResourceLocation id, List<EmiIngredient> inputs, List<EmiStack> outputs, List<EmiIngredient> catalysts, int stirFryCount) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = outputs;
        this.catalysts = catalysts;
        this.stirFryCount = stirFryCount;
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.POT.get()));
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.KITCHEN_SHOVEL.get()));

        registry.getRecipeManager().getAllRecipesFor(ModRecipes.FLEX_POT_RECIPE).forEach(r -> {
            List<EmiIngredient> inputs = r.getIngredients().stream().map(EmiIngredient::of).toList();
            List<EmiStack> outputs = List.of(EmiStack.of(r.getResultItem(RegistryAccess.EMPTY)));
            List<EmiIngredient> catalysts = r.carrier().isEmpty() ? List.of() : List.of(EmiIngredient.of(r.carrier()));

            registry.addRecipe(new EmiFlexPotRecipe(r.getId(), inputs, outputs, catalysts, r.stirFryCount()));
        });
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);
        widgets.addText(Component.translatable("jei.kaleidoscope_cookery.flex_recipe"), WIDTH / 2, 5, 0x555555, false)
                .horizontalAlign(TextWidget.Alignment.CENTER);
        widgets.addText(Component.translatable("jei.kaleidoscope_cookery.pot.stir_fry_count", stirFryCount), WIDTH / 2, 85, 0x555555, false)
                .horizontalAlign(TextWidget.Alignment.CENTER);

        for (int i = 0; i < inputs.size(); i++) {
            int xOffset = (i % 3) * 18 + 15;
            int yOffset = (i / 3) * 18 + 24;
            widgets.addSlot(inputs.get(i), xOffset, yOffset)
                    .drawBack(false);
            if (!inputs.get(i).isEmpty()) {
                widgets.addText(Component.literal("*"), xOffset, yOffset, 0xFFFFFF, true);
            }
        }
        if (!catalysts.isEmpty()) {
            widgets.addSlot(catalysts.get(0), 133, 18)
                    .drawBack(false);
        }
        widgets.addSlot(outputs.get(0), 143, 60)
                .drawBack(false)
                .recipeContext(this);
    }
}
