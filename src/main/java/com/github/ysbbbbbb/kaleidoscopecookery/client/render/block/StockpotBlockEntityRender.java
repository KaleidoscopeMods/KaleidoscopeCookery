package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StockpotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.function.Function;

public class StockpotBlockEntityRender implements BlockEntityRenderer<StockpotBlockEntity> {
    private final BlockEntityRendererProvider.Context context;
    private final Function<ResourceLocation, ISoupBaseRender> soupBaseRender;

    public StockpotBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.soupBaseRender = Util.memoize(id -> {
            ISoupBase soupBase = SoupBaseManager.getSoupBase(id);
            if (soupBase != null) {
                return soupBase.getRender();
            }
            return null;
        });
    }

    @Override
    public void render(StockpotBlockEntity stockpot, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        if (stockpot.getBlockState().getValue(StockpotBlock.HAS_LID)) {
            return;
        }

        int status = stockpot.getStatus();
        ISoupBaseRender soupBase = this.soupBaseRender.apply(stockpot.getSoupBaseId());

        if (status == StockpotBlockEntity.PUT_INGREDIENT) {
            soupBase.renderWhenPutIngredient(stockpot, partialTick, poseStack, buffer, packedLight, packedOverlay, 0.38f);
            renderItems(stockpot, poseStack, buffer, packedLight, packedOverlay, false);
        } else if (status == StockpotBlockEntity.COOKING) {
            StockpotVisuals visuals = Objects.requireNonNullElse(stockpot.visuals, StockpotVisuals.DEFAULT);
            soupBase.renderWhenCooking(
                    stockpot, partialTick,
                    poseStack, buffer,
                    packedLight, packedOverlay,
                    visuals.cookingTexture(), 0.38f
            );
            renderItems(stockpot, poseStack, buffer, packedLight, packedOverlay, true);
        } else if (status == StockpotBlockEntity.FINISHED) {
            StockpotVisuals visuals = Objects.requireNonNullElse(stockpot.visuals, StockpotVisuals.DEFAULT);
            int takeoutCount = stockpot.getTakeoutCount();
            int maxCount = Math.min(stockpot.getResult().getCount(), StockpotBlockEntity.MAX_TAKEOUT_COUNT);
            float soupHeight = 0.065f + 0.315f / maxCount * takeoutCount;
            soupBase.renderWhenFinished(
                    stockpot, partialTick,
                    poseStack, buffer,
                    packedLight, packedOverlay,
                    visuals.finishedTexture(), soupHeight
            );
        }
    }

    private void renderItems(StockpotBlockEntity stockpot, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean isFinished) {
        NonNullList<ItemStack> items = stockpot.getInputs();
        items.forEach(stack -> {
            if (!stack.isEmpty()) {
                int random = stack.hashCode();
                long time = random + System.currentTimeMillis();
                float offsetX = (random % 100) * 0.002f;
                float offsetZ = (float) (Math.sin(time * 0.0005) * 0.2);
                float offsetY = random % 50 * 0.004f;
                float yRot = (random % 2 == 0 ? -1 : 1) * 20 + random % 10;

                BakedModel model;
                if (isFinished) {
                    model = ItemRenderReplacer.getModel(stockpot.getLevel(), stack, ItemRenderReplacerReloadListener.INSTANCE.stockpotFinished());
                } else {
                    model = ItemRenderReplacer.getModel(stockpot.getLevel(), stack, ItemRenderReplacerReloadListener.INSTANCE.stockpotCooking());
                }

                poseStack.pushPose();
                poseStack.mulPose(Axis.XP.rotationDegrees(85 + random % 10));
                poseStack.scale(0.5f, 0.5f, 0.5f);
                poseStack.translate(0.9 + offsetX, 0.9 + offsetY, -0.5 + offsetZ);
                poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
                poseStack.mulPose(Axis.ZP.rotationDegrees(random % 360));
                this.context.getItemRenderer().render(stack, ItemDisplayContext.FIXED,
                        false, poseStack, buffer, packedLight, packedOverlay, model);
                poseStack.popPose();
            }
        });
    }
}
