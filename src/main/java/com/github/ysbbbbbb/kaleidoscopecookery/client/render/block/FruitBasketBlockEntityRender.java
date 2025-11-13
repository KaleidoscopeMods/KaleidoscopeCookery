package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.util.forge.ItemStackHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class FruitBasketBlockEntityRender implements BlockEntityRenderer<FruitBasketBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public FruitBasketBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(FruitBasketBlockEntity basket, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStackHandler items = basket.getItems();
        ItemRenderer itemRenderer = this.context.getItemRenderer();
        int rotation = basket.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() * 90;
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(rotation));
        poseStack.translate(-0.5, 0, -0.5);
        poseStack.translate(0.1, 0.3, 0.35);
        for (int i = 0; i < 2; i++) {
            poseStack.pushPose();
            for (int j = 0; j < 4; j++) {
                int index = i * 4 + j;
                ItemStack itemStack = items.getStackInSlot(index);
                if (!itemStack.isEmpty()) {
                    poseStack.translate(0.15, 0, 0);
                    poseStack.pushPose();
                    poseStack.translate(0, 0, index % 2 == 0 ? -0.01f : 0.01f);
                    poseStack.mulPose(Axis.YN.rotationDegrees(90));
                    poseStack.mulPose(Axis.XN.rotationDegrees(-30));
                    poseStack.scale(0.375f, 0.375f, 0.375f);
                    itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, basket.getLevel(), 0);
                    poseStack.popPose();
                }
            }
            poseStack.popPose();
            poseStack.translate(0, 0, 0.32);
        }
        poseStack.popPose();
    }
}
