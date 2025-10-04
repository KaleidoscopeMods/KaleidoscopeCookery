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
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ReiChoppingBoardRecipeCategory implements DisplayCategory<DefaultCustomDisplay> {
    public static final CategoryIdentifier<DefaultCustomDisplay> ID = CategoryIdentifier.of(KaleidoscopeCookery.MOD_ID, "plugin/chopping_board");
    private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.chopping_board");
    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/chopping_board.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 78;

    @Override
    public CategoryIdentifier<? extends DefaultCustomDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public List<Widget> setupDisplay(DefaultCustomDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        int startX = bounds.x;
        int startY = bounds.y;

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0, 0, WIDTH, HEIGHT));
        widgets.add(Widgets.createSlot(new Point(startX + 38, startY + 27))
                .entries(display.getInputEntries().get(0))
                .disableBackground()
                .markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 128, startY + 30))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());

        return widgets;
    }

    @Override
    public int getDisplayWidth(DefaultCustomDisplay display) {
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
        return EntryStacks.of(ModItems.CHOPPING_BOARD.get());
    }

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new ReiChoppingBoardRecipeCategory());
        registry.addWorkstations(ReiChoppingBoardRecipeCategory.ID,
                ReiUtil.ofItem(ModItems.CHOPPING_BOARD.get()),
                ReiUtil.ofIngredient(Ingredient.of(TagMod.KITCHEN_KNIFE)));
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.getRecipeManager().getAllRecipesFor(ModRecipes.CHOPPING_BOARD_RECIPE)
                .forEach(r -> {
                    List<EntryIngredient> input = ReiUtil.ofIngredients(r.getIngredients());
                    List<EntryIngredient> output = ReiUtil.ofItemStacks(r.getResult());

                    registry.add(new DefaultCustomDisplay(r, input, output) {
                        @Override
                        public CategoryIdentifier<?> getCategoryIdentifier() {
                            return ReiChoppingBoardRecipeCategory.ID;
                        }
                    });
                });
    }
}
