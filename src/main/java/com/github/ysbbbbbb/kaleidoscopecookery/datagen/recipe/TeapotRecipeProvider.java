package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.TeapotBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.concurrent.CompletableFuture;

public class TeapotRecipeProvider extends ModRecipeProvider {
    public TeapotRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        TeapotBuilder.builder()
                .setTeaFluid(Fluids.WATER)
                .setIngredient(TagMod.FLOWER_TEA_INGREDIENTS)
                .setIngredientCount(1)
                .setResult(TeacupRegistry.getItem(TeacupRegistry.FLOWER_TEA))
                .setTime(240)
                .save(consumer);

        TeapotBuilder.builder()
                .setTeaFluid(Fluids.WATER)
                .setIngredient(ModItems.BARLEY_TEA_BAG)
                .setIngredientCount(1)
                .setResult(TeacupRegistry.getItem(TeacupRegistry.BARLEY_TEA))
                .setTime(240)
                .save(consumer);

        TeapotBuilder.builder()
                .setTeaFluid(Fluids.WATER)
                .setIngredient(ModItems.TIEGUANYIN_TEA_BAG)
                .setIngredientCount(1)
                .setResult(TeacupRegistry.getItem(TeacupRegistry.TIEGUANYIN))
                .setTime(240)
                .save(consumer);

        TeapotBuilder.builder()
                .setTeaFluid(Fluids.WATER)
                .setIngredient(ModItems.BILUOCHUN_TEA_BAG)
                .setIngredientCount(1)
                .setResult(TeacupRegistry.getItem(TeacupRegistry.BILUOCHUN))
                .setTime(240)
                .save(consumer);

        TeapotBuilder.builder()
                .setTeaFluid(Fluids.WATER)
                .setIngredient(ModItems.OOLONG_TEA_BAG)
                .setIngredientCount(1)
                .setResult(TeacupRegistry.getItem(TeacupRegistry.OOLONG))
                .setTime(240)
                .save(consumer);

        TeapotBuilder.builder()
                .setTeaFluid(Fluids.WATER)
                .setIngredient(ModItems.SAKURA_FUBUKI_TEA_BAG)
                .setIngredientCount(1)
                .setResult(TeacupRegistry.getItem(TeacupRegistry.SAKURA_FUBUKI))
                .setTime(240)
                .save(consumer);

        TeapotBuilder.builder()
                .setTeaFluid(NeoForgeMod.MILK.get())
                .setIngredient(ModItems.BUTTER_TEA_BAG)
                .setIngredientCount(1)
                .setResult(TeacupRegistry.getItem(TeacupRegistry.BUTTER_TEA))
                .setTime(240)
                .save(consumer);
    }
}
