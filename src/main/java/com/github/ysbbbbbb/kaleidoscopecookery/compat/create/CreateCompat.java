package com.github.ysbbbbbb.kaleidoscopecookery.compat.create;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

public class CreateCompat {
    public static final String ID = "create";
    public static boolean IS_LOADED = false;

    public static void init() {
        ModList.get().getModContainerById(ID).ifPresent(modContainer -> {
            IS_LOADED = true;
            // 注册事件监听器
            NeoForge.EVENT_BUS.addListener(MillstoneCompat::afterMillstoneRecipeMatch);
        });
    }

    public static void getTransformRecipeForSearch(Level level, List<RecipeHolder<MillstoneRecipe>> recipes) {
        if (IS_LOADED) {
            MillstoneCompat.getTransformRecipeForSearch(level, recipes);
        }
    }
}
