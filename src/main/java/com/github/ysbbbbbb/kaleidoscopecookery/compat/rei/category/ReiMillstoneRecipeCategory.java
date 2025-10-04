package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReiMillstoneRecipeCategory implements DisplayCategory<ReiMillstoneRecipeCategory.MillstoneRecipeDisplay> {
    public static final CategoryIdentifier<MillstoneRecipeDisplay> ID = CategoryIdentifier.of(KaleidoscopeCookery.MOD_ID, "plugin/millstone");
    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/millstone.png");
    private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.millstone");

    public static final int WIDTH = 176;
    public static final int HEIGHT = 95;

    @Override
    public CategoryIdentifier<MillstoneRecipeDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public List<Widget> setupDisplay(MillstoneRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        int startX = bounds.x;
        int startY = bounds.y;

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0, 0, WIDTH, HEIGHT));
        widgets.add(Widgets.createSlot(new Point(startX + 69, startY + 39))
                .entries(display.getInputEntries().get(0))
                .markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 146, startY + 47))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());
        if (!display.carrier.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startX + 115, startY + 36))
                    .entries(display.carrier)
                    .disableBackground()
                    .markInput());
        }

        return widgets;
    }

    @Override
    public int getDisplayWidth(MillstoneRecipeDisplay display) {
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
        return EntryStacks.of(ModItems.MILLSTONE.get());
    }

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new ReiMillstoneRecipeCategory());
        registry.addWorkstations(ReiMillstoneRecipeCategory.ID,
                ReiUtil.ofItem(ModItems.MILLSTONE.get())
        );
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.getRecipeManager().getAllRecipesFor(ModRecipes.MILLSTONE_RECIPE)
                .forEach(r -> {
                    List<EntryIngredient> input = ReiUtil.ofIngredients(r.getIngredients());
                    List<EntryIngredient> output = ReiUtil.ofItemStacks(r.getResult());
                    EntryIngredient carrier = r.getCarrier().isEmpty() ? EntryIngredient.empty() : ReiUtil.ofIngredient(r.getCarrier());

                    registry.add(new MillstoneRecipeDisplay(r.getId(), input, output, carrier));
                });
    }

    public static class MillstoneRecipeDisplay extends BasicDisplay {
        public final EntryIngredient carrier;

        public MillstoneRecipeDisplay(ResourceLocation location, List<EntryIngredient> inputs, List<EntryIngredient> outputs, EntryIngredient carrier) {
            super(inputs, outputs, Optional.of(location));
            this.carrier = carrier;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return ID;
        }
    }
}
