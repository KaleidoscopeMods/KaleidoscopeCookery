package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.MillstoneModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MillstoneBlockEntityRender implements BlockEntityRenderer<MillstoneBlockEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/block/millstone.png");

    private final BlockEntityRendererProvider.Context context;
    private final MillstoneModel bodyModel;

    public MillstoneBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.bodyModel = new MillstoneModel(context.bakeLayer(MillstoneModel.LAYER_LOCATION));
    }

    @Override
    public void render(MillstoneBlockEntity millstone, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = millstone.getLevel();
        if (level == null) {
            return;
        }
        Direction facing = millstone.getBlockState().getValue(MillstoneBlock.FACING);
        this.renderBody(poseStack, buffer, packedLight, packedOverlay, partialTick, facing, millstone, level);

        if (!millstone.getOutput().isEmpty()) {
            renderItems(millstone, poseStack, buffer, packedLight, packedOverlay,
                    millstone.getOutput(), context.getItemRenderer());
        } else if (!millstone.getInput().isEmpty()) {
            renderItems(millstone, poseStack, buffer, packedLight, packedOverlay,
                    millstone.getInput(), context.getItemRenderer());
        }
    }

    private void renderBody(PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn,
                            int combinedOverlayIn, float partialTick,
                            Direction facing, MillstoneBlockEntity millstone, Level level) {
        int facingDeg = facing.get2DDataValue() * 90;

        if (millstone.hasEntity()) {
            float rot = facingDeg + millstone.getRotation(level, partialTick);
            this.bodyModel.getWheel().yRot = -rot * Mth.DEG_TO_RAD;
            this.bodyModel.getRoll().zRot = rot * Mth.DEG_TO_RAD;
            this.bodyModel.getRotStick().xRot = -millstone.getLiftAngle() * Mth.DEG_TO_RAD;
        } else {
            float rot = facingDeg + millstone.getCacheRot();
            this.bodyModel.getWheel().yRot = -rot * Mth.DEG_TO_RAD;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180 - facingDeg));
        VertexConsumer checkerBoardBuff = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        bodyModel.renderToBuffer(poseStack, checkerBoardBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        this.bodyModel.getWheel().yRot = 0;
        this.bodyModel.getRoll().zRot = 0;
        this.bodyModel.getRotStick().xRot = 0;
    }

    private void renderItems(MillstoneBlockEntity millstone, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                             int packedOverlay, ItemStack renderItem, ItemRenderer itemRenderer) {
        RandomSource source = RandomSource.create(millstone.hashCode());
        int maxCount = Math.min(renderItem.getCount(), MillstoneBlockEntity.MAX_INPUT_COUNT);
        BakedModel model = ItemRenderReplacer.getModel(millstone.getLevel(), renderItem, ItemRenderReplacerReloadListener.INSTANCE.millstone());
        for (int i = 0; i < maxCount; i++) {
            poseStack.pushPose();
            poseStack.translate(0, 0.875, 0);
            poseStack.rotateAround(Axis.YP.rotationDegrees(i * 45 + source.nextInt(15)), 0.5f, 0, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(source.nextInt(20)));
            poseStack.mulPose(Axis.XN.rotationDegrees(80 + source.nextInt(20)));
            poseStack.scale(0.65F, 0.65F, 0.65F);
            itemRenderer.render(renderItem, ItemDisplayContext.FIXED,
                    false, poseStack, buffer, packedLight, packedOverlay, model);
            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(MillstoneBlockEntity millstone) {
        return true;
    }
}
