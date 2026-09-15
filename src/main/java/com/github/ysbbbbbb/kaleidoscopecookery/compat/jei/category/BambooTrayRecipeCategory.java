package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BambooTrayRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BambooTrayRecipeCategory implements IRecipeCategory<BambooTrayRecipe> {
    public static final RecipeType<BambooTrayRecipe> TYPE = RecipeType.create(KaleidoscopeCookery.MOD_ID, "bamboo_tray", BambooTrayRecipe.class);

    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/bamboo_tray.png");
    private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.bamboo_tray");

    public static final int WIDTH = 176;
    public static final int HEIGHT = 78;

    private final IDrawable bgDraw;
    private final IDrawable iconDraw;

    public BambooTrayRecipeCategory(IGuiHelper guiHelper) {
        this.bgDraw = guiHelper.createDrawable(BG, 0, 0, WIDTH, HEIGHT);
        this.iconDraw = guiHelper.createDrawableItemLike(ModItems.BAMBOO_TRAY.get());
    }

    public static List<BambooTrayRecipe> getRecipes() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }
        return level.getRecipeManager().getAllRecipesFor(ModRecipes.BAMBOO_TRAY_RECIPE);
    }

    @Override
    public void draw(BambooTrayRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.bgDraw.draw(guiGraphics);
        Component process = Component.translatable(
                "jei.kaleidoscope_cookery.bamboo_tray." + recipe.getSubtype().getSerializedName(),
                recipe.getDuration() / 20);
        drawCenteredString(guiGraphics, process, WIDTH / 2, 68);
    }

    private void drawCenteredString(GuiGraphics guiGraphics, Component text, int centerX, int y) {
        Font font = Minecraft.getInstance().font;
        FormattedCharSequence sequence = text.getVisualOrderText();
        guiGraphics.drawString(font, sequence, centerX - font.width(sequence) / 2, y, 0x555555, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BambooTrayRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 41, 27).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 131, 29).addItemStack(recipe.getResult());
    }

    @Override
    public RecipeType<BambooTrayRecipe> getRecipeType() {
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
        return this.iconDraw;
    }
}
