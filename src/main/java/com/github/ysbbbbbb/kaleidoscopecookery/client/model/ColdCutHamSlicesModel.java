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


public class ColdCutHamSlicesModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cold_cut_ham_slices"), "main");
    private final ModelPart food;
    private final ModelPart base;

    private final ModelPart bite1;
    private final ModelPart bite2;
    private final ModelPart bite3;
    private final ModelPart bite4;
    private final ModelPart bite5;
    private final ModelPart bite6;
    private final ModelPart bite7;
    private final ModelPart bite8;

    public ColdCutHamSlicesModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.food = root.getChild("food");
        this.bite4 = this.food.getChild("bite4");
        this.bite6 = this.food.getChild("bite6");
        this.bite7 = this.food.getChild("bite7");
        this.bite8 = this.food.getChild("bite8");
        this.bite1 = this.food.getChild("bite1");
        this.bite2 = this.food.getChild("bite2");
        this.bite3 = this.food.getChild("bite3");
        this.bite5 = this.food.getChild("bite5");
        this.base = root.getChild("base");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition food = partdefinition.addOrReplaceChild("food", CubeListBuilder.create(), PartPose.offset(8.0F, 24.0F, -8.0F));

        PartDefinition bite4 = food.addOrReplaceChild("bite4", CubeListBuilder.create().texOffs(36, 42).addBox(-7.0F, -2.0F, -1.0F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -4.0F, 8.0F));

        PartDefinition bite4_r1 = bite4.addOrReplaceChild("bite4_r1", CubeListBuilder.create().texOffs(13, 10).addBox(-3.0F, -1.0F, -3.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 7.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bite4_r2 = bite4.addOrReplaceChild("bite4_r2", CubeListBuilder.create().texOffs(13, 10).addBox(-3.0F, -1.0F, -3.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 1.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bite6 = food.addOrReplaceChild("bite6", CubeListBuilder.create().texOffs(39, 35).addBox(0.0F, -2.0F, -7.0F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -4.0F, 18.0F));

        PartDefinition bite6_r1 = bite6.addOrReplaceChild("bite6_r1", CubeListBuilder.create().texOffs(13, 10).addBox(-6.0F, -1.0F, -3.0F, 9.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition bite6_r2 = bite6.addOrReplaceChild("bite6_r2", CubeListBuilder.create().texOffs(13, 10).addBox(-6.0F, -1.0F, -3.0F, 9.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition bite7 = food.addOrReplaceChild("bite7", CubeListBuilder.create(), PartPose.offset(-18.0F, -4.0F, 18.0F));

        PartDefinition bite7_r1 = bite7.addOrReplaceChild("bite7_r1", CubeListBuilder.create().texOffs(13, 10).addBox(-3.0F, -1.0F, -6.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, -5.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition bite7_r2 = bite7.addOrReplaceChild("bite7_r2", CubeListBuilder.create().texOffs(13, 10).addBox(-3.0F, -1.0F, -6.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 1.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition bite8 = food.addOrReplaceChild("bite8", CubeListBuilder.create().texOffs(36, 42).addBox(4.0F, -2.0F, -5.0F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-18.0F, -4.0F, 8.0F));

        PartDefinition bite8_r1 = bite8.addOrReplaceChild("bite8_r1", CubeListBuilder.create().texOffs(13, 10).addBox(-3.0F, -1.0F, -6.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, -7.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition bite8_r2 = bite8.addOrReplaceChild("bite8_r2", CubeListBuilder.create().texOffs(13, 10).addBox(-3.0F, -1.0F, -6.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, -1.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition bite1 = food.addOrReplaceChild("bite1", CubeListBuilder.create(), PartPose.offset(-19.0F, -4.0F, -2.0F));

        PartDefinition bite1_r1 = bite1.addOrReplaceChild("bite1_r1", CubeListBuilder.create().texOffs(13, 10).addBox(-3.0F, -1.0F, -3.0F, 9.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, -3.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition bite1_r2 = bite1.addOrReplaceChild("bite1_r2", CubeListBuilder.create().texOffs(13, 10).addBox(-3.0F, -1.0F, -3.0F, 9.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition bite2 = food.addOrReplaceChild("bite2", CubeListBuilder.create().texOffs(39, 35).addBox(0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, -4.0F, -2.0F));

        PartDefinition bite2_r1 = bite2.addOrReplaceChild("bite2_r1", CubeListBuilder.create().texOffs(13, 10).addBox(-3.0F, -1.0F, -3.0F, 9.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, -3.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition bite2_r2 = bite2.addOrReplaceChild("bite2_r2", CubeListBuilder.create().texOffs(12, 11).addBox(-3.0F, -1.0F, -3.0F, 9.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, -3.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition bite3 = food.addOrReplaceChild("bite3", CubeListBuilder.create(), PartPose.offset(2.0F, -4.0F, -2.0F));

        PartDefinition bite3_r1 = bite3.addOrReplaceChild("bite3_r1", CubeListBuilder.create().texOffs(12, 10).addBox(-3.0F, -1.0F, -3.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 5.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bite3_r2 = bite3.addOrReplaceChild("bite3_r2", CubeListBuilder.create().texOffs(12, 10).addBox(-3.0F, -1.0F, -3.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, -1.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bite5 = food.addOrReplaceChild("bite5", CubeListBuilder.create(), PartPose.offset(2.0F, -4.0F, 18.0F));

        PartDefinition bite5_r1 = bite5.addOrReplaceChild("bite5_r1", CubeListBuilder.create().texOffs(12, 10).addBox(-6.0F, -1.0F, -3.0F, 9.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition bite5_r2 = bite5.addOrReplaceChild("bite5_r2", CubeListBuilder.create().texOffs(12, 10).addBox(-6.0F, -1.0F, -3.0F, 9.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(52, 52).addBox(-11.0F, -6.0F, 5.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(96, 0).addBox(-28.0F, -2.0F, -12.0F, 40.0F, 2.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        food.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void updateBites(int bites) {
        this.bite1.visible = bites < 8;
        this.bite2.visible = bites < 7;
        this.bite3.visible = bites < 6;
        this.bite4.visible = bites < 5;
        this.bite5.visible = bites < 4;
        this.bite6.visible = bites < 3;
        this.bite7.visible = bites < 2;
        this.bite8.visible = bites < 1;
    }

    public void resetBites() {
        this.bite1.visible = true;
        this.bite2.visible = true;
        this.bite3.visible = true;
        this.bite4.visible = true;
        this.bite5.visible = true;
        this.bite6.visible = true;
        this.bite7.visible = true;
        this.bite8.visible = true;
    }
}