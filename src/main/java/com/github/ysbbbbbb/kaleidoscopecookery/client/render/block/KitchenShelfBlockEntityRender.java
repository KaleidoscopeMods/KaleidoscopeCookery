package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class KitchenShelfBlockEntityRender implements BlockEntityRenderer<KitchenShelfBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public KitchenShelfBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(KitchenShelfBlockEntity shelf, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int light, int overlay) {
        ItemStack stored = shelf.getStoredStack();
        if (stored.isEmpty()) {
            return;
        }

        int displayCount = Math.min(16, stored.getCount());
        ItemStack display = stored.copyWithCount(1);
        float rotation = shelf.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180;

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(rotation));

        // 以朝北的厨架为基准，每层从后往前放四排，每排两个。
        var tilt = Axis.XP.rotationDegrees(45);
        for (int index = 0; index < displayCount; index++) {
            int layer = index / 8;
            int slot = index % 8;
            int odd = slot / 2;

            poseStack.pushPose();
            poseStack.translate(
                    slot % 2 == 0 ? -0.25 : 0.25,
                    0.7 + layer * -0.5,
                    0.3 - odd * 0.20
            );
            poseStack.mulPose(tilt);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            context.getItemRenderer().renderStatic(
                    display, ItemDisplayContext.FIXED,
                    light, overlay, poseStack,
                    buffer, shelf.getLevel(), 0
            );
            poseStack.popPose();
        }

        poseStack.popPose();
    }
}
