package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TableBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TableBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class TableBlockEntityRender implements BlockEntityRenderer<TableBlockEntity> {
    private static final BiFunction<DyeColor, Integer, ResourceLocation> CACHE_MODEL = Util.memoize((color, position) -> {
        String name = color.getName();
        if (position == TableBlock.SINGLE) {
            return new ResourceLocation(KaleidoscopeCookery.MOD_ID, "block/carpet/table/" + name + "_single");
        }
        if (position == TableBlock.MIDDLE) {
            return new ResourceLocation(KaleidoscopeCookery.MOD_ID, "block/carpet/table/" + name + "_middle");
        }
        if (position == TableBlock.LEFT) {
            return new ResourceLocation(KaleidoscopeCookery.MOD_ID, "block/carpet/table/" + name + "_left");
        }
        return new ResourceLocation(KaleidoscopeCookery.MOD_ID, "block/carpet/table/" + name + "_right");
    });

    private final BlockEntityRendererProvider.Context context;

    public TableBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(TableBlockEntity table, float pPartialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = this.context.getItemRenderer();
        BlockState blockState = table.getBlockState();
        Direction.Axis axis = blockState.getValue(TableBlock.AXIS);

        if (blockState.getValue(TableBlock.HAS_CARPET)) {
            int position = blockState.getValue(TableBlock.POSITION);
            ResourceLocation cacheModel = CACHE_MODEL.apply(table.getColor(), position);
            int rotation = axis == Direction.Axis.X ? 180 : 270;
            poseStack.pushPose();
            poseStack.translate(0.5, 0, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(-rotation));
            poseStack.translate(-0.5, 0, -0.5);
            BakedModel model = itemRenderer.getItemModelShaper().getModelManager().getModel(cacheModel);
            RenderType renderType = RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS);
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, renderType, true, false);
            itemRenderer.renderModelLists(model, ItemStack.EMPTY, packedLight, packedOverlay, poseStack, vertexConsumer);
            poseStack.popPose();
        }

        NonNullList<ItemStack> items = table.getItems();
        int count = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isEmpty()) {
                continue;
            }
            count++;
        }

        if (count == 0) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 1.3125, 0.5);
        poseStack.scale(0.65F, 0.65F, 0.65F);

        if (count == 1) {
            this.rotation(poseStack, axis);
            ItemStack stack1 = items.get(0);
            this.offsetBlockItem(stack1, poseStack);
            itemRenderer.renderStatic(stack1, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, table.getLevel(), 0);
        } else if (count == 2) {
            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(-0.25, 0, 0.1);
            ItemStack stack1 = items.get(0);
            this.offsetBlockItem(stack1, poseStack);
            itemRenderer.renderStatic(stack1, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, table.getLevel(), 0);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(0.25, 0.01, -0.1);
            ItemStack stack2 = items.get(1);
            this.offsetBlockItem(stack2, poseStack);
            itemRenderer.renderStatic(stack2, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, table.getLevel(), 0);
            poseStack.popPose();
        } else if (count == 3) {
            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(0.25, 0, -0.2);
            ItemStack stack1 = items.get(0);
            this.offsetBlockItem(stack1, poseStack);
            itemRenderer.renderStatic(stack1, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, table.getLevel(), 0);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(-0.25, 0.01, 0);
            ItemStack stack2 = items.get(1);
            this.offsetBlockItem(stack2, poseStack);
            itemRenderer.renderStatic(stack2, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, table.getLevel(), 0);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(0.24, 0.02, 0.2);
            ItemStack stack3 = items.get(2);
            this.offsetBlockItem(stack3, poseStack);
            itemRenderer.renderStatic(stack3, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, table.getLevel(), 0);
            poseStack.popPose();
        } else {
            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(0.25, 0, -0.3);
            ItemStack stack1 = items.get(0);
            this.offsetBlockItem(stack1, poseStack);
            itemRenderer.renderStatic(stack1, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, table.getLevel(), 0);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(-0.24, 0.01, -0.1);
            ItemStack stack2 = items.get(1);
            this.offsetBlockItem(stack2, poseStack);
            itemRenderer.renderStatic(stack2, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, table.getLevel(), 0);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(0.24, 0.02, 0.1);
            ItemStack stack3 = items.get(2);
            this.offsetBlockItem(stack3, poseStack);
            itemRenderer.renderStatic(stack3, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, table.getLevel(), 0);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(-0.25, 0.03, 0.3);
            ItemStack stack4 = items.get(3);
            this.offsetBlockItem(stack4, poseStack);
            itemRenderer.renderStatic(stack4, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, table.getLevel(), 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private void rotation(PoseStack poseStack, Direction.Axis axis) {
        if (axis == Direction.Axis.X) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
        } else {
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
        }
    }

    private void offsetBlockItem(ItemStack stack, PoseStack poseStack) {
        if (stack.getItem() instanceof BlockItem) {
            poseStack.translate(0, -0.25, 0);
        }
    }
}
