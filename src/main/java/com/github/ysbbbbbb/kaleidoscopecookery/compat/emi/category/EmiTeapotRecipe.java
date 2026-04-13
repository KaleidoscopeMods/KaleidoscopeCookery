package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class EmiTeapotRecipe extends BasicEmiRecipe {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            new ResourceLocation(ModRecipes.TEAPOT_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModItems.TEAPOT.get()))
    );

    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/teapot.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 78;

    private final Item fluidBucket;
    private final int brewTime;

    public EmiTeapotRecipe(ResourceLocation id, List<EmiIngredient> inputs, List<EmiStack> outputs, Item fluidBucket, int brewTime) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = outputs;
        this.fluidBucket = fluidBucket;
        this.brewTime = brewTime;
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.TEAPOT.get()));

        registry.getRecipeManager().getAllRecipesFor(ModRecipes.TEAPOT_RECIPE).forEach(r -> {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(r.teaFluid());
            Item bucket = fluid == null ? Items.WATER_BUCKET : fluid.getBucket();
            List<EmiIngredient> inputs = List.of(EmiIngredient.of(Arrays.stream(r.ingredient().getItems())
                    .map(stack -> EmiStack.of(stack.copyWithCount(r.ingredientCount())))
                    .toList()));
            List<EmiStack> outputs = List.of(EmiStack.of(r.result().copyWithCount(TeapotRecipe.OUTPUT_COUNT)));
            registry.addRecipe(new EmiTeapotRecipe(r.getId(), inputs, outputs, bucket, r.time()));
        });
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);

        widgets.addSlot(EmiStack.of(fluidBucket), 65, 3);
        widgets.addSlot(inputs.get(0), 83, 3);
        widgets.addSlot(outputs.get(0), 128, 30)
                .drawBack(false)
                .recipeContext(this);

        Component brewTimeText = Component.translatable("jei.kaleidoscope_cookery.teapot.time", brewTime / 20);
        int x = WIDTH / 2 - Minecraft.getInstance().font.width(brewTimeText) / 2;
        widgets.addText(brewTimeText, x, 70, 0x555555, false);
    }
}
