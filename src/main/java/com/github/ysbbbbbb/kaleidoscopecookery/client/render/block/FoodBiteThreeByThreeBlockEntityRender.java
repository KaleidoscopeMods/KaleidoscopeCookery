package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteThreeByThreeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.food.FoodBiteThreeByThreeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.ColdCutHamSlicesModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class FoodBiteThreeByThreeBlockEntityRender implements BlockEntityRenderer<FoodBiteThreeByThreeBlockEntity> {
    private static final ResourceLocation COLD_CUT_HAM_SLICES_TEXTURE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/block/cold_cut_ham_slices.png");

    private final ColdCutHamSlicesModel coldCutHamSlicesModel;

    public FoodBiteThreeByThreeBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.coldCutHamSlicesModel = new ColdCutHamSlicesModel(context.bakeLayer(ColdCutHamSlicesModel.LAYER_LOCATION));
    }

    @Override
    public void render(FoodBiteThreeByThreeBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof FoodBiteThreeByThreeBlock block)) {
            return;
        }

        Direction facing = blockState.getValue(MillstoneBlock.FACING);
        int facingDeg = facing.get2DDataValue() * 90;
        int bites = blockState.getValue(block.getBites());

        poseStack.pushPose();
        coldCutHamSlicesModel.updateBites(bites);

        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180 - facingDeg));
        VertexConsumer checkerBoardBuff = buffer.getBuffer(RenderType.entityCutoutNoCull(COLD_CUT_HAM_SLICES_TEXTURE));
        coldCutHamSlicesModel.renderToBuffer(poseStack, checkerBoardBuff, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        coldCutHamSlicesModel.resetBites();
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(FoodBiteThreeByThreeBlockEntity pBlockEntity) {
        return true;
    }
}
