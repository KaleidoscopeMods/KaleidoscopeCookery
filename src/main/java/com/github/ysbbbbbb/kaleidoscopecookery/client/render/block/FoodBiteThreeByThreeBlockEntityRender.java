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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class FoodBiteThreeByThreeBlockEntityRender implements BlockEntityRenderer<FoodBiteThreeByThreeBlockEntity> {
    private static final ResourceLocation COLD_CUT_HAM_SLICES_TEXTURE = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/block/cold_cut_ham_slices.png");

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
        coldCutHamSlicesModel.renderToBuffer(poseStack, checkerBoardBuff, packedLight, packedOverlay, -1);

        coldCutHamSlicesModel.resetBites();
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(FoodBiteThreeByThreeBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(FoodBiteThreeByThreeBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return getAABB(pos.offset(-3, 0, -3), pos.offset(3, 1, 3));
    }

    private static AABB getAABB(BlockPos pStart, BlockPos pEnd) {
        return new AABB(pStart.getX(), pStart.getY(), pStart.getZ(), pEnd.getX(), pEnd.getY(), pEnd.getZ());
    }
}
