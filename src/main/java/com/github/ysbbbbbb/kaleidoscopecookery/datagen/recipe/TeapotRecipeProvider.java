package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.TeapotBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTeaFluids;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class TeapotRecipeProvider extends ModRecipeProvider {
    public TeapotRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        TeapotBuilder.builder()
                .setIngredient(Items.IRON_NUGGET)
                .setBaseTeaFluid(ModTeaFluids.WATER)
                .setResultTeaFluid(ModTeaFluids.TIEGUANYIN)
                .setTime(240)
                .save(consumer, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "teapot/tieguanyin"));

        TeapotBuilder.builder()
                .setIngredient(ItemTags.FLOWERS)
                .setBaseTeaFluid(ModTeaFluids.WATER)
                .setResultTeaFluid(ModTeaFluids.FLOWER_TEA)
                .setTime(240)
                .save(consumer, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "teapot/flower_tea"));
    }
}
