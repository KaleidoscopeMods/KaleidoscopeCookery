package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Lists;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlexStockpotRecipeCategory implements IRecipeCategory<FlexStockpotRecipe> {
    public static final RecipeType<FlexStockpotRecipe> TYPE = RecipeType.create(KaleidoscopeCookery.MOD_ID, "flex_stockpot", FlexStockpotRecipe.class);
    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/stockpot.png");
    private static final Component TITLE = ComponentUtils.formatList(List.of(
            Component.translatable("jei.kaleidoscope_cookery.flex_recipe"),
            Component.translatable("block.kaleidoscope_cookery.stockpot")
    ), CommonComponents.SPACE);
    public static final int WIDTH = 176;
    public static final int HEIGHT = 102;
    private final IDrawable bgDraw;
    private final IDrawable iconDraw;
    private final IDrawable slotDraw;

    public FlexStockpotRecipeCategory(IGuiHelper guiHelper) {
        this.bgDraw = guiHelper.createDrawable(BG, 0, 0, WIDTH, HEIGHT);
        this.iconDraw = guiHelper.createDrawableItemLike(ModItems.STOCKPOT.get());
        this.slotDraw = guiHelper.getSlotDrawable();
    }

    public static List<FlexStockpotRecipe> getRecipes() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }
        List<FlexStockpotRecipe> stockpotRecipes = Lists.newArrayList();
        stockpotRecipes.addAll(level.getRecipeManager().getAllRecipesFor(ModRecipes.FLEX_STOCKPOT_RECIPE));
        return stockpotRecipes;
    }

    @Override
    public void draw(FlexStockpotRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.bgDraw.draw(guiGraphics);

        Component type = Component.translatable("jei.kaleidoscope_cookery.flex_recipe");
        drawCenteredString(guiGraphics, type, WIDTH / 2, 90);
    }

    private void drawCenteredString(GuiGraphics guiGraphics, Component text, int centerX, int y) {
        Font font = Minecraft.getInstance().font;
        FormattedCharSequence sequence = text.getVisualOrderText();
        guiGraphics.drawString(font, sequence, centerX - font.width(sequence) / 2, y, 0x555555, false);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, FlexStockpotRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> inputs = recipe.getIngredients();
        Component text = Component.literal("*");

        for (int i = 0; i < inputs.size(); i++) {
            if (inputs.get(i).isEmpty()) {
                continue;
            }
            int xOffset = (i % 3) * 18 + 15;
            int yOffset = (i / 3) * 18 + 25;
            builder.addText(text, 9, 9)
                    .setPosition(xOffset, yOffset)
                    .setColor(0xFFFFFF)
                    .setShadow(true);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FlexStockpotRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> inputs = recipe.getIngredients();
        ItemStack output = recipe.result();
        for (int i = 0; i < inputs.size(); i++) {
            int xOffset = (i % 3) * 18 + 15;
            int yOffset = (i / 3) * 18 + 25;
            builder.addSlot(RecipeIngredientRole.INPUT, xOffset, yOffset)
                    .addIngredients(inputs.get(i))
                    .setBackground(slotDraw, -1, -1);
        }
        ISoupBase soupBase = SoupBaseManager.getSoupBase(recipe.soupBase());
        ItemStack displayStack = soupBase.getDisplayStack();
        if (!displayStack.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 72, 61)
                    .addIngredients(Ingredient.of(displayStack));
        }
        if (!recipe.carrier().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 133, 18)
                    .addIngredients(recipe.carrier());
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 143, 60)
                .addItemStack(output);
    }

    @Override
    public RecipeType<FlexStockpotRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    @Nullable
    public IDrawable getIcon() {
        return iconDraw;
    }
}
