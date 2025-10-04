package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
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
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReiPotRecipeCategory implements DisplayCategory<ReiPotRecipeCategory.PotRecipeDisplay> {
    public static final CategoryIdentifier<PotRecipeDisplay> ID = CategoryIdentifier.of(KaleidoscopeCookery.MOD_ID, "plugin/pot");
    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/pot.png");
    private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.pot");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 102;

    @Override
    public CategoryIdentifier<PotRecipeDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public List<Widget> setupDisplay(PotRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        int startX = bounds.x;
        int startY = bounds.y;
        Component stirFryCount = Component.translatable("jei.kaleidoscope_cookery.pot.stir_fry_count", display.stirFryCount);

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0, 0, WIDTH, HEIGHT));
        widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, v) -> {
            drawCenteredString(guiGraphics, stirFryCount, WIDTH / 2, 85);
        }), startX, startY, 0));

        List<EntryIngredient> inputs = display.getInputEntries();
        for (int i = 0; i < inputs.size(); i++) {
            int xOffset = (i % 3) * 18 + 15;
            int yOffset = (i / 3) * 18 + 24;
            widgets.add(Widgets.createSlot(new Point(startX + xOffset, startY + yOffset))
                .entries(inputs.get(i))
                .disableBackground()
                .markInput());
        }
        if (!display.carrier.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startX + 133, startY + 18))
                    .entries(display.carrier)
                    .disableBackground()
                    .markInput());
        }
        widgets.add(Widgets.createSlot(new Point(startX + 143, startY + 60))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());

        return widgets;
    }

    private void drawCenteredString(GuiGraphics guiGraphics, Component text, int centerX, int y) {
        Font font = Minecraft.getInstance().font;
        FormattedCharSequence sequence = text.getVisualOrderText();
        guiGraphics.drawString(font, sequence, centerX - font.width(sequence) / 2, y, 0x555555, false);
    }

    @Override
    public int getDisplayWidth(PotRecipeDisplay display) {
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
        return EntryStacks.of(ModItems.POT.get());
    }

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new ReiPotRecipeCategory());
        registry.addWorkstations(ReiPotRecipeCategory.ID,
                ReiUtil.ofItem(ModItems.POT.get()),
                ReiUtil.ofIngredient(Ingredient.of(TagMod.KITCHEN_SHOVEL)),
                ReiUtil.ofItem(ModItems.OIL.get())
        );
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.getRecipeManager().getAllRecipesFor(ModRecipes.POT_RECIPE)
                .forEach(r -> {
                    List<EntryIngredient> inputs = ReiUtil.ofIngredients(r.getIngredients());
                    List<EntryIngredient> output = ReiUtil.ofItemStacks(r.getResultItem(RegistryAccess.EMPTY));
                    EntryIngredient carrier = r.carrier().isEmpty() ? EntryIngredient.empty() : ReiUtil.ofIngredient(r.carrier());

                    registry.add(new PotRecipeDisplay(r.getId(), inputs, output, carrier, r.stirFryCount()));
                });
    }

    public static class PotRecipeDisplay extends BasicDisplay {
        public final EntryIngredient carrier;
        public final int stirFryCount;

        public PotRecipeDisplay(ResourceLocation location, List<EntryIngredient> inputs, List<EntryIngredient> outputs, EntryIngredient carrier, int stirFryCount) {
            super(inputs, outputs, Optional.of(location));
            this.carrier = carrier;
            this.stirFryCount = stirFryCount;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return ID;
        }
    }
}
