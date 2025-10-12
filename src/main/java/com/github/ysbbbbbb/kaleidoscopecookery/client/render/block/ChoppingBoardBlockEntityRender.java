package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ChoppingBoardBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ChoppingBoardBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ChoppingBoardBlockEntityRender implements BlockEntityRenderer<ChoppingBoardBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ChoppingBoardBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ChoppingBoardBlockEntity choppingBoard, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ResourceLocation modelId = choppingBoard.getModelId();
        if (modelId == null) {
            return;
        }
        if (!modelId.equals(choppingBoard.previousModel)) {
            choppingBoard.previousModel = modelId;
            choppingBoard.cacheModels = new ResourceLocation[choppingBoard.getMaxCutCount() + 1];
            for (int i = 0; i <= choppingBoard.getMaxCutCount(); i++) {
                choppingBoard.cacheModels[i] = new ResourceLocation(modelId.getNamespace(), "chopping_board/" + modelId.getPath() + "/" + i);
            }
        }
        if (choppingBoard.cacheModels == null) {
            return;
        }
        int index = Math.min(choppingBoard.getCurrentCutCount(), choppingBoard.cacheModels.length - 1);
        ResourceLocation cacheModel = choppingBoard.cacheModels[index];

        poseStack.pushPose();
        int rotation = choppingBoard.getBlockState().getValue(ChoppingBoardBlock.FACING).get2DDataValue();
        poseStack.translate(0.5D, 0, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation * 90));
        poseStack.translate(-0.5D, 0.125, -0.5D);
        BakedModel model = itemRenderer.getItemModelShaper().getModelManager().getModel(cacheModel);
        RenderType renderType = Sheets.cutoutBlockSheet();
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, renderType, true, false);
        itemRenderer.renderModelLists(model, ItemStack.EMPTY, packedLight, packedOverlay, poseStack, vertexConsumer);
        poseStack.popPose();
    }
}
