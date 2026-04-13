package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.TrashCanBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.animation.TrashCanAnimation;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.TrashCanModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;
import org.joml.Vector3f;

public class TrashCanBlockEntityRender implements BlockEntityRenderer<TrashCanBlockEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/block/trash_can.png");
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    private final BlockEntityRendererProvider.Context context;
    private final TrashCanModel model;

    public TrashCanBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.model = new TrashCanModel(context.bakeLayer(TrashCanModel.LAYER_LOCATION));
    }

    @Override
    public void render(TrashCanBlockEntity trashCan, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = trashCan.getLevel();
        if (level == null) {
            return;
        }

        BlockEntityRenderDispatcher dispatcher = this.context.getBlockEntityRenderDispatcher();

        // 当玩家指向的方块就是这个时，显示文本
        if (dispatcher.cameraHitResult instanceof BlockHitResult hitResult
            && hitResult.getBlockPos().equals(trashCan.getBlockPos())) {
            Camera camera = dispatcher.camera;
            ItemRenderer itemRenderer = this.context.getItemRenderer();
            ItemStackHandler storage = trashCan.getStorage();

            poseStack.pushPose();
            poseStack.translate(0.5, 1.25, 0.5);
            poseStack.mulPose(Axis.YN.rotationDegrees(180 + camera.getYRot()));

            for (int i = 0; i < storage.getSlots(); i++) {
                ItemStack stack = storage.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    poseStack.pushPose();
                    if (i == 1) {
                        poseStack.translate(-0.5, 0, 0);
                    } else if (i == 2) {
                        poseStack.translate(0.5, 0, 0);
                    } else {
                        poseStack.translate(0, 0.25, 0);
                    }
                    itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, buffer, level, 0);
                    poseStack.popPose();
                }
            }
            poseStack.popPose();
        }

        Direction facing = trashCan.getBlockState().getValue(TrashCanBlock.FACING);
        int facingDeg = facing.get2DDataValue() * 90;
        float ageInTicks = trashCan.getLevel().getGameTime() + partialTick;
        this.model.root().getAllParts().forEach(ModelPart::resetPose);

        trashCan.putState.updateTime(ageInTicks, 1.0f);
        trashCan.putState.ifStarted(state ->
                KeyframeAnimations.animate(this.model, TrashCanAnimation.PUT, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE)
        );

        trashCan.withdrawState.updateTime(ageInTicks, 1.0f);
        trashCan.withdrawState.ifStarted(state ->
                KeyframeAnimations.animate(this.model, TrashCanAnimation.PUT, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE)
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
