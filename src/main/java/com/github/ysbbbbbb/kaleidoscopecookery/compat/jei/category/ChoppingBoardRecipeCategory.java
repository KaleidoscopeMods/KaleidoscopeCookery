package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Lists;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChoppingBoardRecipeCategory implements IRecipeCategory<RecipeHolder<ChoppingBoardRecipe>> {
    public static final RecipeType<RecipeHolder<ChoppingBoardRecipe>> TYPE = RecipeType.createRecipeHolderType(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "chopping_board"));

    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/chopping_board.png");
    private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.chopping_board");

    public static final int WIDTH = 176;
    public static final int HEIGHT = 78;

    private final IDrawable bgDraw;
    private final IDrawable iconDraw;

    public ChoppingBoardRecipeCategory(IGuiHelper guiHelper) {
        this.bgDraw = guiHelper.createDrawable(BG, 0, 0, WIDTH, HEIGHT);
        this.iconDraw = guiHelper.createDrawableItemStack(ModItems.CHOPPING_BOARD.get().getDefaultInstance());
    }

    public static List<RecipeHolder<ChoppingBoardRecipe>> getRecipes() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }
        List<RecipeHolder<ChoppingBoardRecipe>> choppingBoardRecipes = Lists.newArrayList();
        choppingBoardRecipes.addAll(level.getRecipeManager().getAllRecipesFor(ModRecipes.CHOPPING_BOARD_RECIPE));
        return choppingBoardRecipes;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ChoppingBoardRecipe> recipe, IFocusGroup focuses) {
        Ingredient input = recipe.value().getIngredient();
        List<ItemStack> outputs = recipe.value().getResults();

        builder.addSlot(RecipeIngredientRole.INPUT, 38, 27).addIngredients(input);
        if (!outputs.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 30).addItemStack(outputs.get(0));
        }
        int additionalCount = outputs.size() - 1;
        int startX = 128 - (additionalCount - 1) * 12;
        for (int index = 1; index < outputs.size(); index++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, startX + (index - 1) * 24, 54)
                    .setStandardSlotBackground()
                    .addItemStack(outputs.get(index));
        }
    }

    @Override
    public void draw(RecipeHolder<ChoppingBoardRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.bgDraw.draw(guiGraphics);
    }

    @Override
    public RecipeType<RecipeHolder<ChoppingBoardRecipe>> getRecipeType() {
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
