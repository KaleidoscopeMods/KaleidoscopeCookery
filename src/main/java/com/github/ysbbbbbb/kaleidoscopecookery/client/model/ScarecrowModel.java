package com.github.ysbbbbbb.kaleidoscopecookery.client.model;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

public class ScarecrowModel extends EntityModel<ScarecrowEntity> implements ArmedModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "scarecrow"), "main");

    private final ModelPart group;
    private final ModelPart hat;
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public ScarecrowModel(ModelPart root) {
        this.group = root.getChild("group");
        this.hat = this.group.getChild("hat");
        this.leftArm = this.group.getChild("leftArm");
        this.rightArm = this.group.getChild("rightArm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition group = partdefinition.addOrReplaceChild("group", CubeListBuilder.create().texOffs(85, 82).addBox(-4.0F, -26.0278F, -2.0278F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(54, 69).addBox(-4.0F, -17.0278F, -2.0278F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(73, 65).addBox(8.0F, -26.0278F, -2.0278F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(73, 65).mirror().addBox(-8.0F, -26.0278F, -2.0278F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(13, 3).addBox(-4.5F, -35.0278F, -4.2778F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(10, 107).addBox(-14.0F, -25.7778F, -1.0278F, 28.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(101, 65).addBox(4.0F, -26.0278F, -2.0278F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(82, 65).addBox(-9.0F, -26.0278F, -2.0278F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(28, 70).addBox(-1.0F, -18.0278F, -1.0278F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0278F, 0.0278F));

        PartDefinition hat = group.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.1198F, -33.2846F, 0.6972F));

        PartDefinition headwear_r1 = hat.addOrReplaceChild("headwear_r1", CubeListBuilder.create().texOffs(66, 8).addBox(-5.0F, -3.775F, -5.025F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(77, 25).addBox(-8.0F, 1.275F, -7.975F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.2182F));

        PartDefinition leftArm = group.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -24.8F, -0.05F));

        PartDefinition rightArm = group.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-12.0F, -24.8F, -0.05F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(ScarecrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        group.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        if (arm == HumanoidArm.LEFT) {
            this.leftArm.translateAndRotate(poseStack);
        } else {
            this.rightArm.translateAndRotate(poseStack);
        }
    }
}
