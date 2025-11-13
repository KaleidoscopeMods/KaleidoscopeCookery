package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow
    protected abstract void loadSpecialItemModelAndDependencies(ModelResourceLocation modelLocation);

    @Unique
    private static final ModelResourceLocation HONEY = ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "honey"));
    @Unique
    private static final ModelResourceLocation EGG = ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "egg"));
    @Unique
    private static final ModelResourceLocation RAW_DOUGH_IN_MILLSTONE = ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "raw_dough_in_millstone"));
    @Unique
    private static final ModelResourceLocation OIL_IN_MILLSTONE = ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "oil_in_millstone"));


    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadSpecialItemModelAndDependencies(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V", shift = At.Shift.AFTER, ordinal = 1))
    private void loadSpecialItemModelAndDependencies(BlockColors blockColors, ProfilerFiller profilerFiller, Map<ResourceLocation, BlockModel> modelResources, Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>> blockStateResources, CallbackInfo ci) {
        this.loadSpecialItemModelAndDependencies(HONEY);
        this.loadSpecialItemModelAndDependencies(EGG);
        this.loadSpecialItemModelAndDependencies(RAW_DOUGH_IN_MILLSTONE);
        this.loadSpecialItemModelAndDependencies(OIL_IN_MILLSTONE);
    }
}
