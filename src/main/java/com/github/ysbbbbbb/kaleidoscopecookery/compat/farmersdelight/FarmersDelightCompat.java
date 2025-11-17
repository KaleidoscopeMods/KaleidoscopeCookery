package com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import net.minecraft.world.level.Level;

import java.util.List;

public class FarmersDelightCompat {
    public static final String ID = "farmersdelight";
    public static boolean IS_LOADED = false;

    public static void init() {
//        ModList.get().getModContainerById(ID).ifPresent(modContainer -> {
//            IS_LOADED = true;
//            // 注册事件监听器
//            MinecraftForge.EVENT_BUS.addListener(CookingPotCompat::afterStockpotRecipeMatch);
//        });
    }

    public static void getTransformRecipeForJei(Level level, List<StockpotRecipe> recipes) {
        if (IS_LOADED) {
            CookingPotCompat.getTransformRecipeForJei(level, recipes);
        }
    }
}
