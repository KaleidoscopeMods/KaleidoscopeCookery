package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BambooTrayRecipe;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReiBambooTrayRecipeCategory implements DisplayCategory<ReiBambooTrayRecipeCategory.BambooTrayRecipeDisplay> {
    public static final CategoryIdentifier<BambooTrayRecipeDisplay> ID = CategoryIdentifier.of(KaleidoscopeCookery.MOD_ID, "plugin/bamboo_tray");
    private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.bamboo_tray");
    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/bamboo_tray.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 78;

    @Override
    public CategoryIdentifier<BambooTrayRecipeDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public List<Widget> setupDisplay(BambooTrayRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        int startX = bounds.x;
        int startY = bounds.y;
        Component process = Component.translatable(
                "jei.kaleidoscope_cookery.bamboo_tray." + display.subtype.getSerializedName(),
                display.duration / 20);

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0, 0, WIDTH, HEIGHT));
        widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, delta) -> {
            drawCenteredString(guiGraphics, process, WIDTH / 2, 68);
        }), startX, startY, 0));
        widgets.add(Widgets.createSlot(new Point(startX + 41, startY + 27))
                .entries(display.getInputEntries().get(0))
                .disableBackground()
                .markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 131, startY + 29))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());

        return widgets;
    }

    private void drawCenteredString(GuiGraphics guiGraphics, Component text, int centerX, int y) {
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, text, centerX - font.width(text) / 2, y, 0x555555, false);
    }

    @Override
    public int getDisplayWidth(BambooTrayRecipeDisplay display) {
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
        return EntryStacks.of(ModItems.BAMBOO_TRAY.get());
    }

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new ReiBambooTrayRecipeCategory());
        registry.addWorkstations(ID, ReiUtil.ofItem(ModItems.BAMBOO_TRAY.get()));
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.getRecipeManager().getAllRecipesFor(ModRecipes.BAMBOO_TRAY_RECIPE).forEach(recipe -> {
            List<EntryIngredient> inputs = ReiUtil.ofIngredients(recipe.getIngredient());
            List<EntryIngredient> outputs = ReiUtil.ofItemStacks(recipe.getResult());
            registry.add(new BambooTrayRecipeDisplay(recipe.getId(), inputs, outputs,
                    recipe.getSubtype(), recipe.getDuration()));
        });
    }

    public static class BambooTrayRecipeDisplay extends BasicDisplay {
        private final BambooTrayRecipe.Subtype subtype;
        private final int duration;

        public BambooTrayRecipeDisplay(ResourceLocation location, List<EntryIngredient> inputs,
                                       List<EntryIngredient> outputs, BambooTrayRecipe.Subtype subtype, int duration) {
            super(inputs, outputs, Optional.of(location));
            this.subtype = subtype;
            this.duration = duration;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return ID;
        }
    }
}
