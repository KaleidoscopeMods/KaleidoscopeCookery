package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight.FarmersDelightCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Lists;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class EmiStockpotRecipe extends BasicEmiRecipe {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            new ResourceLocation(ModRecipes.STOCKPOT_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModItems.STOCKPOT))
    );

    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/stockpot.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 102;

    private final EmiStack soupBase;

    public EmiStockpotRecipe(ResourceLocation id, List<EmiIngredient> inputs, List<EmiStack> outputs, List<EmiIngredient> catalysts, EmiStack soupBase) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = outputs;
        this.catalysts = catalysts;
        this.soupBase = soupBase;
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.STOCKPOT));
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.STOCKPOT_LID));

        registry.getRecipeManager().getAllRecipesFor(ModRecipes.STOCKPOT_RECIPE).forEach(r -> {
            registerRecipe(registry, r);
        });

        // 农夫乐事兼容
        List<StockpotRecipe> compatRecipes = Lists.newArrayList();
        FarmersDelightCompat.getTransformRecipeForJei(Minecraft.getInstance().level, compatRecipes);
        if (!compatRecipes.isEmpty()) {
            compatRecipes.forEach(r -> registerRecipe(registry, r));
        }
    }

    private static void registerRecipe(EmiRegistry registry, StockpotRecipe r) {
        List<EmiIngredient> inputs = r.getIngredients().stream().map(EmiIngredient::of).toList();
        List<EmiStack> outputs = List.of(EmiStack.of(r.getResultItem(RegistryAccess.EMPTY)));
        List<EmiIngredient> catalysts = List.of(EmiIngredient.of(r.carrier()));
        ISoupBase soupBase = SoupBaseManager.getSoupBase(r.soupBase());
        if (soupBase == null) {
            throw new RuntimeException("No soup found for " + r.soupBase());
        }
        EmiStack soupBaseItem = EmiStack.of(soupBase.getDisplayStack());
        registry.addRecipe(new EmiStockpotRecipe(r.getId(), inputs, outputs, catalysts, soupBaseItem));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);

        for (int i = 0; i < inputs.size(); i++) {
            int xOffset = (i % 3) * 18 + 15;
            int yOffset = (i / 3) * 18 + 25;
            widgets.addSlot(inputs.get(i), xOffset, yOffset)
                    .drawBack(false);
        }
        if (!soupBase.isEmpty()) {
            widgets.addSlot(soupBase, 72, 61)
                    .drawBack(false);
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
