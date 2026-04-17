package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReiTeapotRecipeCategory implements DisplayCategory<ReiTeapotRecipeCategory.TeapotRecipeDisplay> {
    public static final CategoryIdentifier<TeapotRecipeDisplay> ID = CategoryIdentifier.of(KaleidoscopeCookery.MOD_ID, "plugin/teapot");
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/teapot.png");
    private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.teapot");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 88;

    @Override
    public CategoryIdentifier<TeapotRecipeDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public List<Widget> setupDisplay(TeapotRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        int startX = bounds.x;
        int startY = bounds.y + 4;
        Component brewTime = Component.translatable("jei.kaleidoscope_cookery.teapot.time", display.brewTime / 20);

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0, 0, WIDTH, HEIGHT));
        widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, v) -> {
            drawCenteredString(guiGraphics, brewTime, WIDTH / 2, 70);
        }), startX, startY, 0));
        widgets.add(Widgets.createSlot(new Point(startX + 65, startY + 3))
                .entries(display.getInputEntries().get(0))
                .markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 83, startY + 3))
                .entries(display.getInputEntries().get(1))
                .markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 128, startY + 30))
                .entries(display.getOutputEntries().get(0))
                .backgroundEnabled(false)
                .markOutput());

        return widgets;
    }

    private void drawCenteredString(GuiGraphics guiGraphics, Component text, int centerX, int y) {
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, text, centerX - font.width(text) / 2, y, 0x555555, false);
    }

    @Override
    public int getDisplayWidth(TeapotRecipeDisplay display) {
        return WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModItems.TEAPOT.get());
    }

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new ReiTeapotRecipeCategory());
        registry.addWorkstations(ReiTeapotRecipeCategory.ID, ReiUtil.ofItem(ModItems.TEAPOT.get()));
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.getRecipeManager().getAllRecipesFor(ModRecipes.TEAPOT_RECIPE)
                .forEach(r -> {
                    Fluid fluid = BuiltInRegistries.FLUID.get(r.value().teaFluid());
                    Item bucket = fluid.getBucket();
                    List<EntryIngredient> fluidInput = ReiUtil.ofItems(bucket);
                    List<EntryIngredient> inputs = List.of(EntryIngredient.of(Arrays.stream(r.value().ingredient().getItems())
                            .map(stack -> EntryStacks.of(stack.copyWithCount(r.value().ingredientCount())))
                            .toList()));
                    List<EntryIngredient> output = ReiUtil.ofItemStacks(r.value().result().copyWithCount(TeapotRecipe.OUTPUT_COUNT));

                    registry.add(new TeapotRecipeDisplay(r.id(), fluidInput, inputs, output, r.value().time()));
                });
    }

    public static class TeapotRecipeDisplay extends BasicDisplay {
        public final int brewTime;

        public TeapotRecipeDisplay(ResourceLocation location, List<EntryIngredient> fluidInput,
                                   List<EntryIngredient> ingredientInput,
                                   List<EntryIngredient> outputs, int brewTime) {
            super(List.of(fluidInput.getFirst(), ingredientInput.getFirst()), outputs, Optional.of(location));
            this.brewTime = brewTime;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return ID;
        }
    }
}
