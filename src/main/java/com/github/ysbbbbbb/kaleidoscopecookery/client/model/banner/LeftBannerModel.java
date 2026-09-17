package com.github.ysbbbbbb.kaleidoscopecookery.client.model.banner;

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


public class LeftBannerModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "left_banner"), "main");

    private final ModelPart bone;
    private final ModelPart front;
    private final ModelPart back;

    public LeftBannerModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.bone = root.getChild("bone");
        this.front = this.bone.getChild("front");
        this.back = this.bone.getChild("back");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(64, 107).addBox(-1.0F, -3.0F, -16.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(36, 79).addBox(-1.0F, 3.0F, -16.0F, 2.0F, 2.0F, 38.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 4.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 12.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition front = bone.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, -28).addBox(0.0F, 0.0F, -14.0F, 0.0F, 41.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 8.0F));

        PartDefinition back = bone.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, -28).addBox(0.0F, 0.0F, -14.0F, 0.0F, 41.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 8.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public void setWaveAngle(float angle) {
        this.front.zRot = angle;
        this.back.zRot = angle;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
    }
}
