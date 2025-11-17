package com.github.ysbbbbbb.kaleidoscopecookery.client.model;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class MillstoneModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "millstone"), "main");
    private final ModelPart base;
    private final ModelPart wheel;
    private final ModelPart roll;
    private final ModelPart stick;
    private final ModelPart rotStick;

    public MillstoneModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.base = root.getChild("base");
        this.wheel = root.getChild("wheel");
        this.roll = this.wheel.getChild("roll");
        this.stick = this.wheel.getChild("stick");
        this.rotStick = this.stick.getChild("rotStick");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 87).addBox(-24.0F, -14.0F, -8.0F, 32.0F, 8.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 31).addBox(-21.0F, -6.0F, -5.0F, 26.0F, 6.0F, 26.0F, new CubeDeformation(0.0F))
                .texOffs(22, 64).addBox(-18.0F, -15.0F, -2.0F, 20.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));

        PartDefinition wheel = partdefinition.addOrReplaceChild("wheel", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition roll = wheel.addOrReplaceChild("roll", CubeListBuilder.create().texOffs(76, 0).addBox(-6.0F, -6.0F, -7.0F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.0F));

        PartDefinition stick = wheel.addOrReplaceChild("stick", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = stick.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(83, 31).addBox(-8.0F, -2.0F, -2.0F, 16.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 1.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r2 = stick.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(111, 84).addBox(-2.0F, -17.5F, -2.0F, 4.0F, 30.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r3 = stick.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(114, 72).addBox(-2.0F, -21.5F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 3.0F, -36.5F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r4 = stick.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(86, 32).addBox(-7.0F, -2.0F, -2.0F, 14.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -17.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition rotStick = stick.addOrReplaceChild("rotStick", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -17.0F));

        PartDefinition cube_r5 = rotStick.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(111, 84).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 29.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        wheel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public ModelPart getWheel() {
        return wheel;
    }

    public ModelPart getRoll() {
        return roll;
    }

    public ModelPart getRotStick() {
        return rotStick;
    }
}
