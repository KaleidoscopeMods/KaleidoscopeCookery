package com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.ScarecrowModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.layer.ScarecrowHandLayer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.layer.ScarecrowParrotOnShoulderLayer;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ScarecrowRender extends LivingEntityRenderer<ScarecrowEntity, ScarecrowModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/entity/scarecrow.png");

    public ScarecrowRender(EntityRendererProvider.Context context) {
        super(context, new ScarecrowModel(context.bakeLayer(ScarecrowModel.LAYER_LOCATION)), 0);
        this.addLayer(new ScarecrowHandLayer(this, context.getItemInHandRenderer()));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ScarecrowParrotOnShoulderLayer(this, context.getModelSet()));
    }

    @Override
    protected void setupRotations(ScarecrowEntity scarecrow, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
        float time = (float) (scarecrow.level().getGameTime() - scarecrow.lastHit) + partialTicks;
        if (time < 5.0F) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(time / 1.5F * Mth.PI) * 3.0F));
        }
    }

    @Override
    protected boolean shouldShowName(ScarecrowEntity scarecrow) {
        double distance = this.entityRenderDispatcher.distanceToSqr(scarecrow);
        return distance < 4096 && scarecrow.isCustomNameVisible();
    }

    @Override
    public ResourceLocation getTextureLocation(ScarecrowEntity pEntity) {
        return TEXTURE;
    }
}
