package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
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
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReiFlexStockpotRecipeCategory implements DisplayCategory<ReiFlexStockpotRecipeCategory.FlexStockpotRecipeDisplay> {
    public static final CategoryIdentifier<FlexStockpotRecipeDisplay> ID = CategoryIdentifier.of(KaleidoscopeCookery.MOD_ID, "plugin/flex_stockpot");
    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/stockpot.png");
    private static final Component TITLE = ComponentUtils.formatList(List.of(
            Component.translatable("jei.kaleidoscope_cookery.flex_recipe"),
            Component.translatable("block.kaleidoscope_cookery.stockpot")
    ), CommonComponents.SPACE);
    public static final int WIDTH = 176;
    public static final int HEIGHT = 102;

    @Override
    public CategoryIdentifier<FlexStockpotRecipeDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public List<Widget> setupDisplay(FlexStockpotRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        int startX = bounds.x;
        int startY = bounds.y;

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0, 0, WIDTH, HEIGHT));
        widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, v) -> {
            drawCenteredString(guiGraphics, Component.translatable("jei.kaleidoscope_cookery.flex_recipe"), WIDTH / 2, 90);
        }), startX, startY, 0));

        if (!display.soupBase.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startX + 72, startY + 61))
                    .entries(display.soupBase)
                    .disableBackground()
                    .markInput());
        }
        List<EntryIngredient> inputs = display.getInputEntries();
        for (int i = 0; i < inputs.size(); i++) {
            int xOffset = (i % 3) * 18 + 15;
            int yOffset = (i / 3) * 18 + 25;
            widgets.add(Widgets.createSlot(new Point(startX + xOffset, startY + yOffset))
                    .entries(inputs.get(i))
                    .disableBackground()
                    .markInput());
            if (!inputs.get(i).isEmpty()) {
                widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, v) -> {
                    guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("*"), xOffset, yOffset, 0xFFFFFF, true);
                }), startX, startY, 0));
            }
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
    public int getDisplayWidth(FlexStockpotRecipeDisplay display) {
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
        return EntryStacks.of(ModItems.STOCKPOT.get());
    }

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new ReiFlexStockpotRecipeCategory());
        registry.addWorkstations(ReiFlexStockpotRecipeCategory.ID,
                ReiUtil.ofItem(ModItems.STOCKPOT.get()),
                ReiUtil.ofItem(ModItems.STOCKPOT_LID.get())
        );
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.getRecipeManager().getAllRecipesFor(ModRecipes.FLEX_STOCKPOT_RECIPE)
                .forEach(r -> {
                    List<EntryIngredient> inputs = ReiUtil.ofIngredients(r.getIngredients());
                    List<EntryIngredient> output = ReiUtil.ofItemStacks(r.getResultItem(RegistryAccess.EMPTY));
                    EntryIngredient carrier = r.carrier().isEmpty() ? EntryIngredient.empty() : ReiUtil.ofIngredient(r.carrier());

                    ISoupBase soupBase = SoupBaseManager.getSoupBase(r.soupBase());
                    if (soupBase == null) {
                        throw new RuntimeException("No soup found for " + r.soupBase());
                    }
                    ItemStack displayStack = soupBase.getDisplayStack();
                    EntryIngredient soupBaseEntry = displayStack.isEmpty() ? EntryIngredient.empty() : ReiUtil.ofItemStack(displayStack);

                    registry.add(new FlexStockpotRecipeDisplay(r.getId(), inputs, output, carrier, soupBaseEntry));
                });
    }

    public static class FlexStockpotRecipeDisplay extends BasicDisplay {
        public final EntryIngredient carrier;
        public final EntryIngredient soupBase;

        public FlexStockpotRecipeDisplay(ResourceLocation location, List<EntryIngredient> inputs, List<EntryIngredient> outputs, EntryIngredient carrier, EntryIngredient soupBase) {
            super(inputs, outputs, Optional.of(location));
            this.carrier = carrier;
            this.soupBase = soupBase;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return ID;
        }
    }
}
