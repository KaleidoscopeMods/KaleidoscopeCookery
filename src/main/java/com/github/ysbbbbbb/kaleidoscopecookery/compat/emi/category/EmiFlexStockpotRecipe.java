package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
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

public class EmiFlexStockpotRecipe extends BasicEmiRecipe {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.parse(ModRecipes.FLEX_STOCKPOT_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModItems.STOCKPOT.get()))
    );

    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/stockpot.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 102;

    private final EmiStack soupBase;

    public EmiFlexStockpotRecipe(ResourceLocation id, List<EmiIngredient> inputs, List<EmiStack> outputs, List<EmiIngredient> catalysts, EmiStack soupBase) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = outputs;
        this.catalysts = catalysts;
        this.soupBase = soupBase;
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.STOCKPOT.get()));
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.STOCKPOT_LID.get()));

        registry.getRecipeManager().getAllRecipesFor(ModRecipes.FLEX_STOCKPOT_RECIPE).forEach(recipeHolder -> {
            FlexStockpotRecipe r = recipeHolder.value();
            List<EmiIngredient> inputs = r.getIngredients().stream().map(EmiIngredient::of).toList();
            List<EmiStack> outputs = List.of(EmiStack.of(r.getResultItem(RegistryAccess.EMPTY)));
            List<EmiIngredient> catalysts = r.carrier().isEmpty() ? List.of() : List.of(EmiIngredient.of(r.carrier()));
            ISoupBase soupBase = SoupBaseManager.getSoupBase(r.soupBase());
            if (soupBase == null) {
                throw new RuntimeException("No soup found for " + r.soupBase());
            }
            EmiStack soupBaseItem = EmiStack.of(soupBase.getDisplayStack());

            registry.addRecipe(new EmiFlexStockpotRecipe(recipeHolder.id(), inputs, outputs, catalysts, soupBaseItem));
        });
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);
        widgets.addText(Component.translatable("jei.kaleidoscope_cookery.flex_recipe"), WIDTH / 2, 90, 0x555555, false)
                .horizontalAlign(TextWidget.Alignment.CENTER);

        for (int i = 0; i < inputs.size(); i++) {
            int xOffset = (i % 3) * 18 + 15;
            int yOffset = (i / 3) * 18 + 25;
            widgets.addSlot(inputs.get(i), xOffset, yOffset)
                    .drawBack(false);
            if (!inputs.get(i).isEmpty()) {
                widgets.addText(Component.literal("*"), xOffset, yOffset, 0xFFFFFF, true);
            }
        }
        if (!soupBase.isEmpty()) {
            widgets.addSlot(soupBase, 72, 61)
                    .drawBack(false);
        }
        if (!catalysts.isEmpty()) {
            widgets.addSlot(catalysts.getFirst(), 133, 18)
                    .drawBack(false);
        }
        widgets.addSlot(outputs.getFirst(), 143, 60)
                .drawBack(false)
                .recipeContext(this);
    }
}
