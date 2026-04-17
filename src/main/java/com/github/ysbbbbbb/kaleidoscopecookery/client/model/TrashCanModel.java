package com.github.ysbbbbbb.kaleidoscopecookery.client.model;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class TrashCanModel extends HierarchicalModel<Entity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "trash_can"), "main");
    private final ModelPart root;
    private final ModelPart bone;
    private final ModelPart bone2;
    private final ModelPart eye;
    private final ModelPart bone3;

    public TrashCanModel(ModelPart root) {
        this.root = root.getChild("root");
        this.bone = this.root.getChild("bone");
        this.bone2 = this.root.getChild("bone2");
        this.eye = this.root.getChild("eye");
        this.bone3 = this.eye.getChild("bone3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(16, 40).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition bone2 = root.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(8, 0).addBox(-7.0F, -0.25F, -7.0F, 14.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.75F, 0.0F));

        PartDefinition cube_r1 = bone2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(6, 26).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(6, 26).addBox(-1.0F, -0.5F, 3.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition eye = root.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(24, 22).addBox(-5.0F, -1.5F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 0.0F));

        PartDefinition bone3 = eye.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(34, 36).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, -0.1F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
