package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.TeapotBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Consumer;

public class TeapotRecipeProvider extends ModRecipeProvider {
    public TeapotRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        TeapotBuilder.builder()
                .setTeaFluid(Fluids.WATER)
                .setIngredient(Items.IRON_NUGGET)
                .setResult(Items.APPLE.getDefaultInstance())
                .setTime(240)
                .save(consumer);
    }
}
