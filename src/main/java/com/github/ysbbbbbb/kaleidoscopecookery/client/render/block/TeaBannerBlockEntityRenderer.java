package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TeaBannerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TeaBannerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.banner.LeftBannerModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.banner.NormalBannerModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.banner.PatternModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;

import static com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery.MOD_ID;

public class TeaBannerBlockEntityRenderer implements BlockEntityRenderer<TeaBannerBlockEntity> {
    private final NormalBannerModel normalBanner;
    private final LeftBannerModel wallBanner;
    private final PatternModel patternModel;

    public TeaBannerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.normalBanner = new NormalBannerModel(context.bakeLayer(NormalBannerModel.LAYER_LOCATION));
        this.wallBanner = new LeftBannerModel(context.bakeLayer(LeftBannerModel.LAYER_LOCATION));
        this.patternModel = new PatternModel(context.bakeLayer(PatternModel.LAYER_LOCATION));
    }

    @Override
    public void render(TeaBannerBlockEntity teaBanner, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = teaBanner.getLevel();
        if (level == null) {
            return;
        }
        Direction facing = teaBanner.getBlockState().getValue(TeaBannerBlock.FACING);
        boolean wallMounted = teaBanner.getBlockState().getValue(TeaBannerBlock.FACE) == AttachFace.WALL;
        float facingDegrees = facing.get2DDataValue() * 90.0F;

        long positionPhase = teaBanner.getBlockPos().getX() * 7L
                             + teaBanner.getBlockPos().getY() * 9L
                             + teaBanner.getBlockPos().getZ() * 13L;
        float animationTime = positionPhase + level.getGameTime() + partialTick;

        float waveAngle = (-0.0125F + 0.01F * Mth.cos(Mth.PI * 2.0F * animationTime / 100.0F))
                          * Mth.HALF_PI;

        this.normalBanner.setWaveAngle(waveAngle);
        this.wallBanner.setWaveAngle(waveAngle);
        this.patternModel.setWaveAngle(waveAngle);

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YN.rotationDegrees(180.0F - facingDegrees));
        if (wallMounted) {
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        }

        ResourceLocation baseTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/entity/banner/" + teaBanner.getColor().getName() + ".png");
        VertexConsumer baseBuffer = buffer.getBuffer(RenderType.entityCutoutNoCull(baseTexture));
        if (wallMounted) {
            this.wallBanner.renderToBuffer(poseStack, baseBuffer, packedLight, packedOverlay);
        } else {
            this.normalBanner.renderToBuffer(poseStack, baseBuffer, packedLight, packedOverlay);
        }

        String patternName = TeaBannerBlockEntity.getPatternTexture(teaBanner.getPatternItem());
        if (patternName == null) {
            patternName = "tea";
        }
        ResourceLocation patternTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/entity/pattern/" + patternName + ".png");

        if (wallMounted) {
            poseStack.translate(-1, 3, 0);
        } else {
            poseStack.translate(0, 0, 0);
        }

        VertexConsumer patternBuffer = buffer.getBuffer(RenderType.entityCutoutNoCull(patternTexture));
        this.patternModel.renderToBuffer(poseStack, patternBuffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(TeaBannerBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(3);
    }
}
