package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BambooTrayRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class EmiBambooTrayRecipe extends BasicEmiRecipe {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            new ResourceLocation(ModRecipes.BAMBOO_TRAY_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModItems.BAMBOO_TRAY.get()))
    );

    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/bamboo_tray.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 78;

    private final BambooTrayRecipe.Subtype subtype;
    private final int duration;

    public EmiBambooTrayRecipe(ResourceLocation id, List<EmiIngredient> inputs, List<EmiStack> outputs,
                               BambooTrayRecipe.Subtype subtype, int duration) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = outputs;
        this.subtype = subtype;
        this.duration = duration;
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.BAMBOO_TRAY.get()));

        registry.getRecipeManager().getAllRecipesFor(ModRecipes.BAMBOO_TRAY_RECIPE).forEach(recipe -> {
            List<EmiIngredient> inputs = List.of(EmiIngredient.of(recipe.getIngredient()));
            List<EmiStack> outputs = List.of(EmiStack.of(recipe.getResult()));
            registry.addRecipe(new EmiBambooTrayRecipe(recipe.getId(), inputs, outputs,
                    recipe.getSubtype(), recipe.getDuration()));
        });
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);

        widgets.addSlot(inputs.get(0), 41, 27)
                .drawBack(false);
        widgets.addSlot(outputs.get(0), 131, 29)
                .drawBack(false)
                .recipeContext(this);

        Component process = Component.translatable(
                "jei.kaleidoscope_cookery.bamboo_tray." + subtype.getSerializedName(), duration / 20);
        int x = WIDTH / 2 - Minecraft.getInstance().font.width(process) / 2;
        widgets.addText(process, x, 68, 0x555555, false);
    }
}
