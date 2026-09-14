package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.TeapotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.animation.TeapotAnimation;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.TeapotModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class TeapotBlockEntityRender implements BlockEntityRenderer<TeapotBlockEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/block/teapot.png");
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    private final TeapotModel model;

    public TeapotBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.model = new TeapotModel(context.bakeLayer(TeapotModel.LAYER_LOCATION));
    }

    @Override
    public void render(TeapotBlockEntity teapot, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = teapot.getLevel();
        if (level == null) {
            return;
        }

        Direction facing = teapot.getBlockState().getValue(TeapotBlock.FACING);
        int facingDeg = facing.get2DDataValue() * 90;
        float ageInTicks = teapot.getLevel().getGameTime() + partialTick;
        int variant = teapot.getBlockState().getValue(TeapotBlock.VARIANT);

        this.model.root().getAllParts().forEach(ModelPart::resetPose);
        this.model.updateVariant(variant);
        teapot.boilingState.updateTime(ageInTicks, 1.0f);
        teapot.boilingState.ifStarted(state ->
                KeyframeAnimations.animate(this.model, TeapotAnimation.BOILING, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE)
        );

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180 - facingDeg));
        VertexConsumer checkerBoardBuff = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.renderToBuffer(poseStack, checkerBoardBuff, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
