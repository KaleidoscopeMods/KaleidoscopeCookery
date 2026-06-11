package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.create.CreateCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Lists;
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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReiMillstoneRecipeCategory implements DisplayCategory<ReiMillstoneRecipeCategory.MillstoneRecipeDisplay> {
    public static final CategoryIdentifier<MillstoneRecipeDisplay> ID = CategoryIdentifier.of(KaleidoscopeCookery.MOD_ID, "plugin/millstone");
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/millstone.png");
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

        List<EntryIngredient> outputs = display.getOutputEntries();

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0, 0, WIDTH, HEIGHT));

        widgets.add(Widgets.createSlot(new Point(startX + 69, startY + 39))
                .entries(display.getInputEntries().get(0))
                .markInput());

        // 主输出
        widgets.add(Widgets.createSlot(new Point(startX + 146, startY + 47))
                .entries(outputs.get(0))
                .markOutput());

        // 副产物
        if (outputs.size() > 1) {
            for (int i = 1; i < outputs.size(); i++) {
                int x = 166 + i * -20;
                int y = 26;
                widgets.add(Widgets.createSlot(new Point(startX + x, startY + y))
                        .entries(outputs.get(i)));
            }
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
        List<RecipeHolder<MillstoneRecipe>> millstoneRecipes = Lists.newArrayList();
        millstoneRecipes.addAll(registry.getRecipeManager().getAllRecipesFor(ModRecipes.MILLSTONE_RECIPE));

        // 机械动力兼容
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            CreateCompat.getTransformRecipeForSearch(level, millstoneRecipes);
        }

        millstoneRecipes.forEach(r -> {
            List<EntryIngredient> input = ReiUtil.ofIngredients(r.value().getIngredients());
            List<EntryIngredient> outputs = Lists.newArrayList();

            r.value().results().stream()
                    .filter(output -> !output.isEmpty())
                    .forEach(output -> {
                        EntryIngredient entry = ReiUtil.ofItemStack(output.stack());
                        outputs.add(entry);
                    });

            registry.add(new MillstoneRecipeDisplay(r.id(), input, outputs));
        });
    }

    public static class MillstoneRecipeDisplay extends BasicDisplay {
        public MillstoneRecipeDisplay(ResourceLocation location, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
            super(inputs, outputs, Optional.of(location));
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return ID;
        }
    }
}
