package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.TeapotBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
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
                .setIngredient(ItemTags.FLOWERS)
                .setResult(TeacupRegistry.getItem(TeacupRegistry.FLOWER_TEA))
                .setTime(240)
                .save(consumer);

        TeapotBuilder.builder()
                .setTeaFluid(Fluids.WATER)
                .setIngredient(Items.IRON_NUGGET)
                .setResult(TeacupRegistry.getItem(TeacupRegistry.TIEGUANYIN))
                .setTime(240)
                .save(consumer);
    }
}
