package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.BambooTrayBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BambooTrayBlockEntityRender implements BlockEntityRenderer<BambooTrayBlockEntity> {
    private static final double[][] SLOT_POSITIONS = {
            {0.25, 0.25}, {0.75, 0.25}, {0.25, 0.75}, {0.75, 0.75}
    };
    private final BlockEntityRendererProvider.Context context;

    public BambooTrayBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(BambooTrayBlockEntity tray, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        for (int slot = 0; slot < tray.getItems().size(); slot++) {
            ItemStack stack = tray.getItems().get(slot);
            if (stack.isEmpty()) {
                continue;
            }
            int copies = stack.getCount() == 1 ? 1 : Math.min(5, (stack.getCount() - 1) / 16 + 2);
            RandomSource random = RandomSource.create(tray.getBlockPos().asLong() + slot * 31L);
            for (int copy = 0; copy < copies; copy++) {
                poseStack.pushPose();
                double xOffset = (random.nextDouble() - 0.5) * 0.1;
                double zOffset = (random.nextDouble() - 0.5) * 0.1;
                poseStack.translate(
                        SLOT_POSITIONS[slot][0] + xOffset,
                        0.14 + copy * 0.01 + slot * 0.005,
                        SLOT_POSITIONS[slot][1] + zOffset
                );
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360));
                poseStack.scale(0.5F, 0.5F, 0.5F);
                context.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                        poseStack, bufferSource, tray.getLevel(), slot * 7 + copy);
                poseStack.popPose();
            }
        }
    }
}
