package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.SteamerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class SteamerBlockEntityRender implements BlockEntityRenderer<SteamerBlockEntity> {
    private final ItemRenderer itemRenderer;

    public SteamerBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SteamerBlockEntity steamer, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        // 盖住了就不渲染
        if (steamer.getBlockState().getValue(SteamerBlock.HAS_LID)) {
            return;
        }
        renderItems(steamer, poseStack, buffer, packedLight, packedOverlay);
    }

    private void renderItems(SteamerBlockEntity steamer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        NonNullList<ItemStack> items = steamer.getItems();
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            BakedModel model;
            Map<ResourceLocation, ResourceLocation> map = ItemRenderReplacerReloadListener.INSTANCE.steamer();
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());
            boolean hasCustom = key != null && map.containsKey(key);
            model = ItemRenderReplacer.getModel(steamer.getLevel(), stack, map);

            double x = (i % 2) * 0.3 + 0.35;
            double y = (i / 4) * 0.5 + 0.25 + (i % 4) * 0.01;
            double z = ((i / 2) % 2) * 0.3 + 0.35;
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            if (!hasCustom) {
                poseStack.mulPose(Axis.XN.rotationDegrees(90));
            } else {
                poseStack.translate(0, 0.4375, 0.4375);
            }
            poseStack.scale(0.5F, 0.5F, 0.5F);
            itemRenderer.render(stack, ItemDisplayContext.FIXED,
                    false, poseStack, buffer, packedLight, packedOverlay, model);
            poseStack.popPose();
        }
    }
}
