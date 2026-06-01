package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.create.CreateCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class EmiMillstoneRecipe extends BasicEmiRecipe {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            new ResourceLocation(ModRecipes.MILLSTONE_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModItems.MILLSTONE.get()))
    );

    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/millstone.png");

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

        List<MillstoneRecipe> millstoneRecipes = Lists.newArrayList();
        millstoneRecipes.addAll(registry.getRecipeManager().getAllRecipesFor(ModRecipes.MILLSTONE_RECIPE));

        // 机械动力兼容
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            CreateCompat.getTransformRecipeForSearch(level, millstoneRecipes);
        }

        millstoneRecipes.forEach(r -> {
            List<EmiIngredient> inputs = r.getIngredients().stream().map(EmiIngredient::of).toList();
            List<EmiStack> outputs = Lists.newArrayList();

            r.results().stream()
                    .filter(output -> !output.isEmpty())
                    .forEach(output -> {
                        EmiStack emiStack = EmiStack.of(output.stack()).setChance(output.chance());
                        outputs.add(emiStack);
                    });

            registry.addRecipe(new EmiMillstoneRecipe(r.getId(), inputs, outputs));
        });
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);

        widgets.addSlot(inputs.get(0), 69, 39)
                .drawBack(true);

        // 主输出
        widgets.addSlot(outputs.get(0), 146, 47)
                .drawBack(true)
                .large(true)
                .recipeContext(this);

        // 副产物
        if (outputs.size() > 1) {
            for (int i = 1; i < outputs.size(); i++) {
                int x = 174 + i * -20;
                int y = 26;
                widgets.addSlot(outputs.get(i), x, y)
                        .drawBack(true)
                        .recipeContext(this);
            }
        }

        if (!catalysts.isEmpty()) {
            widgets.addSlot(catalysts.get(0), 115, 36)
                    .drawBack(false)
                    .recipeContext(this);
        }
    }
}
