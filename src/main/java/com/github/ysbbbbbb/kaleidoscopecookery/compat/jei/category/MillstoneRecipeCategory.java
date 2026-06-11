package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.create.CreateCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Lists;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class MillstoneRecipeCategory implements IRecipeCategory<RecipeHolder<MillstoneRecipe>> {
    public static final RecipeType<RecipeHolder<MillstoneRecipe>> TYPE = RecipeType.createRecipeHolderType(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "millstone"));

    private static final DecimalFormat FORMAT = new DecimalFormat("0.##%");
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/millstone.png");
    private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.millstone");

    public static final int WIDTH = 176 + 20;
    public static final int HEIGHT = 95;

    private final IDrawable bgDraw;
    private final IDrawable iconDraw;

    public MillstoneRecipeCategory(IGuiHelper guiHelper) {
        this.bgDraw = guiHelper.createDrawable(BG, 0, 0, WIDTH, HEIGHT);
        this.iconDraw = guiHelper.createDrawableItemLike(ModItems.MILLSTONE.get());
    }

    public static List<RecipeHolder<MillstoneRecipe>> getRecipes() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }

        List<RecipeHolder<MillstoneRecipe>> millstoneRecipes = Lists.newArrayList();
        millstoneRecipes.addAll(level.getRecipeManager().getAllRecipesFor(ModRecipes.MILLSTONE_RECIPE));

        // 机械动力兼容
        CreateCompat.getTransformRecipeForSearch(level, millstoneRecipes);

        return millstoneRecipes;
    }

    public static IRecipeSlotRichTooltipCallback addChanceTooltip(RandomOutput output) {
        return (view, tooltip) -> {
            float chance = output.chance();
            if (chance != 1.0F) {
                tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.chance", FORMAT.format(chance))
                        .withStyle(ChatFormatting.GOLD));
            }
        };
    }

    @Override
    public void draw(RecipeHolder<MillstoneRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.bgDraw.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<MillstoneRecipe> holder, IFocusGroup focuses) {
        MillstoneRecipe recipe = holder.value();
        Ingredient input = recipe.ingredient();
        List<RandomOutput> outputs = recipe.results();

        builder.addSlot(RecipeIngredientRole.INPUT, 69, 39).addIngredients(input).setStandardSlotBackground();

        // 主输出
        RandomOutput output = outputs.get(0);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 150, 47)
                .addItemStack(output.stack())
                .setOutputSlotBackground()
                .addRichTooltipCallback(addChanceTooltip(output));

        // 副产物
        if (outputs.size() > 1) {
            for (int i = 1; i < outputs.size(); i++) {
                RandomOutput randomOutput = outputs.get(i);
                int x = switch (i) {
                    case 2 -> 128;
                    case 3 -> 172;
                    default -> 150;
                };
                int y = 20;
                builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                        .addItemStack(randomOutput.stack())
                        .setStandardSlotBackground()
                        .addRichTooltipCallback(addChanceTooltip(randomOutput));
            }
        }
    }

    @Override
    public RecipeType<RecipeHolder<MillstoneRecipe>> getRecipeType() {
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
